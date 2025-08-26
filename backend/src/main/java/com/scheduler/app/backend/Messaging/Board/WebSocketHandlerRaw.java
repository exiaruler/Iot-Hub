package com.scheduler.app.backend.Messaging.Board;

import java.io.IOException;
import java.time.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.*;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheduler.app.backend.Command.Models.Command;
import com.scheduler.app.backend.Command.Service.CommandService;
import com.scheduler.app.backend.Messaging.Board.Models.BoardInput;
import com.scheduler.app.backend.Messaging.Board.Models.BoardInputTask;
import com.scheduler.app.backend.Messaging.Models.*;
import com.scheduler.app.backend.aREST.Models.Board;
import com.scheduler.app.backend.aREST.Models.Device;
import com.scheduler.app.backend.aREST.Models.Mode;
import com.scheduler.app.backend.aREST.Models.Route;
import com.scheduler.app.backend.aREST.Models.Task;
import com.scheduler.app.backend.aREST.Service.BoardService;
import com.scheduler.app.backend.aREST.Service.ScheduleService;
import com.scheduler.app.backend.aREST.Service.TaskService;

// websocket handler
@Component
public class WebSocketHandlerRaw extends TextWebSocketHandler{

    public final BoardService boardService;
    public final CommandService commandService;
    public final ScheduleService scheduleService;
    public final TaskService taskService;
    // board Id and boardTask
    public static HashMap<Long,BoardTask> sentMessages=new HashMap<>();
    //public SchedulerTask scheduler;
    private static final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public WebSocketHandlerRaw(BoardService boardService, CommandService commandService, ScheduleService scheduleService, TaskService taskService) {
        this.boardService = boardService;
        this.commandService = commandService;
        this.scheduleService = scheduleService;
        this.taskService = taskService;
    }
    public List<String> getSessions(){
        Set<String> sessionsSet=sessions.keySet();
        List<String> toList=new ArrayList<>(sessionsSet);
        return toList;
    }
    public WebSocketSession getSession(String sessionId){
        WebSocketSession sess=sessions.get(sessionId);
        return sess;
    }
    // set arrays to null of task
    private BoardTask pruneBoardTask(BoardTask task){
        return task;
    }
    public long taskIdGenerate(long boardId){
        LocalDate date=LocalDate.now();
        LocalTime time=LocalTime.now();
        String strId=""+boardId+date.getDayOfMonth()+date.getMonthValue()+date.getYear()+time.getNano();
        long taskId=Long.parseLong(strId);
        System.out.println("Task Id "+taskId);
        return taskId;
    }
    public String uponConnect(Board board) throws JsonProcessingException{
        String jsonString="";
        Command command=commandService.getCommandByCommand("status", "action",true);
        BoardTask comTask=command.getBoardCommand();
        comTask.initTaskId(board.getId());
        comTask=pruneBoardTask(comTask);
        jsonString=objectToJson(comTask);
        sentMessages.put(board.getId(),comTask);
        return jsonString;
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        String sessionId=session.getId();
        LocalDate date=LocalDate.now();
        LocalTime time=LocalTime.now();
        LocalDateTime dt=LocalDateTime.now();
        String action="";
        String query = session.getUri().getQuery();
        // retrieve action
        if(query!=null){
            action=query.split("=")[1];
            System.out.println(action);
        }
        var headers = session.getHandshakeHeaders();
        String boardIdStr=headers.getFirst("board");
        if(boardIdStr!=""){
            sessions.put(session.getId(),session);
            long boardId=Long.parseLong(boardIdStr);
            System.out.println("board ID "+boardId);
            System.out.println(dt);
            Board board=boardService.setWsConnection(boardId,sessionId,0,false);
            // if time out reset enabled check if it meets the requirement
            if(board.getRestartTimeout()){
                Duration timeDura=Duration.between(time, board.getLastConnectTime());
                Duration dateDura=Duration.between(dt,board.getLastConnectDateTime());
                long sec=timeDura.getNano();
                long dtSec=dateDura.getNano();
                // if requirement is met send restart command and immediately disconnect
                if(sec>board.getTimeout()&&dtSec>board.getTimeout()){
                    Command resetCom=commandService.getCommandByCommand("reset", "action",true);
                    BoardTask restTsk=resetCom.getBoardCommand();
                    restTsk.initTaskId(boardId);
                    String restMsg=objectToJson(restTsk);
                    session.sendMessage(new TextMessage(restMsg));
                    session.close();
                }
            }
            board=boardService.setWsConnection(boardId,sessionId,0,true);
            String status=uponConnect(board);
            // send status message
            if(status!=""){
                session.sendMessage(new TextMessage(status));
            }
            // board startup/power up
            if(action.equals("startup")){
                taskService.purgeOldTasks(boardId);
                // system route
                Command command=commandService.getCommandByCommand("wsconnectopen", "schedule",true);
                BoardTask routinewsConn=command.getBoardCommand();
                routinewsConn.initTaskId(boardId);
                routinewsConn.setDelayInterval(board.getPeriodicCheck());
                routinewsConn.setVariable(new BoardVariable());
                routinewsConn=pruneBoardTask(routinewsConn);
                String routineCheckMsg = objectToJson(routinewsConn);
                if(routineCheckMsg!=""){
                    session.sendMessage(new TextMessage(routineCheckMsg));
                    sentMessages.put(boardId,routinewsConn);
                    // find startup functions
                    boolean startupTskExist=scheduleService.startStartupSchedule(board);
                    if(!startupTskExist){
                        session.close();
                    }
                }
            }
            // routine check
            if(action.equals("check")){
                // check for upcoming schedule commands or user active on the board
                Instant dtcurr=Instant.now();
                if(!taskService.checkNextTask(dtcurr,boardId,0)){
                    session.close();
                }
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("Received: " + payload);
        if(payload!=""){
            BoardInput boardIn=stringToObject(payload);
            BoardInputTask task=boardIn.getTask();
            String ip=boardIn.getIp();
            long boardId=boardIn.getBoard();
            int ram=boardIn.getRamSpace();
            boardService.routineCheck(boardId,ram,ip);
            if(task!=null){
                BoardTask receiTask=sentMessages.get(boardId);
                System.out.println(task.getTaskId());
                if(receiTask!=null){
                    sentMessages.remove(boardId);
                    // log it to complete
                }
            }
            
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("Client disconnected: " + session.getId());
        var headers = session.getHandshakeHeaders();
        String boardIdStr=headers.getFirst("board");
        if(boardIdStr!=""){
            sessions.remove(session.getId());
            long boardId=Long.parseLong(boardIdStr);
            sentMessages.remove(boardId);
            boardService.setWsConnection(boardId,"",0,true);
        }
    }
    // convert board task to json string
    private String objectToJson(BoardTask task) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        String json="";  
        try {
            task=pruneBoardTask(task);
            json = mapper.writeValueAsString(task);
        } catch (Exception e) {
            json="";
            // TODO: handle exception
        }
        return json;
    }
    // map board input to object
    private BoardInput stringToObject(String json) throws JsonMappingException, JsonProcessingException{
        BoardInput boardInput=null;
        ObjectMapper mapper = new ObjectMapper();
        boardInput=mapper.readValue(json,BoardInput.class);
        return boardInput;
    }
    // use in scheduler, sent message commands to board
    public SentToClientResponse sendToClient(String sessionId,BoardTask task,long boardId,Task taskObj,Device device,Route route,Mode mode) throws IOException {
        SentToClientResponse resp=new SentToClientResponse();
        WebSocketSession session = sessions.get(sessionId);
        if (session != null && session.isOpen()) {
            if(!sentMessages.containsKey(boardId)){
                task.initTaskId(boardId);
                String message=objectToJson(task);
                if(message!=""){
                    session.sendMessage(new TextMessage(message));
                    sentMessages.put(boardId, task);
                    List<Task> nextTasks=taskService.taskComplete(taskObj, device, route, mode);
                    resp.setSent(true);
                    resp.setNextTasks(nextTasks);
                    // log message sent
                    // logic to decide to close websocket
                    Instant dtcurr=Instant.now();
                    if(!taskService.checkNextTask(dtcurr,boardId,taskObj.getId())){
                        session.close();
                    }
                }else
                {
                    // log error

                }
            }

        }else if(session != null && !session.isOpen()){
            sessions.remove(session.getId());
        }
        return resp;
    }
    
}
