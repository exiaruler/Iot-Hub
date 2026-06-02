package com.scheduler.app.backend.aREST.Service;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.scheduler.Base.Base;
import com.scheduler.Base.Exception.ErrorException;
import com.scheduler.Base.ModelBase.TaskEventId;
import com.scheduler.app.backend.Command.Models.Command;
import com.scheduler.app.backend.Command.Service.CommandService;
import com.scheduler.app.backend.Messaging.MessageUtil;
import com.scheduler.app.backend.Messaging.Board.Models.ArraySerial.BoardTaskSerial;
import com.scheduler.app.backend.Messaging.Models.BoardPin;
import com.scheduler.app.backend.Messaging.Models.BoardTask;
import com.scheduler.app.backend.Messaging.Models.BoardVariable;
import com.scheduler.app.backend.Messaging.Models.InputCurrent;
import com.scheduler.app.backend.Messaging.Models.OutputCurrent;
import com.scheduler.app.backend.Messaging.Service.BoardTaskService;
import com.scheduler.app.backend.Task.Model.CompletedTask;
import com.scheduler.app.backend.Task.SchedulerTask;
import com.scheduler.app.backend.aREST.Models.Board;
import com.scheduler.app.backend.aREST.Models.Device;
import com.scheduler.app.backend.aREST.Models.Mode;
import com.scheduler.app.backend.aREST.Models.Route;
import com.scheduler.app.backend.aREST.Models.Schedule;
import com.scheduler.app.backend.aREST.Models.Task;
import com.scheduler.app.backend.aREST.Repo.ScheduleRepo;
import com.scheduler.app.backend.aREST.Repo.TaskRepo;
// crud on database. does not manipulate scheduler
@Service
public class TaskService extends Base{
    private final TaskRepo taskRepo;
    private SchedulerTask scheduler=new SchedulerTask();
    private final ScheduleRepo serviceSch;
    private final DeviceService deviceService;
    private final RoutesService routesService;
    public final CommandService commandService;
    public final BoardTaskService boardTaskService;
    public final BoardQueueService boardQueueService;

    private Random randomGenerator;
    public TaskService(TaskRepo taskRepo, ScheduleRepo serviceSch, DeviceService deviceService, CommandService commandService, BoardTaskService boardTaskService, RoutesService routesService, BoardQueueService boardQueueService) {
        this.taskRepo = taskRepo;
        this.serviceSch = serviceSch;
        this.deviceService = deviceService;
        this.routesService = routesService;
        this.commandService = commandService;
        this.boardTaskService = boardTaskService;
        this.boardQueueService = boardQueueService;
        randomGenerator = new Random();
    }
    public Task saveTask(Task task){
        return taskRepo.save(task);
    }
    public MessageUtil getMsgUtil(){
        return new MessageUtil();
    }
    public Task addTask(Task entry){
        Instant setDt=addDuration(1000,false);
        entry.setScheduledTime(setDt);
        entry.setActive(true);
        Task add=taskRepo.save(entry);
        addToScheduler();
        return add;
    }
    // test function mode for route/function
    public Task testModeFunction(BoardTask boardTask,long boardId,long deviceId,String electrode){
        Task tsk=new Task();
        String jsonStr="";
        // convert boardTask to json string
        try {
            boardTask.initTaskId(boardId);
            boardTask.electrodeCalculate(electrode);
            jsonStr=messageUtil.objectToJsonString(boardTask);
        } catch (Exception e) {
            throw new ErrorException("server error occured on that mode");
        }
        tsk.setBoard(boardId);
        tsk.setDeviceId(deviceId);
        tsk.initId(boardId, deviceId);
        tsk.setActive(true);
        tsk.setOneTimeJob(true);
        tsk.setApplication(boardTask.getMethod()+" testing mode function");
        tsk.setBoardTaskJson(jsonStr);
        tsk=taskRepo.save(tsk);
        scheduler.addTaskToQueue(tsk);
        return tsk;

    }
    // board commands only
    public String runCommand(String board,String command,String action,boolean system,boolean systemTask){
        String act="";
        long boardId=getDataLong("select id from board where board_id="+quoteParam(board));
        // find command
        Command com=commandService.getCommandByCommand(command,action, system);
        if(com!=null&&boardId>0){
            createOneTimeTask(com.getCommand()+" task", boardId, 0,0, com.getId(),systemTask);
            act=com.getDisplayName()+" has been added to the queue (action will occcur within 1 minute)";
        }else if(com==null){
            act="command does not exists";
        }
        return act;
    }
    // run commands enter by user
    public String runCommandDeviceInput(Mode mode,long deviceId){
        String act="";
        long boardId=getDataLong("select board_id from device where id="+deviceId);
        if(mode!=null){
            String wsSession=getDataString("select websocket_id from board where id="+boardId);
            if(wsSession=="") act=mode.getMode()+" has been added to the queue";
            createOneTimeTask(mode.getMode(), boardId, deviceId, mode.getId(),0, false);
        }
        return act;
    }
    public Mode randomMode(List<Mode>list){
        int index=randomGenerator.nextInt(list.size());
        return list.get(index);
    }
    public Task createSystemTask(){
        Task sysTask=new Task();
        return sysTask;
    }
    public Task createOneTimeTask(String description,long boardId,long deviceId,long modeId,long commandId,boolean systemTask){
        Task tsk=new Task();
        tsk.setBoard(boardId);
        tsk.setDeviceId(deviceId);
        tsk.initId(boardId, deviceId);
        tsk.setActive(true);
        tsk.setApplication(description);
        tsk.setCommandId(commandId);
        tsk.setModeId(modeId);
        tsk.setSystemTask(systemTask);
        tsk=taskRepo.save(tsk);
        addTaskToScheduler(tsk);
        return tsk;
    }
    // schedule task to scheduler
    public List<Task> setTaskSchedule(Task task,boolean addToSchedule){
        List<Task> modTasks=new ArrayList<>();
        Instant scheDt=addDuration(task.getSchedule().getTime(),true);
        boolean active=task.getSchedule().getStatus();
        task.setScheduledTime(scheDt);
        task.setActive(active);
        // if randomised is enabled
        if(task.getSchedule().getModeRandom()&&task.getSchedule()!=null){
            List<Mode> modeOpt=new ArrayList<>();
            modeOpt=routesService.getModesRouteId(task.getRouteId());
            if(modeOpt.size()>0){
                Mode ranMode=randomMode(modeOpt);
                task.setModeId(ranMode.getId());
            }
        }
        boardQueueService.addToQueueTask(task);
        task=taskRepo.save(task);
        modTasks.add(task);
        if(active){
            // if task is a repeative send connection command to board
            if(task.getSchedule().getRepeatTask()){
                // add websocket connect command
                // check if there an existing system connect task
                TaskEventId parentTaskId=task.getId();
                long boardId=task.getBoard();
                long deviceId=task.getDeviceId();
                String query="select count(*) from task where parent_board_id="+parentTaskId.getBoardId();
                query+=" and parent_device_id="+parentTaskId.getDeviceId();
                query+=" and parent_event_time="+quoteParam(parentTaskId.getEventTime().toString());
                query+=" and system_task=true and board_id="+boardId;
                query+=" and device_id="+deviceId;
                query+=" and application="+quoteParam("schedule httprequestconnection");
                long existId=taskRepo.findAll().stream().filter(rec->rec.getParentTask()==parentTaskId&&rec.getSystemTask()&&rec.getBoard()==boardId&&rec.getDeviceId()==deviceId&&rec.getApplication().equals("schedule httprequestconnection")).count();
                //long existId=getDataLong(query);
                if(existId<1){
                    Task connectTask=new Task();
                    Command wscom=commandService.getCommandByCommand("httprequestconnection", "schedule",true);
                    BoardTask tsk=wscom.getBoardCommand();
                    long delayTime=task.getSchedule().getTime();
                    BoardTask tempTask=new BoardTask(tsk);
                    tempTask.runTarget(1);
                    tempTask.setDelayInterval(delayTime);
                    tempTask.setVariable(new BoardVariable());
                    tempTask.initTaskId(task.getBoard());
                    //long boardId=task.getBoard();
                    tempTask.initTaskId(boardId);
                    tempTask.setCommand(null);
                    // convert message to string for system tasks
                    String commStr="";
                    try {
                        commStr=messageUtil.objectToJsonString(tempTask);
                    } catch (JsonProcessingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    connectTask.setApplication("schedule httprequestconnection");
                    if(commStr!=""){
                        connectTask.setBoardTaskJson(commStr);
                    }
                    connectTask.setBoard(task.getBoard());
                    connectTask.setSystemTask(true);
                    connectTask.setParentTask(task.getId());
                    connectTask.initId(task.getBoard(),task.getDeviceId());
                    //connectTask.parentTask(task.getId());
                    connectTask.setDeviceId(task.getDeviceId());
                    connectTask.setMotor(false);
                    connectTask.setActive(true);
                    taskRepo.save(connectTask);
                    boardQueueService.addToQueueTask(connectTask);
                    modTasks.add(connectTask);      
                }
                
            }
        }
        if(addToSchedule)addToScheduler();
        return modTasks;
    }
    // delete 1 time tasks that are 1 time only when board start up and disable active tasks
    //@Transactional
    public List<Task> purgeOldTasks(long boardId){
        List<Task> modTasks=new ArrayList<>();
        // disabled automated and startup schedule tasks
        scheduler.removeTasksByBoardId(boardId);
        // purge 1 time jobs from database
        List<Task> taskIds=taskRepo.getOneTimeJobs(boardId);
        if(taskIds.size()>0){
            for(int i=0; i<taskIds.size(); i++){
                Task tsk=taskIds.get(i);
                if(tsk!=null){
                    if(tsk.getBoardTaskId()>0){
                        boardTaskService.deleteBoardTask(tsk.getBoardTaskId());
                    }
                    taskRepo.delete(tsk);
                }
            }
        }
        /* 
        List<Task> autoTaskIds=taskRepo.getRoutineJobs(boardId);
        if(autoTaskIds.size()>0){
            for(int x=0; x<autoTaskIds.size(); x++){
                Task tsk=autoTaskIds.get(x);
                if(tsk!=null){
                    tsk.active(true);
                    taskRepo.save(tsk);
                    modTasks.add(tsk);
                }
            }
        }
        addToScheduler();
        */
        return modTasks;
    }
    public void deleteTask(Task task){
        if(task.getOneTimeJob()){
            if(task.getBoardTaskId()>0){
                boardTaskService.deleteBoardTask(task.getBoardTaskId());
            }
            taskRepo.delete(task);
        }
        if(task.getSchedule()!=null){
            //task.setActive(false);
            //taskRepo.save(task);
        }
        //addToScheduler();   
    }
    // use for http polling
    public List<BoardTaskSerial> getNextTasks(long id){
        List <BoardTaskSerial> taskLists=new ArrayList<>();
        List<Task> filterTasks=scheduler.queryQueueNow(id);

            for(Task tsk:filterTasks){
                //Task tsk=filterTasks.get(i);
                try {
                    if(tsk.getDeviceId()>0){
                        Device device=deviceService.getDevice(tsk.getDeviceId());
                        Route route=device.getRoutes().stream().filter(rec->rec.getId()==tsk.getRouteId()).findFirst().orElse(null);
                        BoardTask boardTask=null;
                        Mode mode=null;
                        // if task has route and mode id get board task from there
                        if(tsk.getRouteId()>0){
                            if(tsk.getModeId()>0){
                                mode=routesService.getMode(tsk.getModeId());
                                if(mode!=null){
                                    boardTask=mode.getBoardAction();
                                }
                            }else
                            {
                                Route rou=routesService.getRouteById(tsk.getRouteId());
                                if(rou!=null){
                                    boardTask=rou.getBoardAction();
                                }
                            }
                            if(boardTask==null){
                                boardTask=scheduler.boardTaskToObject(tsk.getBoardTaskJson());
                            }
                            if(boardTask!=null)taskLists.add(new BoardTaskSerial(boardTask));
                            List<Task> nextTasks=taskComplete(tsk, device, route, mode);
                            // add future connection tasks in the next task
                            List<Task> sysNextTasks=nextTasks.stream().filter(t->t.getSystemTask()&&t.getParentTask().equals(tsk.getId())&&!t.getBoardTaskJson().equals("")).toList();
                            List<Task> requeueTasks=nextTasks.stream().filter(t->t.getId().equals(tsk.getId())).toList();
                            //System.out.println("sysNextTask "+sysNextTasks.size());
                            //System.out.println("requeueTasks "+requeueTasks.size());
                            //if(sysNextTasks.size()>0) filterTasks.addAll(sysNextTasks);
                            
                            if(sysNextTasks.size()>0){
                                for(Task sysTask:sysNextTasks){
                                    BoardTask nTsk=scheduler.boardTaskToObject(sysTask.getBoardTaskJson());
                                    if(nTsk!=null){
                                        taskLists.add(new BoardTaskSerial(nTsk));
                                        taskRepo.deleteById(sysTask.getId());
                                    }
                                }
                            }
                            
                            scheduler.batchRequeTasks(requeueTasks);
                            
                        }else if(tsk.getSystemTask()&&tsk.getBoardTaskJson()!="")
                        {
                            // system task
                            boardTask=scheduler.boardTaskToObject(tsk.getBoardTaskJson());
                            Instant current=Instant.now();
                            Instant tskTrig=tsk.getScheduledTime();
                            long millis = Duration.between(tskTrig, current).toMillis();
                            // modify delay interval if bigger than 1 second
                            if(millis>1000){
                                boardTask.setDelayInterval(boardTask.getDelayInterval()-millis);
                            }
                            //System.out.println(boardTask.getDelayInterval());
                            List<Task> nextTasks=taskComplete(tsk, device, route,null);
                            scheduler.batchRequeTasks(nextTasks);
                            taskLists.add(new BoardTaskSerial(boardTask));
                        }
                    }else if(tsk.getDeviceId()==0){
                        BoardTask boTask=boardTaskService.getTaskByCommandId(tsk.getCommandId());
                        if(boTask!=null)taskLists.add(new BoardTaskSerial(boTask));
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        return taskLists;
    }
    // task successful
    @Transactional
    public List<Task> taskComplete(Task task,Device device,Route route,Mode mode){
        List<Task> nextScheduledTasks=new ArrayList<>();
        Instant dt=Instant.now();
        if(task.getSchedule()!=null){
            if(task.getSchedule().getStartup()){
                task.setActive(false);
                task=taskRepo.save(task);
            }
            if(task.getSchedule().getRepeatTask()&&dt.isAfter(task.getScheduledTime())){
                nextScheduledTasks.addAll(setTaskSchedule(task,false));
            }else if(task.getSchedule().getRepeatTask()){
                nextScheduledTasks.add(task);
            }
            // set mode value to state associated with that function
            if(device!=null){

            }
            //task.getSchedule().getDevice().setState(state);
            //task.getSchedule().getDevice().setWarning(warning);
        }else if(task.getOneTimeJob()&&task.getSchedule()==null){
            deleteTask(task);
        }
        return nextScheduledTasks;
    }
    // task failed
    public Task taskFailed(Task task,Board board){
        // fail clause for 1 time jobs
        if(task.getOneTimeJob()&&task.getSchedule()==null){
            Instant currentDt=Instant.now();
            Instant scheduled=task.getScheduledTime();
            // check if there an 2 min or more diffence to delete 1 time job
            long minDiff=Math.abs(Duration.between(currentDt, scheduled).toMinutes());
            if(minDiff>=2){
                deleteTask(task);
                task=null;
            }
        }else if(task.getSchedule()!=null){
            // decide to deactive automated tasks if restart timeout enabled on board
            task=taskRepo.findById(task.getId()).get();
            boolean state=true;
            if(board.getRestartTimeout()){
                Instant currentDt=Instant.now();
                long milDiff=Math.abs(Duration.between(currentDt, task.getScheduledTime()).toMillis());
                if(milDiff>=board.getTimeout()){
                    state=false;
                }
            }
            task.setActive(state);
            task=taskRepo.save(task);
        }
        return task;
    }
    // deactive route tasks
    @Transactional
    public void deactiveTask(List<Long> ids){
        List<Task> tasks=taskRepo.getDeviceRoutineTasks(ids,true);
        for(int i=0; i<tasks.size(); i++){
            Task tsk=tasks.get(i);
            tsk.setActive(false);
            taskRepo.save(tsk);
            tasks.set(i, tsk);
        }
        scheduler.batchRemove(tasks);
    }
    
    // modify task when finished in scheduler
    public void modifyTaskFromScheduler(Task task,CompletedTask complete){
        // update device state and warning
        if(complete.getDevice()!=null){
            String state="";
            String warning="";
            if(complete.getWarning()!=""&&!complete.getStatus()){
                warning=complete.getWarning();
            }else{
                if(complete.getStatusString()!=""){
                    state=complete.getStatusString();
                }else{
                    state="Offline";
                }
            }
            if(task.getSchedule()!=null){
                task.getSchedule().getDevice().setState(state);
                task.getSchedule().getDevice().setWarning(warning);
            }
        }
        //task.setRetry(0);
        if(!task.getOneTimeJob()){
            if(complete.getStatus()){
                if(task.getSchedule().getStartup()){
                    task.setActive(false);
                }
                
                if(task.getSchedule().getRepeatTask()){
                    Instant schedule=addDuration(task.getSchedule().getTime(),false);
                    task.setScheduledTime(schedule);
                    task.setActive(true);
                }
                // start next task
                if(task.getSchedule().getNextTask()!=0){
                    Schedule schedule=serviceSch.getReferenceById(task.getSchedule().getNextTask());
                    if(schedule!=null){
                        setTaskSchedule(schedule.getTask(),true);
                    }
                }
            // disable schedule task and update device state to offline
            }else if(!complete.getStatus()){
                task.setActive(false);
            }
            taskRepo.save(task);
            addToScheduler();
        }else
        {
            deviceService.updateDeviceAfterAction(complete,complete.getDevice());
            taskRepo.save(task);
            deleteTask(task);
        }
    }
    // get board task from within task
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
    // delete all task in queue and running
    public void masterDelete(){
        List<Task> list=taskRepo.getAllTaskAct(true);
        for(int i=0; i<list.size(); i++){
            Task update=list.get(i);
            update.setActive(false);
            taskRepo.save(update);
        }
        //taskRepo.deleteAll();
        addToScheduler();
        scheduler.clearRunningTask();
    }
    public void clearQueue(){
        taskRepo.deleteAll();
        scheduler.clearRunningTask();
    }
    public Optional<Task> getTask(TaskEventId id){
        return taskRepo.findById(id);
    }
    // find all task in database
    public List<Task> getAllTask(){
        return taskRepo.findAll();
    }
    // get tasks in scheduler
    public List<Task> getTasksScheduler(){
        return scheduler.getQueue();
    }
    public List<Task> getTasksByBoard(long boardId,long deviceId){
        return taskRepo.findAll().stream().filter(tsk->tsk.getBoard()==boardId || tsk.getDeviceId()==deviceId && tsk.getBoard()==boardId).toList();
    }
    // find task by active
    public List<Task> getAllTaskStat(boolean status){
        return taskRepo.getAllTaskAct(status);
    }
    public List<Task> getAllRunningTask(){
        List<Task> list=scheduler.getAllRunTask();
        return list;
    }
    public boolean checkCurrentRun(){
        return scheduler.checkRun();
    }
    // add task to scheduler
    public boolean addToScheduler(){
        boolean results=false;
        List <Task> list=taskRepo.getAllTaskAct(true);
        if(list.size()>0) results=true;
        scheduler.addToQueue(list);
        return results;
    }
    public void addTaskToScheduler(Task task){
        scheduler.addTaskToQueue(task);
    }
    public boolean checkNextTask(Instant datetime,long boardId,TaskEventId taskId){
        boolean res=false;
        long diff=60;
        Instant minPast=datetime.minusSeconds(diff);
        Instant ahead=datetime.plusSeconds(diff);
        String query="select count(*) from task where scheduled_time BETWEEN "+quoteParam(minPast.toString())+" and "+quoteParam(datetime.toString());
        query+=" and active=true and board_id="+boardId;
        if(taskId!=null){
            query+=" and id!="+taskId;
        }
        query+=" or scheduled_time BETWEEN "+quoteParam(datetime.toString())+" and "+quoteParam(ahead.toString());
        query+=" and active=true and board_id="+boardId;
        if(taskId!=null){
            query+=" and id!="+taskId;
        }
        int output=getDataInt(query);
        if(output>0) res=true;
        return res;
    }
    // schedule time for task
    private Instant addDuration(long time,boolean shave){
        Instant currenDateTime = Instant.now();
        long sec=ZonedDateTime.ofInstant(currenDateTime, ZoneId.systemDefault()).getSecond();
        long nano=ZonedDateTime.ofInstant(currenDateTime, ZoneId.systemDefault()).getNano();
        // shave of seconds and nano seconds
        if(shave){
            currenDateTime=currenDateTime.minusSeconds(sec);
            currenDateTime=currenDateTime.minusNanos(nano);
        }
        currenDateTime=currenDateTime.plus(time, ChronoUnit.MILLIS);
        return currenDateTime;
    }
}
