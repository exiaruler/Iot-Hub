package com.scheduler.app.backend.aREST.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.scheduler.Base.Base;
import com.scheduler.Base.ResourceNotFoundException;
import com.scheduler.Base.ModelBase.TaskEventId;
import com.scheduler.app.backend.aREST.Models.Board;
import com.scheduler.app.backend.aREST.Models.Device;
import com.scheduler.app.backend.aREST.Models.Mode;
import com.scheduler.app.backend.aREST.Models.Route;
import com.scheduler.app.backend.aREST.Models.Schedule;
import com.scheduler.app.backend.aREST.Models.Task;
import com.scheduler.app.backend.aREST.Repo.ScheduleRepo;

@Service
public class ScheduleService extends Base{
    private final ScheduleRepo scheRepo;
    public final TaskService taskService;
    public final DeviceService deviceService;
    public final RoutesService routeService;

    public ScheduleService(ScheduleRepo schedule, TaskService taskService, DeviceService deviceService, RoutesService routeService){
        this.scheRepo = schedule;
        this.taskService = taskService;
        this.deviceService = deviceService;
        this.routeService = routeService;
    }
    public List<Schedule> getAllSchedule(){
        return scheRepo.findAll();
    }
    public Schedule getSchedule(long id){
        return scheRepo.findById(id).get();
    }
    public boolean deleteSchedule(long id){
        Schedule rec=scheRepo.findById(id).get();
        boolean del=false;
        if(rec!=null){
            scheRepo.delete(rec);
            del=true;
        }else new ResourceNotFoundException("Schedule Record does not exist");
        return del;
    }
      
    public Task createTask(TaskEventId id,String application,String url,long routeId,long modeId,boolean hasMotor,Schedule schedule,Device device,Route route){
        Task tsk=null;
        if(id!=null&&schedule!=null){
            Task task=taskService.getTask(id).get();
            if(task!=null){
                if(device!=null){
                    task.setRouteId(routeId);
                    task.setModeId(modeId);
                    task.setMotor(hasMotor);  
                }else if(device==null){
                    task.setUrl(url);
                }
                tsk=task;
            }
        }else if(schedule!=null){   
            tsk=new Task();
            tsk.initId(device.getBoard().getId(),device.getId());
            tsk.oneTimeJob(false);
            tsk.setApplication(application);
            tsk.setSchedule(schedule);
            if(device!=null){
                tsk.setDeviceId(device.getId());
                tsk.setBoard(device.getBoard().getId());
                tsk.setRouteId(routeId);
                tsk.setModeId(modeId);
                tsk.setMotor(hasMotor);
                
            }else if(device==null){
                tsk.setUrl(url);
                tsk.setHttpTask(true);
            }
        }
        return tsk;
    }
    public Schedule addScheduleSocket(Schedule schedule){
        boolean hasMotor=false;
        if(schedule.getDeviceId()!=0&&schedule.getRouteId()!=0){
            Device device=deviceService.getDevice(schedule.getDeviceId());
            if(device!=null){
                List <Schedule> deviceSchList=new ArrayList<>();
                if(!device.getSchedules().isEmpty()){
                    deviceSchList=device.getSchedules();
                } 
                schedule.setDevice(device);
                Optional<Route> routeQuery=device.getRoutes().stream().filter(rec->rec.getId()==schedule.getRouteId()).findFirst();
                if(routeQuery.isPresent()){
                    Route rou=routeQuery.get();
                    schedule.setRoute(rou);
                    if(!schedule.getModeRandom()){
                        Optional<Mode> mode=rou.getMode().stream().filter(rec->rec.getId()==schedule.getModeId()).findFirst();
                        if(mode.isPresent()){
                            schedule.setMode(mode.get());
                            Task tsk=createTask(null,schedule.getName(),"",rou.getId(),mode.get().getId(),hasMotor,schedule,device,rou);
                            schedule.setTask(tsk);
                        }
                    }else
                    {
                        // if random mode is enabled select random mode
                        Mode mode=taskService.randomMode(rou.getMode());
                        Task tsk=createTask(null,schedule.getName(),"",rou.getId(),mode.getId(),hasMotor,schedule,device,rou);
                        schedule.setMode(null);
                        schedule.setTask(tsk);

                    }
                    scheRepo.save(schedule);
                    if(schedule.getRepeatTask()){
                        taskService.setTaskSchedule(schedule.getTask(),true);
                    }
                }

            }

        }
        return schedule;
    }
    public Schedule updatScheduleSocket(long id,Schedule schedule){
        Schedule existRec=scheRepo.findById(id).get();
        boolean hasMotor=false;
        if(existRec!=null){
            existRec.setName(schedule.getName());
            Device device=deviceService.getDevice(schedule.getDeviceId());
            existRec.setDevice(device);
            existRec.setModeRandom(schedule.getModeRandom());
            existRec.setStatus(schedule.getStatus());
            if(schedule.getStartup()&&schedule.getRepeatTask()) new ResourceNotFoundException("Schedule task cannot have startup and status enabled");
            existRec.setStartup(schedule.getStartup());
            existRec.setRepeatTask(schedule.getRepeatTask());
            Optional<Route> routeQuery=device.getRoutes().stream().filter(rec->rec.getId()==schedule.getRouteId()).findFirst();
            if(routeQuery.isPresent()){
                    Route rou=routeQuery.get();
                    existRec.setRoute(rou);
                    if(!schedule.getModeRandom()){
                        Optional<Mode> mode=rou.getMode().stream().filter(rec->rec.getId()==schedule.getModeId()).findFirst();
                        if(mode.isPresent()){
                            existRec.setMode(mode.get());
                            existRec.setModeId(schedule.getModeId());
                            Task tsk=existRec.getTask();
                            tsk.setActive(false);
                            tsk.setApplication(schedule.getName());
                            tsk.setRouteId(schedule.getRouteId());
                            tsk.setModeId(schedule.getModeId());
                            tsk.setMotor(hasMotor);
                            existRec.setTask(tsk);
                        }
                    }else
                    {
                        Mode mode=taskService.randomMode(rou.getMode());
                        Task tsk=existRec.getTask();
                        tsk.setActive(false);
                        tsk.setApplication(schedule.getName());
                        tsk.setRouteId(schedule.getRouteId());
                        tsk.setModeId(mode.getId());
                        tsk.setMotor(hasMotor);
                        existRec.setTask(tsk);
                        existRec.setModeId(0);
                        existRec.setMode(null);
                    }
                scheRepo.save(existRec);
                if(schedule.getRepeatTask()){
                    taskService.setTaskSchedule(existRec.getTask(),true);
                }  
            }
        }else new ResourceNotFoundException("Schedule Record does not exist");
        return existRec;
    }
    
    public Schedule addSchedule(String name,long time,boolean repeat,boolean startup,String url,long deviceId,long routeId,long modeId){
        Schedule scheduleTask=new Schedule();
        Task taskSche=new Task();
        boolean hasMotor=false;
        scheduleTask.setName(name);
        scheduleTask.setTime(time);
        // save task with device and route
        if(deviceId!=0&&routeId!=0){
            Device device=deviceService.getDevice(deviceId);
            List <Schedule> deviceSchList=new ArrayList<>();
            if(!device.getSchedules().isEmpty()){
                deviceSchList=device.getSchedules();
            } 
            scheduleTask.setDevice(device);
            if(startup){
                scheduleTask.setStartup(startup);
            }else
            {
                scheduleTask.setRepeatTask(repeat);
            }
            if(device!=null){
                List <Route> routeList=device.getRoutes();
                for(int i=0; i<routeList.size(); i++){
                    long id=routeList.get(i).getId();
                    if(routeId==id){
                        Route route=routeList.get(i);
                        scheduleTask.setRoute(route);
                        if(route.getCommand()!=null&&route.getCommand().getHasMotor()) hasMotor=true;
                        if(route.getModes()){
                            Mode mode=routeService.getMode(modeId);
                            if(mode!=null){
                                scheduleTask.setModeValue(mode.getMode());
                            }
                        }
                        // save task
                        taskSche=createTask(null,name,url,routeId,modeId,hasMotor,scheduleTask,device,route);
                        if(taskSche!=null) scheduleTask.setTask(taskSche);
                        deviceSchList.add(scheduleTask);
                        device.setSchedules(deviceSchList); 
                    }
                }
                
            }
        }else
        // save http task
        {
            if(startup){
                scheduleTask.setStartup(startup);
            }else
            {
                scheduleTask.setRepeatTask(repeat);
            }
            scheduleTask.setUrl(url);
            taskSche.setUrl(url);
            taskSche.setHttpTask(true);
            taskSche.application(name);
            taskSche.oneTimeJob(false);
            taskSche.setSchedule(scheduleTask);
            scheduleTask.setTask(taskSche);
        }
        scheRepo.save(scheduleTask);
        return scheduleTask;
    }
    public boolean startStartupSchedule(Board board){
        boolean exist=false;
        long [] devicesIds=deviceService.getDevicesById(board.getId());
        if(devicesIds.length>0){
            String devIdList=Arrays.toString(devicesIds).replace("[", "").replace("]", "");
            List<Long> activeStartups=scheRepo.getActiveStartupSchedules(devIdList);
            List<Long> activeRoutine=scheRepo.getActiveRoutineSchedules(devIdList);
            activeStartups.addAll(activeRoutine);
            for(int i=0; i<activeStartups.size(); i++){
                Long scheId=activeStartups.get(i);
                startupTask(scheId);
                exist=true;
            }
            taskService.addToScheduler();
        }
        return exist;
    }
    public Schedule updateScheduleTest(long id,Task task){
        Schedule sche=scheRepo.getReferenceById(id);
        if(sche!=null){
            sche.setTask(task);
            scheRepo.save(sche);
        }
        return sche;
    }
    // start task schedule start task
    public boolean startupTask(long id){
        boolean success=false;
        Schedule schedule=scheRepo.findById(id).get();
        if(schedule!=null){
            Task task=schedule.getTask();
            
            taskService.setTaskSchedule(task,false);
            success=true;
        }
        return success;
    }
    
    public boolean testTask(long id){
        boolean success=false;
        Schedule schedule=scheRepo.getReferenceById(id);
        if(schedule!=null){
            Task task=schedule.getTask();
            taskService.setTaskSchedule(task,true);
            success=true;
        }
        return success;
    }
    
}
