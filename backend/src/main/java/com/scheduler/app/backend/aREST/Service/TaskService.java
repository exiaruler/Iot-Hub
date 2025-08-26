package com.scheduler.app.backend.aREST.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private final CommandService commandService;
    public final BoardTaskService boardTaskService;
    public TaskService(TaskRepo service, ScheduleRepo serviceSch, DeviceService deviceService, CommandService commandService, BoardTaskService boardTaskService) {
        this.service = service;
        this.serviceSch = serviceSch;
        this.deviceService = deviceService;
        this.commandService = commandService;
        this.boardTaskService = boardTaskService;
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
    
    // schedule task to scheduler
    public List<Task> setTaskSchedule(Task task,boolean addToSchedule){
        List<Task> modTasks=new ArrayList<>();
        LocalDateTime scheDt=addDuration(task.getSchedule().getTime());
        task.setScheduledTime(scheDt);
        task.setActive(true);
        service.save(task);
        modTasks.add(task);
        // if task is a repeative send connection command to board
        if(task.getSchedule().getRepeatTask()){
            // add websocket connect command
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
            connectTask.setDeviceId(task.getDeviceId());
            connectTask.setBoardTaskId(add.getId());
            connectTask.setMotor(false);
            connectTask.setActive(true);
            service.save(connectTask);
            modTasks.add(connectTask);
        }
        if(addToSchedule)addToScheduler();
        return modTasks;
    }
    // delete 1 time tasks that are 1 time only when board start up
    public void purgeOldTasks(long boardId){
        // disabled automated and startup schedule tasks
        List<Long> autoTaskIds=service.getRoutineJobIds(boardId);
        if(autoTaskIds.size()>0){
            for(int x=0; x<autoTaskIds.size(); x++){
                Task tsk=service.findById(autoTaskIds.get(x)).get();
                if(tsk!=null){
                    tsk.active(false);
                    service.save(tsk);
                }
            }
        }
        addToScheduler();
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
        addToScheduler();
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
        boolean success=true;
        if(task.getSchedule()!=null){
            if(task.getSchedule().getStartup()){
                task.setActive(false);
                task=service.save(task);
            }
            if(task.getSchedule().getRepeatTask()){
                //task=setTaskSchedule(task,false);
                nextScheduledTasks.addAll(setTaskSchedule(task,false));
            }
            //addTaskToScheduler(task);
            //addToScheduler();
            success=true;
            //task.getSchedule().getDevice().setState(state);
            //task.getSchedule().getDevice().setWarning(warning);
        }else if(task.getOneTimeJob()&&task.getSchedule()==null){
            deleteTask(task);
            success=true;
        }
        return nextScheduledTasks;
    }
    // task failed
    public Task taskFailed(Task task){
        boolean success=true;
        if(task.getOneTimeJob()&&task.getSchedule()==null){
            LocalDateTime currentDt=LocalDateTime.now();
            LocalDateTime scheduled=task.getScheduledTime();
            // check if there an hour or diffence
            long hourDiff=Math.abs(Duration.between(currentDt, scheduled).toHours());
            if(hourDiff>=1){
                deleteTask(task);
                task=null;
                success=true;
            }else{
                //addTaskToScheduler(task);
                task.setActive(true);
                service.save(task);
                //addToScheduler();
                success=true;
            }
        }else if(task.getSchedule()!=null){
            //addTaskToScheduler(task);
            task.setActive(true);
            service.save(task);
            //addToScheduler();
            success=true;
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
        System.out.println(query);
        int output=getDataInt(query);
        if(output>0) res=true;
        return res;
    }
    // schedule time for task
    private LocalDateTime addDuration(long time){
        LocalDateTime currenDateTime = LocalDateTime.now();
        long sec=currenDateTime.getSecond();
        long nano=currenDateTime.getNano();
        currenDateTime.minusSeconds(sec).minusNanos(nano);
        currenDateTime=currenDateTime.plus(time, ChronoUnit.MILLIS);
        return currenDateTime;
    }
}
