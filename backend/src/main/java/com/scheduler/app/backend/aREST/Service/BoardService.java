package com.scheduler.app.backend.aREST.Service;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.scheduler.Base.Base;
import com.scheduler.Base.Exception.ValidationException;
import com.scheduler.app.backend.Command.Models.Command;
import com.scheduler.app.backend.Command.Service.CommandService;
import com.scheduler.app.backend.Hardware.Models.Hardware;
import com.scheduler.app.backend.Hardware.Service.HardwareService;
import com.scheduler.app.backend.Messaging.Board.Models.BoardLogin;
import com.scheduler.app.backend.Messaging.Board.Models.BoardRegister;
import com.scheduler.app.backend.Messaging.Board.Models.DeviceCheck;
import com.scheduler.app.backend.Messaging.Models.BoardTask;
import com.scheduler.app.backend.aREST.Models.Board;
import com.scheduler.app.backend.aREST.Repo.BoardRepo;


@Service
public class BoardService extends Base {
    
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
  
    private String genereateBoardId(long id){
        String boardId=generateRandString(4)+id;
        return boardId;
    }
    private int checkBoardName(String name){
        int exist=0;
        if(name!=null&&name!=""){
            exist=getDataInt("select count(name) from board where name="+quoteParam(name));
        }
        return exist;
    }
    // socket board add
    public Board addBoardSocket(String name,long hardwareObj,String boardUniqueId){
        Board newBoard=new Board();
        Map<String, String> errors = new HashMap<>();
        int nameExi=checkBoardName(name);
        if(nameExi>0) errors.put("name", "board with the name "+name+" exists");
        newBoard.setName(name);
        newBoard.setSocket(true);
        Hardware hard=hardwareService.getBoard(hardwareObj);
        if (hard != null) {
            long id=hard.getId();
            newBoard.setHardware(hard);
        }else errors.put("hardwareId", "Hardware does not exists");
        // in development environemnt
        if(boardUniqueId!=""&&boardUniqueId!=null){
            Board exist=boardRepo.findBoardByBoardId(boardUniqueId);
            if(exist!=null){
                errors.put("boardId", "board with that unique ID has been created");
            }
            newBoard.setBoardId(boardUniqueId);
        }
        if(!errors.isEmpty()) throw new ValidationException(errors,null);
        Board save=boardRepo.save(newBoard);
        if(boardUniqueId==""){
            long id=save.getId();
            save.setBoardId(genereateBoardId(id));
            save=boardRepo.save(save);
        }
        return save;
    }
 
    public Board updateBoardObject(Board entry){
        return boardRepo.save(entry);
    }
   
    // occasional routine check
    @Transactional
    public DeviceCheck routineCheck(long id,int ram,String ip){
        DeviceCheck check=null;
        Board boardExist=boardRepo.findById(id).get();
        if(boardExist!=null){
            Instant dt=Instant.now();
            boardExist.setLastConnectDateTime(dt);
            
            boardExist.setRamUsage(ram);
            if(boardExist.getIp()!=ip&&ip!="") boardExist.setIp(ip);
            check=createDeviceCheck(boardExist);
            
            List <BoardTask> taskLists=new ArrayList<>();
            List <BoardTask> scheduledTasks=taskService.getNextTasks(boardExist.getId());
            if(scheduledTasks.size()>0)taskLists.addAll(scheduledTasks);

            if(taskLists.size()>0&&taskLists.size()<50){
                check.setTasks(taskLists);
            }else
            {
                // open websocket or message carrier to process startup commands

            }
            boardRepo.save(boardExist);
        }
        return check;
    }
    // when board first powered on
    @Transactional
    public DeviceCheck startup(BoardRegister register,String ip,int ram,String ssid,String macAddress){
        DeviceCheck check=null;
        String boardId=register.getBoardId().trim();
        Board exist=boardRepo.findBoardByBoardId(boardId);
        if(exist!=null){
            Instant dt=Instant.now();
            exist.setLastConnectDateTime(dt);
            taskService.purgeOldTasks(exist.getId());
            // verify password
            check=createDeviceCheck(exist);
            if(exist.getIp()!=ip) exist.setIp(ip);
            exist.setRamUsage(ram);
            
            // activate device to register
            if(!exist.getActivated()){
                exist.setActivated(true);
            }
            executeQuery("delete from board_queue where board="+exist.getId());
            // check if there are any startup tasks. if so add startup tasks to the scheduler for the board to process
            if(exist.getDevice().size()>0&&exist.getDevice()!=null){
                String devicesId=Arrays.toString(deviceService.getDevicesById(exist.getId())).replace("[","").replace("]","");
                int startUpCount=getDataInt("select count(id) from schedule where startup=true and device_id in ("+quoteParam(devicesId)+")");
                if(startUpCount>0){
                    check.setScheduleAvaliable(true);
                }
            }
            scheduleService.startStartupSchedule(exist);
            Board update=boardRepo.save(exist);

            Command com=commandService.getCommandByCommand("httprequestconnection", "schedule", true);
            List <BoardTask> taskLists=new ArrayList<>();
            List <BoardTask> scheduledTasks=taskService.getNextTasks(exist.getId());
            if(scheduledTasks.size()>0)taskLists.addAll(scheduledTasks);
            // add htp request connection command
            if(com!=null&&!exist.getDevMode()){
                BoardTask boTsk=com.getBoardCommand();
                boTsk.initTaskId(update.getId());
                boTsk.setDelayInterval(60000);
                boTsk.setRunTarget(0);
                taskLists.add(boTsk);
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
        newCheck.setBoardId(board.getBoardId());
        newCheck.setId(board.getId());
        newCheck.setRoutineCheck(board.getPeriodicCheck());
        newCheck.setCloseConnection(120000);
        return newCheck;
    }
    private BoardLogin createBoardLogin(Board board){
        BoardLogin login=new BoardLogin();
        login.setBoardId(board.getBoardId());
        login.setId(board.getId());
        login.setDevMode(board.getDevMode());
        login.setDevServerUrl(board.getDevServerUrl());
        login.setDevWsUrl(board.getDevWsUrl());
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
        Board rec=boardRepo.findById(id).get();
        HashMap<String, String> errors = new HashMap<>();
        if(rec!=null){
            int nameExi=getDataInt("select count(name) from board where name="+quoteParam(obj.getName())+" and id!="+id);
            if(nameExi>0){
                errors.put("name", "Board name already exists");
            }
            rec.setName(obj.getName());
            rec.setDevMode(obj.getDevMode());
            if(rec.getDevMode()){
                boolean emptyValid=true;
                // validate dev mode url and ws host
                if(obj.getDevServerUrl().equals("")) {
                    errors.put("devServerUrl", "Development server url cannot be blank");
                    emptyValid=false;
                }
                if(obj.getDevWsUrl().equals("")) {
                    errors.put("devWsUrl", "Development WebSocket url cannot be blank");
                    emptyValid=false;
                }
                if(emptyValid){
                    rec.setDevServerUrl(obj.getDevServerUrl().trim());
                    rec.setDevWsUrl(obj.getDevWsUrl().trim());
                    if(!obj.getDevServerUrl().startsWith("http://")&&!obj.getDevServerUrl().startsWith("https://")){
                        errors.put("devServerUrl", "Development server url must start with http:// or https://");
                    }
                    if(!obj.getDevWsUrl().startsWith("ws://")&&!obj.getDevWsUrl().startsWith("wss://")){
                        errors.put("devWsUrl", "Development WebSocket url must start with ws:// or wss://");
                    }
                }
            }
            rec.setRestartTimeout(obj.getRestartTimeout());
            if(!errors.isEmpty()) throw new ValidationException(errors,null);
            boardRepo.save(rec);
        }
        return rec;
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
