package com.scheduler.app.backend.Messaging.Board;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
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
import com.scheduler.app.backend.Messaging.Models.BoardTask;
import com.scheduler.app.backend.Messaging.Models.BoardVariable;
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
    // commands not to log
    private String[] banComs={"reset","resetboard"};

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
        BoardSession boarSess=new BoardSession();
        Instant dt=Instant.now();
        String action="";
        String boardIdStr="";
        String query = session.getUri().getQuery();
        // multiple query
        if(query!=null&&query.contains("&")){
            String[] queries=query.split("&");
            boardIdStr=queries[0].split("=")[1];
            action=queries[1].split("=")[1];
        }else
        {
            // retrieve action
            if(query!=null){
                action=query.split("=")[1];
            }
            var headers = session.getHandshakeHeaders();
            boardIdStr=headers.getFirst("board");
        }
        if(boardIdStr!=""){
            sessions.put(session.getId(),session);
            long boardId=Long.parseLong(boardIdStr);
            boarSess.setSession(session);
            boarSess.setSessionId(sessionId);
            boarSess.setBoardId(boardId);
            //Board board=boardService.setWsConnection(boardId,sessionId,0,false);
            Board board=null;
            try {
                board=boardService.getBoard(boardId);
            } catch (Exception e) {
                // TODO: handle exception
            }
            if(board!=null){
                if(action.equals("check")&&board.getRestartTimeout()){
                    // if time out reset enabled check if it meets the requirement
                        Instant lastConnect = board.getLastConnectDateTime();
                        long elapsedMs = lastConnect == null ? Long.MAX_VALUE : Duration.between(lastConnect, dt).toMillis();
                        // if requirement is met add restart command to scheduler
                        if(elapsedMs > board.getTimeout()){
                            board.setLastConnectDateTime(Instant.now());
                            boardService.updateBoardObject(board);
                            //taskService.runCommand(board.getBoardId(),"reset","action", true, true);
                        }
                    
                }
                // send status check first before saving the session id
                String status=uponConnect(board);
                // send status message
                if(status!=""){
                    session.sendMessage(new TextMessage(status));
                }
                // save session id
                board=boardService.setWsConnection(boardId,sessionId,0,true);
                // board startup/power up
                if(action.equals("startup")){
                    // set a state in the board to stop processing message when board starting up
                    taskService.purgeOldTasks(boardId);
                    // system route
                    Command command=commandService.getCommandByCommand("wsconnectopen", "schedule",true);
                    BoardTask routinewsConn=command.getBoardCommand();
                    routinewsConn.initTaskId(boardId);
                    routinewsConn.setDelayInterval(board.getPeriodicCheck());
                    routinewsConn.setVariable(new BoardVariable());
                    routinewsConn.setParam("check");
                    routinewsConn=pruneBoardTask(routinewsConn);
                    routinewsConn.setRunTarget(0);
                    String routineCheckMsg = objectToJson(routinewsConn);
                    if(routineCheckMsg!=""){
                        session.sendMessage(new TextMessage(routineCheckMsg));
                        sentMessages.put(boardId,routinewsConn);
                        // find startup functions
                        boolean esp32Check=board.getHardware().getBoardName().equals("ESP32");
                        if(esp32Check){
                            session.close();
                        }else
                        {
                            boolean startupTskExist=scheduleService.startStartupSchedule(board);
                            if(!startupTskExist){
                                session.close();
                            }
                        }
                    }
                }
                // routine check
                if(action.equals("check")){
                    // check for upcoming schedule commands or user active on the board
                    Instant dtcurr=Instant.now();
                    if(!taskService.checkNextTask(dtcurr,boardId,null)){
                        session.close();
                        
                    }
                }
                // connect
                if(action.equals("connect")){
                    // set a state in the board to start processing message when board connected
                }
            }
            else
            {
                // board does not exist send board reset command to wipe board configuration
                Command restCommand=commandService.getCommandByCommand("resetboard", "action",true);
                BoardTask reset=restCommand.getBoardCommand();
                reset.setVariable(new BoardVariable());
                String resetMsg = objectToJson(reset);
                if(resetMsg!=""){
                    session.sendMessage(new TextMessage(resetMsg));
                }
                session.close();
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
        String boardIdStr="";
        String query = session.getUri().getQuery();
        // multiple query
        if(query!=null&&query.contains("&")){
            String[] queries=query.split("&");
            boardIdStr=queries[0].split("=")[1];
        }else
        {
            boardIdStr=headers.getFirst("board");
        }
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
    public SentToClientResponse sendToClient(String sessionId,BoardTask task,long boardId,Task taskObj,Device device,Route route,Mode mode,String comMessage) throws IOException {
        SentToClientResponse resp=new SentToClientResponse();
        WebSocketSession session = sessions.get(sessionId);
        if (session != null && session.isOpen()) {
            if(!sentMessages.containsKey(boardId)){
                String message="";
                if(task!=null){
                    task.initTaskId(boardId);
                    message=objectToJson(task);
                }else if(comMessage!=""){
                    message=comMessage;
                }
                if(message!=""){
                    boolean log=false;
                    // reset and update commands do not log
                    if(Arrays.binarySearch(banComs,task.getMethod())<0) log=true;
                    System.out.println("task sent "+taskObj.getApplication());
                    System.out.println("schedule time "+taskObj.getScheduledTime());
                    //System.out.println("schedule time formatted "+taskObj.getScheduledTime().getHour()+":"+taskObj.getScheduledTime().getMinute()+":"+taskObj.getScheduledTime().getSecond());
                    session.sendMessage(new TextMessage(message));
                    if(log)sentMessages.put(boardId, task);
                    List<Task> nextTasks=taskService.taskComplete(taskObj, device, route, mode);
                    resp.setSent(true);
                    resp.setNextTasks(nextTasks);
                    // log message sent
                    if(log){

                    }
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
