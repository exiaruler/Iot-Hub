package com.scheduler.app.backend.aREST.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.scheduler.Base.Base;
import com.scheduler.Base.JsonObject.JsonObject;
import com.scheduler.app.backend.Hardware.Models.Hardware;
import com.scheduler.app.backend.Hardware.Repo.HardwareRepo;
import com.scheduler.app.backend.Hardware.Service.HardwareService;
import com.scheduler.app.backend.Messaging.Board.Models.BoardRegister;
import com.scheduler.app.backend.Messaging.Board.Models.DeviceCheck;
import com.scheduler.app.backend.aREST.ArestV2Frame;
import com.scheduler.app.backend.aREST.Models.Board;
import com.scheduler.app.backend.aREST.Models.Device;
import com.scheduler.app.backend.aREST.Repo.BoardRepo;


@Service
@Transactional
public class BoardService extends Base {
    
    private final BoardRepo board;
    public final HardwareService hardwareService;
    private final DeviceService deviceService;
    public ArestV2Frame arest=new ArestV2Frame();

    public BoardService(BoardRepo board, DeviceService deviceService,HardwareService hardwareService) {
        this.board = board;
        this.hardwareService = hardwareService;
        this.deviceService = deviceService;
    }
   

    public Board addBoardTest(Board newBoard){
        return board.save(newBoard);
    }
    public Board addBoardManual(String name,String ip,boolean arest,boolean status){
        Board newBoard=new Board();
        newBoard.setName(name);
        newBoard.setIp(ip);
        newBoard.setArest(arest);
        newBoard.setStatus(status);
        // generate board Id
        List <Board> boards=board.findAll();
        if(!boards.isEmpty()){
            Board lastBoard=boards.get(boards.size()-1);
            String boardId=generateRandString(4)+lastBoard.getId();
            newBoard.setBoardId(boardId);
        }else{
            String boardId=generateRandString(4)+1;
            newBoard.setBoardId(boardId);
        }
        return board.save(newBoard);
    }
    private String genereateBoardId(){
        int totalBoards=board.findAll().size();
        long lastIdPo=board.findAll().get(totalBoards-1).getId()+1;
        if(totalBoards==0){
            totalBoards=1;
        }else totalBoards++;
        String boardId=generateRandString(4)+lastIdPo;
        return boardId;
    }
    // socket board add
    public Board addBoardSocket(String name,Hardware hardwareObj){
        Board newBoard=new Board();
        newBoard.setName(name);
        newBoard.setBoardId(genereateBoardId());
        newBoard.setSocket(true);
        if (hardwareObj != null) {
            long id=hardwareObj.getId();
            Hardware existingHardware = hardwareService.getBoard(id);
            newBoard.setHardware(existingHardware);
        }
        Board save=board.save(newBoard);
        return save;
    }
    public Object updateBoardArestCommand(Board boardRec){
        return board.save(boardRec);
    }
    public Board addBoard(Board entry){
        return board.save(entry);
    }
    public ArrayList<Board> scanNewBoards(){
        ArrayList<Board> addedList=new ArrayList<Board>();
        String ipAddress="192.168.1.";
        for(int i=0; i<255; i++){
            String ipTest=ipAddress+i;
            String rawJson=httpUtil.request(ipTest);
            if(rawJson!=""){
                Board add=addBoardIp(ipTest,rawJson);
                if(add!=null) addedList.add(add);
            }
        }
        return addedList;
    }
    public DeviceCheck routineCheck(long id,int ram,String ip){
        DeviceCheck check=null;
        Board boardExist=board.getReferenceById(id);
        if(boardExist!=null){
            /* 
            LocalDate date=LocalDate.now();
            LocalTime time=LocalTime.now();
            LocalDateTime dt=LocalDateTime.now();
            boardExist.setLastConnectDate(date);
            boardExist.setLastConnectTime(time);
            boardExist.setLastConnectDateTime(dt);
            */
            boardExist.setRamUsage(ram);
            if(boardExist.getIp()!=ip&&ip!="") boardExist.setIp(ip);
            check=createDeviceCheck(boardExist);
            board.save(boardExist);
        }
        return check;
    }
    public DeviceCheck startup(BoardRegister register,String ip,int ram){
        DeviceCheck check=null;
        Board exist=board.findBoardByBoardId(register.getBoardId());
        if(exist!=null){
            // verify password
            check=createDeviceCheck(exist);
            if(exist.getIp()!=ip) exist.setIp(ip);
            exist.setRamUsage(ram);
            // activate device to register
            if(!exist.getActivated()){
                exist.setActivated(true);
            }
            // check if there are any startup tasks. if so add startup tasks to the scheduler for the board to process
            if(exist.getDevice().size()>0&&exist.getDevice()!=null){
                String devicesId=Arrays.toString(deviceService.getDevicesById(exist.getId())).replace("[","").replace("]","");
                int startUpCount=getDataInt("SELECT count(id) FROM scheduler.schedule where startup=true and device_id in ("+quoteParam(devicesId)+")");
                if(startUpCount>0){
                    check.setScheduleAvaliable(true);
                }
            }
            board.save(exist);
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
    
    public Board addBoardIp(String rawJson,String ip){
        Board add=null;
        if(arduinoboardCheck(rawJson)){
            Board newBoard=new Board();
            JsonObject json=jsonobj.jsonToObject(rawJson);
            if(arest.testBoardFrameWork(json,ip)){
                String boardId=json.findKeyValue("id");
                Board existingBoard=board.findBoardByBoardId(boardId);
                if(existingBoard!=null){
                    newBoard=existingBoard;
                }else{
                    newBoard.setBoardId(boardId);
                } 
                newBoard.setName(json.findKeyValue("name"));
                newBoard.setArest(true);
                newBoard.setIp(ip);
                List<Device> deviceList=deviceService.addDeviceFromScan(newBoard,ip,json);
                newBoard.setDevice(deviceList);
                Board save=addBoard(newBoard);
                add=save;
            }else
            // arest Command
            {
                String boardId=json.findKeyValue("id");
                Board existingBoard=board.findBoardByBoardId(boardId);
                if(existingBoard!=null){
                    newBoard=existingBoard;
                }else{
                    newBoard.setBoardId(boardId);
                } 
                newBoard.setName(json.findKeyValue("name"));
                newBoard.setIp(ip);
                newBoard.setArestCommand(true);
                Board save=addBoard(newBoard);
                add=save;
            }
            }
        return add;
    }
    public Board addBoardByIp(String ip){
        Board board=null;
        if(ip!=""){
            String jsonRaw=httpUtil.request(ip);
            if(jsonRaw!=""){
                board=addBoardIp(jsonRaw,ip);
            }
        }
        return board;
    }
    // save ws session id into database for use
    public Board setWsConnection(long id,String sessionId,int ram,boolean updateLastConnect){
        Board boardRec=board.findById(id).get();
        if(boardRec!=null){
            boardRec.setWebsocketId(sessionId);
            LocalDate date=LocalDate.now();
            LocalTime time=LocalTime.now();
            LocalDateTime dt=LocalDateTime.now();
            if(updateLastConnect){
                boardRec.setLastConnectDate(date);
                boardRec.setLastConnectTime(time);
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
            rec=obj;
            board.save(rec);
        }
        return rec;
    }
    
    public List<Board> getBoards(){
        List <Board> list=board.findAll();
        return list;
    }
    public Board getBoard(long id){
        return board.getReferenceById(id);
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
        return board.findBoardByBoardId(id).getHardware();
    }
    public Board getBoardByBoardId(String id){
        return board.findBoardByBoardId(id);
    }
    
}
