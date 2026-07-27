package com.scheduler.app.backend.aREST.Service;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.scheduler.Base.Exception.ValidationException;
import com.scheduler.Base.Service.BaseService;
import com.scheduler.app.backend.Command.Models.Command;
import com.scheduler.app.backend.Command.Service.CommandService;
import com.scheduler.app.backend.Hardware.Models.Hardware;
import com.scheduler.app.backend.Hardware.Service.HardwareService;
import com.scheduler.app.backend.Messaging.Board.Models.ArraySerial.BoardTaskSerial;
import com.scheduler.app.backend.Messaging.Board.Models.BoardLogin;
import com.scheduler.app.backend.Messaging.Board.Models.BoardRegister;
import com.scheduler.app.backend.Messaging.Board.Models.DeviceCheck;
import com.scheduler.app.backend.Messaging.Models.BoardTask;
import com.scheduler.app.backend.aREST.Models.Board;
import com.scheduler.app.backend.aREST.Repo.BoardRepo;


@Service
public class BoardService extends BaseService<Board, Long> {
    
    private final BoardRepo boardRepo;
    public final HardwareService hardwareService;
    private final DeviceService deviceService;
    public final CommandService commandService;
    public final ScheduleService scheduleService;
    public final TaskService taskService;
    public final BoardQueueService boardQueueService;


    public BoardService(BoardRepo boardRepo, DeviceService deviceService,HardwareService hardwareService, CommandService commandService, ScheduleService scheduleService, TaskService taskService, BoardQueueService boardQueueService) {
        this.boardRepo = boardRepo;
        this.hardwareService = hardwareService;
        this.deviceService = deviceService;
        this.commandService = commandService;
        this.scheduleService = scheduleService;
        this.taskService = taskService;
        this.boardQueueService = boardQueueService;
    }

    @Override
    protected JpaRepository<Board, Long> repository() {
        return boardRepo;
    }
  
    private String genereateBoardId(long id){
        String boardId=generateRandString(4)+id;
        return boardId;
    }
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    @Override
    protected void beforeSave(Board entity, Map<String, String> errors, Map<String, String> warnings) {
        boolean existingBoard = entity.getId() > 0 && boardRepo.existsById(entity.getId());
        String name = isBlank(entity.getName()) ? entity.getName() : entity.getName().trim();
        String boardId = entity.getBoardId();

        if (!existingBoard) {
            entity.setName(name);
            if (getDataInt("select count(name) from board where name=" + quoteParam(name)) > 0) {
                errors.put("name", "board with the name " + name + " exists");
            }
            if (!isBlank(boardId)) {
                entity.setBoardId(boardId.trim());
                if (boardRepo.findBoardByBoardId(entity.getBoardId()) != null) {
                    errors.put("boardId", "board with that unique ID has been created");
                }
            }
            if (entity.getHardware() == null) {
                errors.put("hardwareId", "Hardware does not exists");
            }
            if (!errors.isEmpty()) throw new ValidationException(errors, null);
            return;
        }

        long id = entity.getId();
        if (getDataInt("select count(name) from board where name=" + quoteParam(name) + " and id!=" + id) > 0) {
            errors.put("name", "Board name already exists");
        }
        if (!isBlank(boardId) && getDataInt("select count(board_id) from board where board_id=" + quoteParam(boardId) + " and id!=" + id) > 0) {
            errors.put("boardId", "Unique Board ID already exists");
        }
        if (isBlank(boardId)) errors.put("boardId", "Unique Board ID is required");
        if (TimeUnit.MILLISECONDS.toMinutes(entity.getOffline()) < 30) {
            errors.put("offline", "Offline time must be at least 30 minutes or more");
        }
        if (entity.getDevMode()) {
            if (isBlank(entity.getDevServerUrl())) errors.put("devServerUrl", "Development server url cannot be blank");
            else if (!entity.getDevServerUrl().startsWith("http://") && !entity.getDevServerUrl().startsWith("https://")) errors.put("devServerUrl", "Development server url must start with http:// or https://");
            if (isBlank(entity.getDevWsUrl())) errors.put("devWsUrl", "Development WebSocket url cannot be blank");
            else if (!entity.getDevWsUrl().startsWith("ws://") && !entity.getDevWsUrl().startsWith("wss://")) errors.put("devWsUrl", "Development WebSocket url must start with ws:// or wss://");
        }
        if (!errors.isEmpty()) throw new ValidationException(errors, null);

        Board persistedBoard = boardRepo.getReferenceById(id);
        boolean devMode = entity.getDevMode();
        String devServerUrl = entity.getDevServerUrl();
        String devWsUrl = entity.getDevWsUrl();
        long offline = entity.getOffline();
        boolean restartTimeout = entity.getRestartTimeout();

        entity.setBoardKey(persistedBoard.getBoardKey());
        entity.setSsid(persistedBoard.getSsid());
        entity.setMacAddress(persistedBoard.getMacAddress());
        entity.setIp(persistedBoard.getIp());
        entity.setStatus(persistedBoard.getStatus());
        entity.setArest(persistedBoard.getArest());
        entity.setArestCommand(persistedBoard.getArestCommand());
        entity.setSocket(persistedBoard.getSocket());
        entity.setPeriodicCheck(persistedBoard.getPeriodicCheck());
        entity.setRamUsage(persistedBoard.getRamUsage());
        entity.setActivated(persistedBoard.getActivated());
        entity.setWebsocketId(persistedBoard.getWebsocketId());
        entity.setLastConnectDateTime(persistedBoard.getLastConnectDateTime());
        entity.setTimeout(persistedBoard.getTimeout());
        entity.setTasksExecuted(persistedBoard.getTasksExecuted());
        entity.setDevice(persistedBoard.getDevice());
        entity.setSection(persistedBoard.getSection());
        entity.setHardware(persistedBoard.getHardware());
        entity.setBoardOperations(persistedBoard.getBoardOperations());
        entity.setHardwardId(persistedBoard.getHardwardId());
        entity.setName(name);
        entity.setBoardId(boardId.trim());
        entity.setOffline(offline);
        entity.setRestartTimeout(restartTimeout);
        entity.setDevMode(devMode);
        entity.setDevServerUrl(devMode ? devServerUrl.trim() : persistedBoard.getDevServerUrl());
        entity.setDevWsUrl(devMode ? devWsUrl.trim() : persistedBoard.getDevWsUrl());
    }

    @Override
    protected void afterSave(Board entity) {
        if (isBlank(entity.getBoardId())) {
            entity.setBoardId(genereateBoardId(entity.getId()));
            boardRepo.save(entity);
        }
    }
    // socket board add
    public Board addBoardSocket(String name,long hardwareObj,String boardUniqueId){
        Board newBoard=new Board();
        newBoard.setName(name);
        newBoard.setSocket(true);
        Hardware hard=hardwareService.getBoard(hardwareObj);
        if (hard != null) {
            newBoard.setHardware(hard);
        }
        newBoard.setBoardId(boardUniqueId);
        return save(newBoard);
    }
 
    public Board updateBoardObject(Board entry){
        return boardRepo.save(entry);
    }
    @Transactional
    public void offlineBoard(){
        List<Board> offline=boardRepo.getBoardsPassBy();
        if(offline.size()>0){
            for(Board bo:offline){
                List <Long> devIds=new ArrayList<>();
                bo.getDevice().stream().map(dev->devIds.add(dev.getId()));
                bo.getBoardOperations().clear();
                deviceService.routesService.updateRouteOffline(devIds);
                //taskService.deactiveTask(devIds);
            }
            boardRepo.saveAll(offline);
            System.out.println("offline board size "+offline.size());
        }
    }
   
    // occasional routine check
    @Transactional
    public DeviceCheck routineCheck(long id,int ram,String ip){
        DeviceCheck check=null;
        Board boardExist=boardRepo.findById(id).get();
        if(boardExist!=null){
            // if board is offline within board period and restart enabled, reset board
            // else restart routine tasks
            Instant dt=Instant.now();
            Instant lastCon=boardExist.getLastConnectDateTime();
            long diff=Duration.between(lastCon, dt).toMillis();
            // temp
            if(diff>=boardExist.getOffline()){
                scheduleService.startRoutineSchedule(boardExist);
            }
            boardExist.setLastConnectDateTime(dt);
            
            boardExist.setRamUsage(ram);
            if(boardExist.getIp()!=ip&&ip!="") boardExist.setIp(ip);
            check=createDeviceCheck(boardExist);
            boardRepo.save(boardExist);
            List <BoardTaskSerial> taskLists=new ArrayList<>();
            List <BoardTaskSerial> scheduledTasks=taskService.getNextTasks(boardExist.getId());
            if(scheduledTasks.size()>0)taskLists=scheduledTasks;
            //System.out.println(taskLists.size());
            check.setTasks(taskLists);
            
        }
        return check;
    }
    
    // when board first powered on
    @Transactional
    public BoardLogin startup(BoardRegister register,String ip,int ram,String ssid,String macAddress,int freeHeap,int heap,int systemTotalTask,int taskTotal,int totalQueue,int millis){
        BoardLogin check=null;
        String boardId=register.getBoardId().trim();
        Board exist=boardRepo.findBoardByBoardId(boardId);
        if(exist!=null){
            Instant dt=Instant.now();
            exist.setLastConnectDateTime(dt);
            taskService.purgeOldTasks(exist.getId());
            // verify password
            check=createBoardLogin(exist);
            if(exist.getIp()!=ip) exist.setIp(ip);
            exist.setRamUsage(ram);
            if(exist.getSsid()==null||exist.getSsid().equals("")||!exist.getSsid().equals(ssid)) exist.setSsid(ssid);
            if(exist.getMacAddress()==null||exist.getMacAddress().equals("")||!exist.getMacAddress().equals(macAddress)) exist.setMacAddress(macAddress);
            // activate device to register
            if(!exist.getActivated()){
                exist.setActivated(true);
            }
            executeQuery("delete from board_queue where board="+exist.getId());
            // check if there are any startup tasks. if so add startup tasks to the scheduler for the board to process
            if(exist.getDevice().size()>0&&exist.getDevice()!=null){
                String devicesId=Arrays.toString(deviceService.getDevicesById(exist.getId())).replace("[","").replace("]","");
                int startUpCount=getDataInt("select count(id) from schedule where startup=true and device_id in ("+quoteParam(devicesId)+")");
            }
            scheduleService.startStartupSchedule(exist);
            Board update=boardRepo.save(exist);
            BoardTask boTsk=commandService.getRequestConnection();
            List <BoardTaskSerial> taskLists=new ArrayList<>();
            List <BoardTaskSerial> scheduledTasks=taskService.getNextTasks(exist.getId());
            if(scheduledTasks.size()>0)taskLists.addAll(scheduledTasks);
            // add htp request connection command
            if(boTsk!=null&&!exist.getDevMode()){
                boTsk.initTaskId(update.getId());
                boTsk.setDelayInterval(60000);
                boTsk.setRunTarget(0);
                taskLists.add(new BoardTaskSerial(boTsk));
                boardQueueService.addToQueueBoardTask(boTsk, update, null);
            }
            
            if(taskLists.size()>0&&taskLists.size()<50){
                check.setTasks(taskLists);
            }else
            {
                // open websocket or message carrier to process startup commands

            }
            
        }
        return check;
    }
     
    private DeviceCheck createDeviceCheck(Board board){
        DeviceCheck newCheck=new DeviceCheck();
        return newCheck;
    }
    private BoardLogin createBoardLogin(Board board){
        BoardLogin login=new BoardLogin();
        login.setId(board.getId());
        if(board.getDevMode()){
            login.setDevMode(board.getDevMode());
            login.setDevServerUrl(board.getDevServerUrl());
            login.setDevWsUrl(board.getDevWsUrl());
        }
        return login;
    }
    
    // save ws session id into database for use
    @Transactional
    public Board setWsConnection(long id,String sessionId,int ram,boolean updateLastConnect){
        Board boardRec=null;
        Optional<Board> findBoard=boardRepo.findById(id);
        if(findBoard.isPresent()){
            boardRec=findBoard.get();
            boardRec.setWebsocketId(sessionId);
            Instant dt=Instant.now();
            if(updateLastConnect){
                boardRec.setLastConnectDateTime(dt);
            }
            if(ram>0){
                boardRec.setRamUsage(ram);
            }
            
            boardRec=boardRepo.save(boardRec);
        }
        return boardRec;
    }

    public Board updateBoard(Board obj,long id){
        if (!boardRepo.existsById(id)) {
            return null;
        }
        obj.setId(id);
        obj=save(obj);
        
        return obj;
    }

    public List<Board> getBoards(){
        List <Board> list=boardRepo.findAll();
        return list;
    }
    public Board getBoard(long id){
        return boardRepo.findById(id).get();
    }
    public Board getBoardById(long id){
        return boardRepo.findById(id).get();
    }
    public String deleteBoard(long id){
        String output="Does not exist";
        if(boardRepo.existsById(id)){
            Board item=boardRepo.getReferenceById(id);
            boardRepo.deleteById(id);
            output="board remove "+item.getName();
        }
        return output;
    }
   
    public Optional<Board> findBoard(long id){
        return boardRepo.findById(id);
    }
    public Hardware getBoardHardwareId(String id){
        Hardware hardware=boardRepo.findBoardByBoardId(id).getHardware();
        //List<HardwarePins> pins=hardware.getPins().stream().sorted(Comparator.comparing(HardwarePins::getBoardPin)).collect(Collectors.toList());
        //hardware.setPins(pins);
        return hardware;
    }
    public Board getBoardByBoardId(String id){
        return boardRepo.findBoardByBoardId(id);
    }
    
}
