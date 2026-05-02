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
import com.scheduler.app.backend.Messaging.Board.Models.BoardRegister;
import com.scheduler.app.backend.Messaging.Board.Models.DeviceCheck;
import com.scheduler.app.backend.Messaging.Models.BoardTask;
import com.scheduler.app.backend.aREST.Models.Board;
import com.scheduler.app.backend.aREST.Repo.BoardRepo;


@Service
@Transactional
public class BoardService extends Base {
    
    private final BoardRepo board;
    public final HardwareService hardwareService;
    private final DeviceService deviceService;
    public final CommandService commandService;
    public final ScheduleService scheduleService;
    public final TaskService taskService;
    public final BoardQueueService boardQueueService;


    public BoardService(BoardRepo board, DeviceService deviceService,HardwareService hardwareService, CommandService commandService, ScheduleService scheduleService, TaskService taskService, BoardQueueService boardQueueService) {
        this.board = board;
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
    // socket board add
    public Board addBoardSocket(String name,long hardwareObj,String boardUniqueId){
        Board newBoard=new Board();
        Map<String, String> errors = new HashMap<>();
        int nameExi=getDataInt("select count(name) from board where name="+quoteParam(name));
        if(nameExi>0) errors.put("name", "board with the name "+name+" exists");
        newBoard.setName(name);
        newBoard.setSocket(true);
        Hardware hard=hardwareService.getBoard(hardwareObj);
        if (hard != null) {
            long id=hard.getId();
            newBoard.setHardware(hard);
        }else errors.put("hardwareId", "Hardware does not exists");
        if(boardUniqueId!=null&&boardUniqueId!=""){
            Board exist=board.findBoardByBoardId(boardUniqueId);
            if(exist!=null){
                errors.put("boardUniqueId", "board with that unique ID has been created");
            }
            newBoard.setBoardId(boardUniqueId);
        }
        if(!errors.isEmpty()) throw new ValidationException(errors);
        Board save=board.save(newBoard);
        if(boardUniqueId==null||boardUniqueId==""){
            long id=save.getId();
            save.setBoardId(genereateBoardId(id));
            save=board.save(save);
        }
        return save;
    }
    public Object updateBoardArestCommand(Board boardRec){
        return board.save(boardRec);
    }
    public Board updateBoardObject(Board entry){
        return board.save(entry);
    }
    public Board addBoard(Board entry){
        return board.save(entry);
    }

    
    public DeviceCheck routineCheck(long id,int ram,String ip){
        DeviceCheck check=null;
        Board boardExist=board.findById(id).get();
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
            board.save(boardExist);
        }
        return check;
    }
    public DeviceCheck startup(BoardRegister register,String ip,int ram,String ssid,String macAddress){
        DeviceCheck check=null;
        String boardId=register.getBoardId().trim();
        Board exist=board.findBoardByBoardId(boardId);
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
            Board update=board.save(exist);

            Command com=commandService.getCommandByCommand("httprequestconnection", "schedule", true);
            Command command=commandService.getCommandByCommand("wsconnectopen", "schedule",true);
            List <BoardTask> taskLists=new ArrayList<>();
            List <BoardTask> scheduledTasks=taskService.getNextTasks(exist.getId());
            if(scheduledTasks.size()>0)taskLists.addAll(scheduledTasks);
            // add htp request connection command
            if(com!=null){
                BoardTask boTsk=com.getBoardCommand();
                boTsk.initTaskId(update.getId());
                boTsk.setDelayInterval(60000);
                boTsk.setRunTarget(0);
                taskLists.add(boTsk);
                boardQueueService.addToQueueBoardTask(boTsk, update, null);
            }
            if(command!=null){
                BoardTask routinewsConn=command.getBoardCommand();
                routinewsConn.setDelayInterval(100);
                routinewsConn.setRunTarget(1);
                routinewsConn.setParam("startup");
                //taskLists.add(routinewsConn);
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
        newCheck.setDevMode(board.getDevMode());
        newCheck.setRoutineCheck(board.getPeriodicCheck());
        newCheck.setCloseConnection(120000);
        return newCheck;
    }
    
    // save ws session id into database for use
    public Board setWsConnection(long id,String sessionId,int ram,boolean updateLastConnect){
        Board boardRec=null;
        Optional<Board> findBoard=board.findById(id);
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
            
            boardRec=board.save(boardRec);
        }
        return boardRec;
    }

    public Board updateBoard(Board obj,long id){
        Board rec=board.findById(id).get();
        if(rec!=null){
            rec.setName(obj.getName());
            rec.setDevMode(obj.getDevMode());
            rec.setRestartTimeout(obj.getRestartTimeout());
            board.save(rec);
        }
        return rec;
    }
    
    public List<Board> getBoards(){
        List <Board> list=board.findAll();
        return list;
    }
    public Board getBoard(long id){
        return board.findById(id).get();
    }
    public Board getBoardById(long id){
        return board.findById(id).get();
    }
    public String deleteBoard(long id){
        String output="Does not exist";
        if(board.existsById(id)){
            Board item=board.getReferenceById(id);
            board.deleteById(id);
            output="board remove "+item.getName();
        }
        return output;
    }
    public void deleteAllBoards(){
        board.deleteAll();
        deviceService.deleteAllBoard();
    }
   
    public Optional<Board> findBoard(long id){
        return board.findById(id);
    }
    public Hardware getBoardHardwareId(String id){
        Hardware hardware=board.findBoardByBoardId(id).getHardware();
        //List<HardwarePins> pins=hardware.getPins().stream().sorted(Comparator.comparing(HardwarePins::getBoardPin)).collect(Collectors.toList());
        //hardware.setPins(pins);
        return hardware;
    }
    public Board getBoardByBoardId(String id){
        return board.findBoardByBoardId(id);
    }
    
}
