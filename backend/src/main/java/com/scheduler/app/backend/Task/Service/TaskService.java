package com.scheduler.app.backend.Task.Service;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.scheduler.Base.Background;
import com.scheduler.Base.Exception.ErrorException;
import com.scheduler.Base.ModelBase.TaskEventId;
import com.scheduler.Base.Service.BaseService;
import com.scheduler.app.backend.Command.Models.Command;
import com.scheduler.app.backend.Command.Service.CommandService;
import com.scheduler.app.backend.InterfaceModels.Reference.TaskEventIdView;
import com.scheduler.app.backend.Messaging.Board.Models.ArraySerial.BoardTaskSerial;
import com.scheduler.app.backend.Messaging.MessageUtil;
import com.scheduler.app.backend.Messaging.Models.BoardPin;
import com.scheduler.app.backend.Messaging.Models.BoardTask;
import com.scheduler.app.backend.Messaging.Models.BoardVariable;
import com.scheduler.app.backend.Messaging.Models.InputCurrent;
import com.scheduler.app.backend.Messaging.Models.OutputCurrent;
import com.scheduler.app.backend.Messaging.Service.BoardTaskService;
import com.scheduler.app.backend.Schedule.Models.NextSchedule;
import com.scheduler.app.backend.Schedule.Models.Schedule;
import com.scheduler.app.backend.Task.Model.Task;
import com.scheduler.app.backend.Task.Repo.TaskRepo;
import com.scheduler.app.backend.Task.SchedulerTask;
import com.scheduler.app.backend.aREST.Models.Board;
import com.scheduler.app.backend.aREST.Models.Device;
import com.scheduler.app.backend.aREST.Models.Mode;
import com.scheduler.app.backend.aREST.Models.Route;
import com.scheduler.app.backend.aREST.Repo.BoardRepo;
import com.scheduler.app.backend.aREST.Service.BoardQueueService;
import com.scheduler.app.backend.aREST.Service.BoardService;
import com.scheduler.app.backend.aREST.Service.DeviceService;
import com.scheduler.app.backend.aREST.Service.RoutesService;
// crud on database. does not manipulate scheduler
@Service
public class TaskService extends BaseService<Task,TaskEventId>{
    private final TaskRepo taskRepo;
    private final BoardRepo boardRepo;
    private final BoardService boardService;
    private SchedulerTask scheduler=new SchedulerTask();
    private final DeviceService deviceService;
    private final RoutesService routesService;
    public final CommandService commandService;
    public final BoardTaskService boardTaskService;
    public final BoardQueueService boardQueueService;
    

    private Random randomGenerator;
    public TaskService(TaskRepo taskRepo, DeviceService deviceService, CommandService commandService, BoardTaskService boardTaskService, RoutesService routesService, BoardQueueService boardQueueService, BoardRepo boardRepo, @Lazy BoardService boardService) {
        this.taskRepo = taskRepo;
        this.boardRepo = boardRepo;
        this.boardService = boardService;
        this.deviceService = deviceService;
        this.routesService = routesService;
        this.commandService = commandService;
        this.boardTaskService = boardTaskService;
        this.boardQueueService = boardQueueService;
        randomGenerator = new Random();
    }
    // transaction types
    // queue - use for automated task scheduling and update device state
    // queue-add - add task to scheduler but not update route/function state
    // queue-add-trans - user add task to scheduler not update route/function state
    // disable - disable task but trigger next tasks
    // disable-end - disable task but does not trigger next tasks
    @Override
    protected void beforeSave(Task entity, Map<String, String> errors, Map<String, String> warnings) {
        // set transaction global
        if(!entity.getTransaction().equals("")){
            Background.putGlobal("transaction|"+entity.getId().toString(),entity.getTransaction());
        }
        // set task delay time from schedule
        if(entity.getSchedule()!=null){
            entity.setDelayTime(entity.getSchedule().getTime());
        }
        // update route/function state 
        if(entity.getTransaction().equals("queue")&&entity.getRoute()!=null||entity.getTransaction().equals("disable")||entity.getTransaction().equals("disable-end")&&entity.getRoute()!=null){
            routesService.save(entity.getRoute());
        }
        // disable task
        if(entity.getTransaction().equals("disable")||entity.getTransaction().equals("disable-end")){
            entity.setActive(false);
            entity.setInScheduler(false);
        }
        // queue task to schedule
        if(entity.getTransaction().equals("queue")||entity.getTransaction().equals("queue-add")){
            Board board=boardService.findById(entity.getBoard());
            Instant scheDt=addDuration(entity.getSchedule().getTime(),false,true);
            boolean active=entity.getSchedule().getStatus();
            entity.setScheduledTime(scheDt);
            entity.setActive(active);
            // if randomised is enabled
            if(entity.getSchedule().getModeRandom()&&entity.getSchedule()!=null){
                List<Mode> modeOpt=new ArrayList<>();
                modeOpt=routesService.getModesRouteId(entity.getRouteId());
                if(modeOpt.size()>0){
                    Mode ranMode=randomMode(modeOpt);
                    entity.setModeId(ranMode.getId());
                }
            }
            if(active){
                if((entity.getSchedule().getRepeatTask())||(!entity.getSchedule().getRepeatTask()&&!entity.getSchedule().getStartup())){
                    // add websocket connect command
                    // check if there an existing system connect task
                    TaskEventId parentTaskId=entity.getId();
                    long boardId=entity.getBoard();
                    long deviceId=entity.getDeviceId();
                    String query="select count(*) from task where parent_board_id="+parentTaskId.getBoardId();
                    query+=" and device_id="+parentTaskId.getDeviceId();
                    query+=" and event_time="+quoteParam(parentTaskId.getEventTime().toString());
                    query+=" and system_task=true and board_id="+boardId;
                    query+=" and device_id="+deviceId;
                    query+=" and application="+quoteParam("schedule httprequestconnection");

                    long existId=getDataLong(query);
                    if(existId<1){
                        Task connectTask=new Task();
                        BoardTask tsk=commandService.getRequestConnection();
                        long delayTime=entity.getSchedule().getTime();
                        BoardTask tempTask=new BoardTask(tsk);
                        Object boardMilObj=Background.getGlobal("board-millis|"+boardId);
                        long millis=0;
                        boolean delayCalculate=false;
                        Device device=board.getDevice().stream().filter(dev->dev.getId()==deviceId).findFirst().orElse(null);
                        delayCalculate=device.getSystemAutoCalculate();
                        if(boardMilObj!=null){
                            millis=Long.class.cast(boardMilObj);
                        } else {
                            millis=board.getCurrentMillis();
                        }
                        tempTask.setRunTarget(1);
                        tempTask.setDelayInterval(delayTime);
                        if(delayCalculate){
                            tempTask.setSystemCalculate(true);
                            tempTask.setDelayInterval(delayTime+millis);
                        }
                        tempTask.setVariable(new BoardVariable());
                        tempTask.initTaskId(entity.getBoard());
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
                        connectTask.setDelayTime(delayTime);
                        connectTask.setScheduledTime(scheDt);
                        connectTask.setBoard(entity.getBoard());
                        connectTask.setSystemTask(true);
                        connectTask.setParentTask(entity.getId());
                        connectTask.initId(entity.getBoard(),entity.getDeviceId());
                        connectTask.setDeviceId(entity.getDeviceId());
                        connectTask.setMotor(false);
                        connectTask.setActive(true);
                        connectTask.setAddtoSchedule(entity.getAddtoSchedule());
                        this.save(connectTask);
                    }
                }
            }
        }
        // add to scheduler queue
        if(entity.getAddtoSchedule()&&entity.getActive()){
            Background.putGlobal("add-to-queue|"+entity.getId().toString(),entity.getAddtoSchedule());
        }
    }
    @Override
    protected void afterSave(Task entity) {
        String transaction="";
        if(Background.globalExist("transaction|"+entity.getId().toString())){
            Object transGlob=Background.getGlobal("transaction|"+entity.getId().toString());
            transaction=(String) transGlob;
        }
        if(entity.getActive()){
            Board board=boardService.findById(entity.getBoard());
            boardQueueService.addToQueueTask(entity,board,entity.getDelayTime());
        }
        // add task to scheduler
        if(Background.globalExist("add-to-queue|"+entity.getId())){
            scheduler.addTaskToQueue(entity);
            entity.setInScheduler(true);
            taskRepo.save(entity);
            Background.removeGlobal("add-to-queue|"+entity.getId().toString());
            Background.removeGlobal("scheduler-out|"+entity.getId().toString());
        }
         // if schedule has next tasks add to scheduler
        if(entity.getSchedule()!=null&&transaction.equals("queue")||transaction.equals("disable")){
            Schedule sche=entity.getSchedule();
            if(sche.getNextSchedules().size()>0){
                for(NextSchedule ns:sche.getNextSchedules()){
                    String nextTrans="queue";
                    Schedule sch=ns.getTargetSchedule();
                    if(sch.getStatus()){
                        Task scheTsk=sch.getTask();
                        scheTsk.setAddtoSchedule(true);
                        if(sch.getNextSchedules().size()>0) nextTrans="queue-add";
                        scheTsk.setTransaction(nextTrans);
                        this.save(scheTsk);
                    }
                }
            }
        }

        Background.removeGlobal("transaction|"+entity.getId().toString());
    }
    
    @Override
    protected void beforeDelete(TaskEventId id) {
        Task task=this.findById(id);
        String deleGlob="";
        String key="remove|"+id.toString();
        if(Background.globalExist(key)){
            deleGlob=(String)Background.getGlobal(key);
        }
        if(task.getActive()&&deleGlob.equals("")){
            Board board=boardService.findById(task.getBoard());
            boardQueueService.addToQueueTask(task, board, task.getDelayTime());
        }
        
        if(task.getActive()&&deleGlob.equals("remove")){
            // delete existing board operation
            if(task.getBoardTaskId()>0){
                boardTaskService.deleteBoardTask(task.getBoardTaskId());
            }
        }
        Background.removeGlobal(key);
    }
 
    public MessageUtil getMsgUtil(){
        return new MessageUtil();
    }
    // add all tasks to scheduler
    public void addAllTasks(List<Task> tasks){
        scheduler.addAllTasks(tasks);
    }
    public Task addTask(Task entry){
        Instant setDt=addDuration(1000,false,false);
        entry.setScheduledTime(setDt);
        entry.setActive(true);
        Task add=this.save(entry);
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
        tsk=this.save(tsk);
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
        tsk=this.save(tsk);
        addTaskToScheduler(tsk);
        return tsk;
    }
    // delete 1 time tasks that are 1 time only when board start up and disable active tasks
    @Transactional
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
                    this.delete(tsk.getId());
                }
            }
        }
        return modTasks;
    }
    @Transactional 
    public void removeTasksFromScheduler(long boardId){
        // disabled automated and startup schedule tasks
        scheduler.removeTasksByBoardId(boardId);
        // delete one time tasks
        //taskRepo.deleteOneTimeTaskByBoardId(boardId,true);
        // gets active tasks
        List<TaskEventIdView> ids=taskRepo.getActiveTaskIdsTest(boardId);
        for(TaskEventIdView id:ids){
            TaskEventId tid=new TaskEventId(id.getBoardId(),id.getDeviceId(), id.getEventTime());
            Task tsk =this.findById(tid);
            if(tsk!=null){
                if(tsk.getSchedule()!=null){
                    tsk.setTransaction("disable-end");
                    this.save(tsk);
                }else
                {
                    Background.putGlobal("remove|"+tsk.getId().toString(),"remove");
                    this.delete(tid);   
                }
            }
        }
    }

    // use for http polling
    @Transactional
    public List<BoardTaskSerial> getNextTasks(long id,Board board){
        List <BoardTaskSerial> taskLists=new ArrayList<>();
        List<Task> filterTasks=scheduler.queryQueueNow(id);
            for(Task tsk:filterTasks){
                try {
                    // signify out of scheduler
                    Background.putGlobal("scheduler-out|"+tsk.getId().toString(),false);
                    if(tsk.getDeviceId()>0){
                        Device device=deviceService.getDevice(tsk.getDeviceId());
                        Route route=device.getRoutes().stream().filter(rec->rec.getId()==tsk.getRouteId()).findFirst().orElse(null);
                        List<BoardTask> boardTasks=new ArrayList<>();
                        // if task has route and mode id get board task from there
                        if(tsk.getRouteId()>0){
                            boardTasks=completeTask(tsk, device,tsk.getRouteId(),tsk.getModeId());
                            
                            if(boardTasks.size()>0){
                                for(BoardTask bt:boardTasks){
                                    taskLists.add(new BoardTaskSerial(bt));
                                }
                            }
                            List<Task> sysNextTasks=scheduler.getSystemTaskByBoardId(id);
                            if(sysNextTasks.size()>0){
                                for(Task sysTask:sysNextTasks){
                                    BoardTask nTsk=scheduler.boardTaskToObject(sysTask.getBoardTaskJson());
                                    if(nTsk!=null){
                                        taskLists.add(new BoardTaskSerial(nTsk));
                                        this.delete(sysTask.getId());
                                    }
                                }
                            }          
                        }else if(tsk.getSystemTask()&&!tsk.getBoardTaskJson().equals(""))
                        {
                            // system task
                            long routeId=0;
                            if(route!=null) routeId=route.getId(); 
                            List<BoardTask> tasks=completeTask(tsk,device,routeId,0);
                            for(BoardTask bt:tasks){
                                taskLists.add(new BoardTaskSerial(bt));
                            }
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
    // task compute and complete that returns board task
    @Transactional 
    public List<BoardTask> completeTask(Task task,Device device,long routeId,long modeId){
        List<BoardTask> tasks=new ArrayList<>();
        Instant dt=Instant.now();
        Route route=null;
        // get current action
        if(device!=null){
            if(routeId>0){
                route=device.getRoutes().stream().filter(rec->rec.getId()==routeId).findFirst().orElse(null);
                if(modeId>0){
                    Mode mode=routesService.getMode(modeId);
                    if(mode!=null) tasks.addAll(route.activateMode(mode.getId()));
                }else
                {
                    if(route!=null) tasks.add(route.getBoardAction());               
                }
                if(tasks.size()==0){
                    BoardTask boardTask=scheduler.boardTaskToObject(task.getBoardTaskJson());
                    if(boardTask!=null) tasks.add(boardTask);
                }
            }else if(task.getSystemTask()&&!task.getBoardTaskJson().equals("")){
                // system task
                BoardTask boardTask=scheduler.boardTaskToObject(task.getBoardTaskJson());
                Instant current=Instant.now();
                Instant tskTrig=task.getScheduledTime();
                long millis = Duration.between(tskTrig, current).toMillis();
                // modify delay interval if bigger than 1 second
                if(millis>1000){
                    if(device.getSystemAutoCalculate()){
                        long intervalCalculate=device.getBoard().getCurrentMillis()-millis;
                        boardTask.setDelayInterval(intervalCalculate);
                    }else boardTask.setDelayInterval(boardTask.getDelayInterval()-millis);
                }
                tasks.add(boardTask);
            }
        }
        // update task 
        if(task.getSchedule()!=null){
            if(task.getSchedule().getStartup()||!task.getSchedule().getStartup()&&!task.getSchedule().getRepeatTask()){
                task.setTransaction("disable");
                task.setRoute(route);
                task=this.save(task);
            }
            if(task.getSchedule().getRepeatTask()&&dt.isAfter(task.getScheduledTime())){
                task.setTransaction("queue");
                task.setRoute(route);
                task.setAddtoSchedule(true);
                this.save(task);
            }else if(task.getSchedule().getRepeatTask()){
                task.setAddtoSchedule(true);
                this.save(task);
            }
        }else if(task.getOneTimeJob()&&task.getSchedule()==null){
            this.delete(task.getId());
        }
        return tasks;
    }
    // task successful
    @Transactional
    public List<Task> taskComplete(Task task,Device device,Route route,Mode mode){
        List<Task> nextScheduledTasks=new ArrayList<>();
        Instant dt=Instant.now();
        if(task.getSchedule()!=null){
            if(task.getSchedule().getStartup()||!task.getSchedule().getStartup()&&!task.getSchedule().getRepeatTask()){
                task.setTransaction("disable");
                task=this.save(task);
            }
            if(task.getSchedule().getRepeatTask()&&dt.isAfter(task.getScheduledTime())){
                task.setTransaction("queue");
                task.setAddtoSchedule(true);
                this.save(task);
            }else if(task.getSchedule().getRepeatTask()){
                nextScheduledTasks.add(task);
                task.setAddtoSchedule(true);
                this.save(task);
            }
            // set mode value to state associated with that function
            if(device!=null){

            }
        }else if(task.getOneTimeJob()&&task.getSchedule()==null){
            this.delete(task.getId());
        }
        return nextScheduledTasks;
    }
    // task failed
    @Transactional
    public Task taskFailed(Task task,Board board){
        // fail clause for 1 time jobs
        if(task.getOneTimeJob()&&task.getSchedule()==null){
            Instant currentDt=Instant.now();
            Instant scheduled=task.getScheduledTime();
            // check if there an 2 min or more diffence to delete 1 time job
            long minDiff=Math.abs(Duration.between(currentDt, scheduled).toMinutes());
            if(minDiff>=2){
                Background.putGlobal("remove|"+task.getId().toString(),"remove");
                this.delete(task.getId());
                task=null;
            }else scheduler.addTaskToQueue(task); 
        }else if(task.getSchedule()!=null){
            // deactive automated tasks if offline period is met
            task=this.findById(task.getId());
            Instant lastCon=board.getLastConnectDateTime();
            if(lastCon!=null){
                Instant current=Instant.now();
                long diff=Duration.between(lastCon, current).toMillis();
                if(diff>=board.getOffline()){
                    task.setTransaction("disable");
                    task=this.save(task);
                }else scheduler.addTaskToQueue(task);
            }else 
            {
                task.setTransaction("disable");
                task=this.save(task);
            }
        }
        return task;
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
  
    // get tasks by board and device
    public List<Task> getTasksByBoardDevice(String boardId,boolean active,String deviceId){
        List <Task> tasks=new ArrayList<>();
        long boardLonId=getDataLong("select id from board where board_id="+quoteParam(boardId));
        Board board=boardRepo.findById(boardLonId).orElse(null);
        if(board!=null){
            List<Long> deviceIds=board.getDevice().stream().map(d->d.getId()).toList();
            if(deviceId!=null && !deviceId.isEmpty()){
                long devId=getDataLong("select id from device where device_id="+quoteParam(deviceId));
                deviceIds=deviceIds.stream().filter(id->id==devId).toList();
            }
            tasks=taskRepo.getDeviceRoutineTasks(deviceIds, active);
        }
        return tasks;
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
    private Instant addDuration(long time,boolean shaveSeconds,boolean shaveNano){
        Instant currenDateTime = Instant.now();
        long sec=ZonedDateTime.ofInstant(currenDateTime, ZoneId.systemDefault()).getSecond();
        long nano=ZonedDateTime.ofInstant(currenDateTime, ZoneId.systemDefault()).getNano();
        // shave of seconds and nano seconds
        if(shaveSeconds){
            currenDateTime=currenDateTime.minusSeconds(sec);
        }
        if(shaveNano){
            currenDateTime=currenDateTime.minusNanos(nano);
        }
        currenDateTime=currenDateTime.plus(time, ChronoUnit.MILLIS);
        return currenDateTime;
    }
    @Override
    protected JpaRepository<Task,TaskEventId> repository() {
        // TODO Auto-generated method stub
        return taskRepo;
    }
}
