package com.scheduler.app.backend.aREST.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scheduler.Base.Base;
import com.scheduler.app.backend.Command.Models.Command;
import com.scheduler.app.backend.Command.Service.CommandService;
import com.scheduler.app.backend.Messaging.Models.BoardTask;
import com.scheduler.app.backend.Messaging.Models.BoardVariable;
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
    private final TaskRepo service;
    private SchedulerTask sche=new SchedulerTask();
    private final ScheduleRepo serviceSch;
    private final DeviceService deviceService;
    private final RoutesService routesService;
    private final CommandService commandService;
    public final BoardTaskService boardTaskService;
    private Random randomGenerator;
    public TaskService(TaskRepo service, ScheduleRepo serviceSch, DeviceService deviceService, CommandService commandService, BoardTaskService boardTaskService, RoutesService routesService) {
        this.service = service;
        this.serviceSch = serviceSch;
        this.deviceService = deviceService;
        this.routesService = routesService;
        this.commandService = commandService;
        this.boardTaskService = boardTaskService;
        randomGenerator = new Random();
    }
    public Task saveTask(Task task){
        return service.save(task);
    }
    public Task addTask(Task entry){
        LocalDateTime setDt=addDuration(1000);
        entry.setScheduledTime(setDt);
        entry.setActive(true);
        Task add=service.save(entry);
        addToScheduler();
        return add;
    }
    public Mode randomMode(List<Mode>list){
        int index=randomGenerator.nextInt(list.size());
        return list.get(index);
    }
    // schedule task to scheduler
    public List<Task> setTaskSchedule(Task task,boolean addToSchedule){
        List<Task> modTasks=new ArrayList<>();
        LocalDateTime scheDt=addDuration(task.getSchedule().getTime());
        boolean active=task.getSchedule().getStatus();
        task.setScheduledTime(scheDt);
        task.setActive(active);
        if(task.getSchedule().getModeRandom()&&task.getSchedule()!=null){
            if(task.getRandomModes().isEmpty()){
                List<Mode> modeOpt=task.getSchedule().getRoute().getMode();
                if(modeOpt instanceof List==true){
                    task.setRandomModes(routesService.getModesRouteId(task.getRouteId()));
                }
            }
            if(task.getRandomModes().size()>0){
                Mode ranMode=randomMode(task.getRandomModes());
                task.setModeId(ranMode.getId());
            }
        }
        task=service.save(task);
        modTasks.add(task);
        if(active){
            // if task is a repeative send connection command to board
            if(task.getSchedule().getRepeatTask()){
                // add websocket connect command
                // check if there an existing system connect task
                String query="Select Id from scheduler.task where parentTask="+task.getParentTask();
                query+=" and systemTask=true and Board="+task.getBoard();
                query+=" and deviceId="+task.getDeviceId();
                query+=" and application="+quoteParam("schedule wsconnectopen");
                long existId=getDataLong(query);
                if(existId<1){
                    Task connectTask=new Task();
                    Command wscom=commandService.getCommandByCommand("wsconnectopen", "schedule",true);
                    BoardTask tsk=wscom.getBoardCommand();
                    long delayTime=task.getSchedule().getTime();
                    BoardTask tempTask=new BoardTask(tsk);
                    tempTask.runTarget(1);
                    tempTask.setDelayInterval(delayTime);
                    tempTask.setVariable(new BoardVariable());
                    tempTask.setId(0);
                    tempTask.setCommand(null);
                    BoardTask add=boardTaskService.addBoardTask(tempTask);

                    connectTask.setApplication("schedule wsconnectopen");
                    connectTask.setBoard(task.getBoard());
                    connectTask.setSystemTask(true);
                    connectTask.parentTask(task.getId());
                    connectTask.setDeviceId(task.getDeviceId());
                    connectTask.setBoardTaskId(add.getId());
                    connectTask.setMotor(false);
                    connectTask.setActive(true);
                    service.save(connectTask);
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
        sche.removeTasksByBoardId(boardId);
        //addToScheduler();
        // purge 1 time jobs
        List<Long> taskIds=service.getOneTimeJobIds(boardId);
        if(taskIds.size()>0){
            for(int i=0; i<taskIds.size(); i++){
                Task tsk=service.findById(taskIds.get(i)).get();
                if(tsk!=null){
                    if(tsk.getBoardTaskId()>0){
                        boardTaskService.deleteBoardTask(tsk.getBoardTaskId());
                    }
                    service.delete(tsk);
                }
            }
        }
        List<Long> autoTaskIds=service.getRoutineJobIds(boardId);
        if(autoTaskIds.size()>0){
            for(int x=0; x<autoTaskIds.size(); x++){
                Task tsk=service.findById(autoTaskIds.get(x)).get();
                if(tsk!=null){
                    tsk.active(false);
                    service.save(tsk);
                    modTasks.add(tsk);
                }
            }
        }
        addToScheduler();
        return modTasks;
    }
    public void deleteTask(Task task){
        if(task.getOneTimeJob()){
            if(task.getBoardTaskId()>0){
                boardTaskService.deleteBoardTask(task.getBoardTaskId());
            }
            service.delete(task);
        }
        if(task.getSchedule()!=null){
            //task.setActive(false);
            //service.save(task);
        }
        //addToScheduler();   
    }
    // task successful
    //@Async
    @Transactional
    public List<Task> taskComplete(Task task,Device device,Route route,Mode mode){
        List<Task> nextScheduledTasks=new ArrayList<>();
        LocalDateTime dt=LocalDateTime.now();
        if(task.getSchedule()!=null){
            if(task.getSchedule().getStartup()){
                task.setActive(false);
                task=service.save(task);
            }
            System.out.println(task.getApplication());
            System.out.println(task.getScheduledTime());
            if(task.getSchedule().getRepeatTask()&&dt.isAfter(task.getScheduledTime())){
                nextScheduledTasks.addAll(setTaskSchedule(task,false));
            }else if(task.getSchedule().getRepeatTask()){
                nextScheduledTasks.add(task);
            }
            //task.getSchedule().getDevice().setState(state);
            //task.getSchedule().getDevice().setWarning(warning);
        }else if(task.getOneTimeJob()&&task.getSchedule()==null){
            deleteTask(task);
        }
        return nextScheduledTasks;
    }
    // task failed
    public Task taskFailed(Task task,Device device){

        if(task.getOneTimeJob()&&task.getSchedule()==null){
            LocalDateTime currentDt=LocalDateTime.now();
            LocalDateTime scheduled=task.getScheduledTime();
            // check if there an 2 min or more diffence
            long minDiff=Math.abs(Duration.between(currentDt, scheduled).toMinutes());
            if(minDiff>=2){
                deleteTask(task);
                task=null;
            }else{
                //addTaskToScheduler(task);
                //task.setActive(true);
                //service.save(task);
                //addToScheduler();
            }
        }else if(task.getSchedule()!=null){
            // decide to deactive automated tasks if restart timeout enabled on board
            task=service.findById(task.getId()).get();
            boolean state=true;
            Board boardSett=device.getBoard();
            
            if(boardSett.getRestartTimeout()){
                LocalDateTime currentDt=LocalDateTime.now();
                long milDiff=Math.abs(Duration.between(currentDt, task.getScheduledTime()).toMillis());
                if(milDiff>=boardSett.getTimeout()){
                    state=false;
                }
            }
            task.setActive(state);
            task=service.save(task);
            //addToScheduler();
        }
        return task;
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
                    LocalDateTime schedule=addDuration(task.getSchedule().getTime());
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
            service.save(task);
            addToScheduler();
        }else
        {
            deviceService.updateDeviceAfterAction(complete,complete.getDevice());
            service.save(task);
            deleteTask(task);
        }
    }
    // delete all task in queue and running
    public void masterDelete(){
        List<Task> list=service.getAllTaskAct(true);
        for(int i=0; i<list.size(); i++){
            Task update=list.get(i);
            update.setActive(false);
            service.save(update);
        }
        //service.deleteAll();
        addToScheduler();
        sche.clearRunningTask();
    }
    public void clearQueue(){
        service.deleteAll();
        sche.clearRunningTask();
    }
    public Optional<Task> getTask(long id){
        return service.findById(id);
    }
    /* 
    public Task getTask(long id){
        return service.findById(id).get();
    }
        */
    // find all task in database
    public List<Task> getAllTask(){
        return service.findAll();
    }
    // find task by active
    public List<Task> getAllTaskStat(boolean status){
        return service.getAllTaskAct(status);
    }
    public List<Task> getAllRunningTask(){
        List<Task> list=sche.getAllRunTask();
        return list;
    }
    public boolean checkCurrentRun(){
        return sche.checkRun();
    }
    // add task to scheduler
    public boolean addToScheduler(){
        boolean results=false;
        List <Task> list=service.getAllTaskAct(true);
        if(list.size()>0) results=true;
        sche.addToQueue(list);
        return results;
    }
    public void addTaskToScheduler(Task task){
        sche.addTaskToQueue(task);
    }
    public boolean checkNextTask(Instant datetime,long boardId,long taskId){
        boolean res=false;
        long diff=60;
        Instant minPast=datetime.minusSeconds(diff);
        Instant ahead=datetime.plusSeconds(diff);
        String query="Select count(id) from scheduler.task where scheduledTime BETWEEN "+quoteParam(minPast.toString())+" and "+quoteParam(datetime.toString());
        query+=" and active=true and board="+boardId;
        if(taskId>0){
            query+=" and id!="+taskId;
        }
        query+=" or scheduledTime BETWEEN "+quoteParam(datetime.toString())+" and "+quoteParam(ahead.toString());
        query+=" and active=true and board="+boardId;
        if(taskId>0){
            query+=" and id!="+taskId;
        }
        int output=getDataInt(query);
        if(output>0) res=true;
        return res;
    }
    // schedule time for task
    private LocalDateTime addDuration(long time){
        LocalDateTime currenDateTime = LocalDateTime.now();
        long sec=currenDateTime.getSecond();
        long nano=currenDateTime.getNano();
        // setting for precise timing
        //currenDateTime=currenDateTime.minusSeconds(sec).minusNanos(nano);
        currenDateTime=currenDateTime.plus(time, ChronoUnit.MILLIS);
        return currenDateTime;
    }
}
