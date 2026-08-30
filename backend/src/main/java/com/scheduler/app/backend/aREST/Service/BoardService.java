package com.scheduler.app.backend.aREST.Service;
import java.io.InputStream;
import java.net.HttpURLConnection;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.scheduler.Base.Exception.ValidationException;
import com.scheduler.Base.Service.BaseService;
import com.scheduler.app.backend.Background.Background;
import com.scheduler.app.backend.Command.Service.CommandService;
import com.scheduler.app.backend.Firmware.Model.Firmware;
import com.scheduler.app.backend.Firmware.Service.FirmwareService;
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
    private final FirmwareService firmwareService;


    public BoardService(BoardRepo boardRepo, DeviceService deviceService,HardwareService hardwareService, CommandService commandService, ScheduleService scheduleService, TaskService taskService, BoardQueueService boardQueueService, FirmwareService firmwareService) {
        this.boardRepo = boardRepo;
        this.hardwareService = hardwareService;
        this.deviceService = deviceService;
        this.commandService = commandService;
        this.scheduleService = scheduleService;
        this.taskService = taskService;
        this.boardQueueService = boardQueueService;
        this.firmwareService = firmwareService;
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
        if(persistedBoard!=null){
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
            if(persistedBoard.getMillis()>entity.getMillis()) entity.setMillis(persistedBoard.getMillis());
            if(persistedBoard.getHeap()>entity.getHeap()) entity.setHeap(persistedBoard.getHeap());
            entity.setBoardId(boardId.trim());
            entity.setOffline(offline);
            entity.setRestartTimeout(restartTimeout);
            entity.setDevMode(devMode);
            entity.setDevServerUrl(devMode ? devServerUrl.trim() : persistedBoard.getDevServerUrl());
            entity.setDevWsUrl(devMode ? devWsUrl.trim() : persistedBoard.getDevWsUrl());
            if(!entity.getLastLoginDateTime().equals(persistedBoard.getLastLoginDateTime()))entity.setLastLoginDateTime(entity.getLastLoginDateTime());
        }
    }
    public String boardLiveKey(long id){
        return "board-live|"+id;
    }

    @Override
    protected void afterSave(Board entity) {
        if (isBlank(entity.getBoardId())) {
            entity.setBoardId(genereateBoardId(entity.getId()));
            boardRepo.save(entity);
        }
        if(Background.globalExist("board|online|"+entity.getId())){
            Instant getNextOp=boardQueueService.getNextQueueOperation(entity.getId());
            Background.putGlobal("board-next-op|"+entity.getId(),getNextOp);
        }

    }
    @Override
    protected void afterFindById(Long id, Board entity) {
        // retrieve next queue operation for the board and set it in the entity
        Instant dt=boardQueueService.getNextQueueOperation(id);
        entity.setNextQueueOperation(dt);
        if(dt!=null) Background.putGlobal("board-next-op|"+id,entity.getNextQueueOperation());

    }
    @Override
    protected void afterDelete(Long id) {
        // TODO Auto-generated method stub
        
        Background.removeGlobal("board-next-op|"+id);
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
    private Board offlineBoard(Board board){
        board.setMillis(0);
        board.setLastLoginDateTime(null);
        board.setRamUsage(0);
        board.setHeap(0);
        return board;
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
                bo=offlineBoard(bo);
                Background.removeGlobal("board|online|"+bo.getId());
                this.save(bo);
                //taskService.deactiveTask(devIds);
            }
            System.out.println("offline board size "+offline.size());
        }
    }
   
    // occasional routine check
    @Transactional
    public DeviceCheck routineCheck(long id,int ram,String ip,int heap,long millis,long tid){
        DeviceCheck check=null;
        Board boardExist=this.findById(id);
        if(boardExist!=null){
            Background.putGlobal("http-check|"+id,"updating");
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
            boardExist.setHeap(heap);
            boardExist.setMillis(millis);
            Background.putGlobal("board-millis|"+boardExist.getId(),millis);
            boardExist.setRamUsage(ram);
            if(boardExist.getIp()!=ip&&ip!="") boardExist.setIp(ip);
            check=createDeviceCheck(boardExist);
            boardExist=this.save(boardExist);
            Background.putGlobal("http-check|"+id,"queueing");

            List <BoardTaskSerial> taskLists=new ArrayList<>();
            List <BoardTaskSerial> scheduledTasks=taskService.getNextTasks(boardExist.getId(),boardExist);
            if(scheduledTasks.size()>0)taskLists=scheduledTasks;
            //System.out.println(taskLists.size());
            check.setTasks(taskLists);
            if(tid>0){
                // delete or update board queue
                boardQueueService.updateQueue(tid, id,dt);
            }
            Background.removeGlobal("board-millis|"+boardExist.getId());
            Background.removeGlobal("http-check|"+id);
        }
        return check;
    }
    // firmware update
    public ResponseEntity<StreamingResponseBody> getUpdate(long id) {
        Firmware firmware;
        Board boardExist=this.findById(id);
        if(boardExist!=null){

            try {
                firmware = firmwareService.getUpdateVersion(boardExist.getFirmwareVersion());
            } catch (IllegalArgumentException exception) {
                return ResponseEntity.badRequest().build();
            }
            if (firmware == null) {
                return ResponseEntity.noContent().build();
            }

            HttpURLConnection connection;
            try {
                connection = firmwareService.openDownload(firmware);
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    firmwareService.closeDownload(connection);
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
                }
            } catch (Exception exception) {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
            }

            long contentLength = connection.getContentLengthLong();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            if (contentLength >= 0) {
                headers.setContentLength(contentLength);
            }
            headers.set("X-Firmware-Version", firmware.getVersion());
            StreamingResponseBody responseBody = outputStream -> {
                try (InputStream inputStream = connection.getInputStream()) {
                    inputStream.transferTo(outputStream);
                } finally {
                    connection.disconnect();
                }
            };
            
            return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }
        
    // when board first powered on
    @Transactional
    public BoardLogin startup(BoardRegister register,String ip,int ram,String ssid,String macAddress,int freeHeap,int heap,int systemTotalTask,int taskTotal,int totalQueue,long millis,String version){
        BoardLogin check=null;
        String boardId=register.getBoardId().trim();
        long boardIdLong=getDataLong("select id from board where board_id="+quoteParam(boardId));
        Board exist=this.findById(boardIdLong);
        if(exist!=null){
            Background.putGlobal("http-check|"+exist.getId(),"updating");
            // clean up globals if update occured
            Object wsIdGl=Background.getGlobal("update|board|"+exist.getId());
            if(wsIdGl!=null){
                String wsId=String.class.cast(wsIdGl);
                Background.removeGlobal("update|websocketid|"+wsId);
                Background.removeGlobal("update|board|"+exist.getId());
            }
            Instant dt=Instant.now();
            exist.setLastConnectDateTime(dt);
            exist.setLastLoginDateTime(dt);
            taskService.purgeOldTasks(exist.getId());
            Firmware firmware=firmwareService.getVersion(version);
            // add or update firmware version that board is using
            if(exist.getFirmware()==null||!exist.getFirmwareVersion().equals(version)){
                exist.setFirmware(firmware);
                exist.setFirmwareVersion(version);
            }
            // check if there a required mandatory update
            // verify password
            check=createBoardLogin(exist);
            if(exist.getIp()!=ip) exist.setIp(ip);
            exist.setRamUsage(ram);
            exist.setHeap(freeHeap);
            exist.setHeapTotal(heap);
            exist.setMillis(millis);
            Background.putGlobal("board-millis|"+exist.getId(),millis);
            if(exist.getSsid()==null||exist.getSsid().equals("")||!exist.getSsid().equals(ssid)) exist.setSsid(ssid);
            if(exist.getMacAddress()==null||exist.getMacAddress().equals("")||!exist.getMacAddress().equals(macAddress)) exist.setMacAddress(macAddress);
            // activate device to register
            if(!exist.getActivated()){
                exist.setActivatedDateTime(dt);
                exist.setActivated(true);
            }
            executeQuery("delete from board_queue where board_id="+exist.getId());
            scheduleService.startStartupSchedule(exist);
            Background.putGlobal("board|online|"+exist.getId(),Instant.now());
            Board update=save(exist);

            Background.putGlobal("http-check|"+exist.getId(),"queueing");
            BoardTask boTsk=commandService.getRequestConnection();
            List <BoardTaskSerial> taskLists=new ArrayList<>();
            List <BoardTaskSerial> scheduledTasks=taskService.getNextTasks(update.getId(),update);
            if(scheduledTasks.size()>0)taskLists.addAll(scheduledTasks);
            // add htp request connection command
            if(boTsk!=null&&!update.getDevMode()){
                boTsk.initTaskId(update.getId());
                boTsk.setDelayInterval(60000);
                boTsk.setRunTarget(0);
                taskLists.add(new BoardTaskSerial(boTsk));
                boardQueueService.addToQueueBoardTask(boTsk, update, null,dt);
            }
            if(taskLists.size()>0&&taskLists.size()<50){
                check.setTasks(taskLists);
            }else
            {
                // open websocket or message carrier to process startup commands

            }
            Background.removeGlobal("board-millis|"+exist.getId());
            Background.removeGlobal("http-check|"+exist.getId());
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
        Board findBoard=this.findById(id);
        if(findBoard!=null){
            boardRec=findBoard;
            boardRec.setWebsocketId(sessionId);
            Instant dt=Instant.now();
            if(updateLastConnect){
                boardRec.setLastConnectDateTime(dt);
            }
            if(ram>0){
                boardRec.setRamUsage(ram);
            }
            
            boardRec=save(boardRec);
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
        long idLong=getDataLong("select id from board where board_id="+quoteParam(id));
        if( idLong<0) return null;
        return this.findById(idLong);
    }
    
}
