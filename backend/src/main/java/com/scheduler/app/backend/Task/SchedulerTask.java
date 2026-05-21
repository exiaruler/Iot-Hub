package com.scheduler.app.backend.Task;
import java.util.ArrayList;
import java.util.List;
import com.scheduler.app.backend.Task.Model.CompletedTask;
import com.scheduler.app.backend.Task.Thread.HttpSchedule;
import com.scheduler.app.backend.Task.Thread.CheckRun;
import com.scheduler.app.backend.aREST.Models.*;
import com.scheduler.app.backend.aREST.Service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheduler.app.backend.Command.Models.Command;
import com.scheduler.app.backend.Command.Service.CommandService;
import com.scheduler.app.backend.Messaging.Board.SentToClientResponse;
import com.scheduler.app.backend.Messaging.Board.WebSocketHandlerRaw;
import com.scheduler.app.backend.Messaging.Models.BoardPin;
import com.scheduler.app.backend.Messaging.Models.BoardTask;
import com.scheduler.app.backend.Messaging.Models.InputCurrent;
import com.scheduler.app.backend.Messaging.Models.OutputCurrent;
import com.scheduler.app.backend.Messaging.Service.BoardTaskService;
// scheduler
@Component
public class SchedulerTask{
    // setting for queue to start
    public static boolean running=true;
    // start the queue when server starts or reboots
    private boolean start=false;
    // running queue active
    public static boolean queueRun=false;
    @Autowired
    private BoardService boardService;
    @Autowired
    public TaskService taskService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private RoutesService routeService;
    @Autowired
    private ParameterService parameterService;
    @Autowired
    public WebSocketHandlerRaw websocketHandler;
    @Autowired
    public BoardTaskService boardTaskService;
    @Autowired
    public CommandService commandService;
    
    // task queue
    private static List<Task> queue=new ArrayList<Task>();
    // task running 
    private static List<Task> runningQueue=new ArrayList<Task>();
    // task finished processing 
    private static List<CompletedTask> completeTaskQueue=new ArrayList<CompletedTask>();
    
    @Scheduled(fixedRate = 1000)
    public void runSche(){
        if(queue.isEmpty()&&!start){
            boolean active=taskService.addToScheduler();
            start=true;
        }
        if(running&&!queue.isEmpty()){
            start=true;
            Instant dt=Instant.now();
            System.out.println(dt);
            System.out.println("number of task that are pending in the queue "+queue.size());
            System.out.println("number of task that are running "+runningQueue.size());
            List<Task> scheduled=queue.stream().filter(rec->rec.getScheduledTime().equals(dt)||rec.getScheduledTime().isBefore(dt)).toList();
            if(scheduled.size()>0){
                scheduled.stream().forEach(tas->processTask(tas));
            }
        }
    }
    // get board tasks at the present by boardId
    public List<Task> queryQueueNow(long boardId){
        Instant dt=Instant.now();
        List<Task> filterList=queue.stream().filter(rec->rec.getScheduledTime().equals(dt)||rec.getScheduledTime().isBefore(dt)&&rec.getBoard()==boardId||rec.getSystemTask()&&rec.getBoard()==boardId).toList();
        queue.removeIf(rec ->(rec.getScheduledTime().equals(dt)|| rec.getScheduledTime().isBefore(dt))&& rec.getBoard() == boardId||rec.getSystemTask()&&rec.getBoard()==boardId);
        return filterList;
    }
    public BoardTask boardTaskToObject(String json){
        BoardTask tsk=null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            tsk=mapper.readValue(json,BoardTask.class);
        } catch (Exception e) {
            return null;
        }
        return tsk;
    }
    public void processTask(Task task){
        String [] paramsArr={};
                    // add task to running queue if it passes check else let it stick in the queue 
                    if(!checkTaskRunning(task)&&!task.getHttpTask()){
                        Board board=null;
                        Device device=null;
                        Route route=null;
                        Mode mode=null;
                        try {
                            board=boardService.getBoardById(task.getBoard());
                            device=deviceService.getDevice(task.getDeviceId());
                            route=routeService.getRouteById(task.getRouteId());
                            mode=routeService.getMode(task.getModeId());
                        } catch (Exception e) {
                            
                        }
                        paramsArr=parameterService.getParamsArray(task.getModeId());
                        if(board.getSocket()){
                            boolean removed=queue.remove(task);
                            if(removed){
                                try {
                                    sendSocketMessage(task, board,device, route, mode);
                                } catch (InstantiationException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    // run http task
                    if(task.getHttpTask()){
                        HttpSchedule thread=new HttpSchedule(task,null,null,null,paramsArr);
                        thread.start();
                        queue.remove(task);
                    }
    }
    /* 
    @Scheduled(fixedRate = 60000)
    public void checkRunningQueue(){
        if(queueRun){
            Instant dt=Instant.now();
            for(int i=0; i<runningQueue.size(); i++){
                Task task=queue.get(i);
                Instant taskDt=task.getScheduledTime();
                if(dt.isAfter(taskDt)&&checkTaskRunning(task)){
                    
                    if(task.getRetry()!=task.getSchedule().getRetries()){
                        runningQueue.remove(i);
                        int tries=task.getRetry();
                        tries++;
                        task.setRetry(tries);
                        queue.add(task);
                        if(runningQueue.size()<0){
                            queueRun=false;
                        }
                    }
                    
                }
            }
        }
    }
    */
    // loop through array to update database
    /* 
    @Scheduled(fixedRate = 100)
    public void runComplete(){
        if(!completeTaskQueue.isEmpty()){
            System.out.println("Number of completed task in queue "+completeTaskQueue.size());
            for(int i=0; i<completeTaskQueue.size(); i++){
                CompletedTask task=completeTaskQueue.get(i);
                // if task involved a servo or motor remove from running task
                if(task.getTask().isMotor()&&task.getDevice().getBoard().getSocket()){
                    removeRunningTask(task.getTask());
                }
                //updateQueue(task.getTask());
                taskService.modifyTaskFromScheduler(task.getTask(),task);
                completeTaskQueue.remove(i);
            }
        }
    }
    */
    // check if the device or board is running to avoid clashing or strain on power
    public boolean checkTaskRunning(Task task){
        boolean result=false;
        queue.stream().filter(rec->rec.getId()==task.getId()).findFirst();
        List<Task> query=runningQueue.stream().filter(rec->rec.getBoard()==task.getBoard()&&rec.getDeviceId()==task.getDeviceId()||rec.getId()==task.getId()).toList();
        if(query.size()>0){
            result=true;
        }
        return result;
    }
    // get priority task 
    private Task getPriority(Task task){
        return task;
    }
    // get all running task
    public List<Task> getAllRunTask(){
        return runningQueue;
    }
    // get queue tasks
    public List<Task> getQueue(){
        return queue;
    }
    // check if any task running 
    public boolean checkRun(){
        boolean result=false;
        if(runningQueue.size()>0){
            while(!result){
                CheckRun thread=new CheckRun(runningQueue);
                thread.start();
                try {
                    thread.join();
                    boolean check=thread.checkRun(runningQueue);
                    if(check){
                        result=true;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
            }
        }
        return result;
    }
    // add list of tasks to the queue from the service 
    public void addToQueue(List<Task> currentList){
        queue=currentList;
    }
    // add single task to the queue
    public void addTaskToQueue(Task task){
        queue.add(task);
    }
    // add to completed task array queue
    public void addToComplete(Device device,boolean status,String state,String warning,boolean complete,Task task){
        //if(complete){
            CompletedTask compTask=new CompletedTask();
            compTask.setDevice(device);
            compTask.setStatus(status);
            compTask.setStatusString(state);
            compTask.setWarning(warning);
            compTask.setTask(task);
            completeTaskQueue.add(compTask);
            System.out.println(completeTaskQueue.size());
        //}
    }
    // add task back into queue if required
    public void failedTask(Task task,Device dev){
        runningQueue.remove(task);
        /* 
        // retry task before set to fail
        if(task.getRetry()<task.getSchedule().getRetries()){
            int tryA=task.getRetry();
            tryA++;
            task.setRetry(tryA);
            queue.add(task);
            System.out.println("Retry count at "+tryA);
        }else if(task.getRetry()==task.getSchedule().getRetries()){
            // add to complete array to deactive and set it to offline
            if(dev!=null){
                addToComplete(dev,false,"","",false, task);
            }
        }else queue.add(task);
        */
        queue.add(task);
    }
    public void updateQueue(Task task){
        task.setActive(false);
        taskService.saveTask(task);
    }
    public void updateTaskProcess(Task task){
        task.setActive(false);
    }
    public void requeueTask(Task task){
        if(task.getActive()&&task!=null&&!queue.contains(task)){
            queue.add(task);
        }
    }
    public boolean removeTasksByBoardId(long id){
        boolean deleteAll=false;
        List<Task> queryList=queue.stream().filter(rec->rec.getBoard()==id).toList();
        int delCount=0;
        if(queryList.size()>0){
            for(int i=0; i<queryList.size(); i++){
                Task tsk=queryList.get(i);
                int qIndex=queue.indexOf(tsk);
                if(qIndex>-1){
                    while(queue.indexOf(tsk)!=-1){
                        queue.remove(qIndex);
                        delCount++;
                    }
                }
            }
        }
        if(queryList.size()==delCount) deleteAll=true;
        return deleteAll;
    }
    public boolean batchRequeTasks(List<Task> batch){
        return queue.addAll(batch);
    }
    private boolean batchRemove(List<Task> batch){
        return queue.removeAll(batch);
    }
    public void clearRunningTask(){
        runningQueue.clear();
    }
    public void removeRunningTask(Task task){
        runningQueue.remove(task);
    }
    public BoardTask getBoardTask(Task task,Route route,Mode mode){
        BoardTask commandInput=null; 
        if(route!=null&&mode!=null){
            if(route.getModes()){
                commandInput=mode.getBoardAction();
            }else if(!route.getModes()){
                commandInput=route.getBoardAction();
            }
        }else
        {
            // schedule commands
            if(task.getSchedule()!=null){
                commandInput=boardTaskService.getBoardTask(task.getBoardTaskId());
            }
            // get command store in commandId 
            if(task.getSchedule()==null&&task.getCommandId()>0){
                Command com=commandService.getCommand(task.getCommandId());
                commandInput=com.getBoardCommand();
            }
        }
        if(commandInput!=null){
                    // retrieve pins,output and input lists
            List<BoardPin> pins=boardTaskService.getPins(commandInput.getId());
            List<OutputCurrent> output=boardTaskService.getOuputs(commandInput.getId());
            List<InputCurrent> input=boardTaskService.getInputs(commandInput.getId());
            commandInput.setPins(pins);
            commandInput.setOutput(output);
            commandInput.setInput(input);
        }
        return commandInput;
    }
    
    @Async
    @Transactional
    public void sendSocketMessage(Task task,Board board,Device device,Route route,Mode mode) throws InstantiationException{
        long startTime = System.nanoTime();
        Instant currDt=Instant.now();
        if(board.getSocket()&&task.getScheduledTime().isBefore(currDt)){
                long boardId=board.getId();
                String wsId=board.getWebsocketId();
                BoardTask commandInput=getBoardTask(task,route,mode); 
                String commandMsg="";
                if(commandInput==null)
                {
                    // command task if it's already in string
                    if(task.getBoardTaskJson()!=""&&task.getSystemTask()){
                        commandMsg=task.getBoardTaskJson();
                    }
                }
                if((commandInput!=null&&wsId!="")||(wsId!=""&&commandMsg!="")){
                    try {
                        SentToClientResponse sent=websocketHandler.sendToClient(wsId, commandInput,boardId,task,device,route,mode,commandMsg);
                        if(sent.getSent()){
                            batchRequeTasks(sent.getNextTasks());
                        } else if(!sent.getSent()){
                            Task retTask=taskService.taskFailed(task,board);
                            if(retTask!=null){
                                requeueTask(retTask);
                            }
                        }
                        // check the type of task
                        // if the task is a 1 time only check the scheduled time and compare to the current time
                        // if it's less than 1 hour difference requeue it or else log it and delete it
                    } catch (IOException e) {
                        // requeue task
                        taskService.taskFailed(task,board);
                        e.printStackTrace();
                    }
                }else{
                    requeueTask(task);
                }
                long endTime = System.nanoTime();
        }else
        {
            Task retTask=taskService.taskFailed(task,board);
            if(retTask!=null){
                requeueTask(retTask);
            }
        }
    }
    
}