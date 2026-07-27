package com.scheduler.app.backend.aREST.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.scheduler.Base.Base;
import com.scheduler.Base.Exception.ValidationException;
import com.scheduler.Base.ModelBase.TaskEventId;
import com.scheduler.Base.Service.BaseService;
import com.scheduler.Base.ResourceNotFoundException;
import com.scheduler.app.backend.aREST.Models.Board;
import com.scheduler.app.backend.aREST.Models.Device;
import com.scheduler.app.backend.aREST.Models.Mode;
import com.scheduler.app.backend.aREST.Models.Route;
import com.scheduler.app.backend.aREST.Models.Schedule;
import com.scheduler.app.backend.aREST.Models.Task;
import com.scheduler.app.backend.aREST.Repo.ScheduleRepo;

@Service
public class ScheduleService extends BaseService<Schedule, Long> {
    @Autowired
    private ScheduleRepo scheRepo;
    public final TaskService taskService;
    public final DeviceService deviceService;
    public final RoutesService routeService;

    public ScheduleService(ScheduleRepo schedule, TaskService taskService, DeviceService deviceService, RoutesService routeService){
        this.scheRepo = schedule;
        this.taskService = taskService;
        this.deviceService = deviceService;
        this.routeService = routeService;
    }
    @Override
    protected JpaRepository<Schedule, Long> repository() {
        return scheRepo;
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
            //tsk.setParentTask(tsk.getId());
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
    @Override
    protected void beforeSave(Schedule entity, Map<String, String> errors, Map<String, String> warnings) {
        boolean existingSchedule = entity.getId() > 0 && scheRepo.existsById(entity.getId());

        if (entity.getStartup() && entity.getRepeatTask()) {
            errors.put("occurance", "Startup and repeat task cannot be enabled at the same time");
        }

        if (entity.getDeviceId() == 0 || entity.getRouteId() == 0) {
            errors.put("schedule", "A device and route are required for a socket schedule");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors, null);
        }

        Device device = deviceService.getDevice(entity.getDeviceId());
        entity.setDevice(device);

        Optional<Route> routeQuery = device.getRoutes().stream()
                .filter(route -> route.getId() == entity.getRouteId())
                .findFirst();
        if (!routeQuery.isPresent()) {
            errors.put("route", "The selected route does not belong to the selected device");
            throw new ValidationException(errors, null);
        }

        Route route = routeQuery.get();
        entity.setRoute(route);

        Mode selectedMode = null;
        if (!entity.getModeRandom()) {
            selectedMode = route.getMode().stream()
                    .filter(mode -> mode.getId() == entity.getModeId())
                    .findFirst()
                    .orElse(null);
            if (selectedMode == null) {
                errors.put("mode", "The selected mode does not belong to the selected route");
                throw new ValidationException(errors, null);
            }
            entity.setMode(selectedMode);
        } else {
            selectedMode = taskService.randomMode(route.getMode());
            entity.setMode(null);
            entity.setModeId(0);
        }

        Task task;
        if (existingSchedule) {
            Schedule persistedSchedule = scheRepo.findById(entity.getId()).get();
            task = persistedSchedule.getTask();
            task.setActive(false);
            task.setApplication(entity.getName());
            task.setRouteId(route.getId());
            task.setModeId(selectedMode.getId());
            task.setMotor(false);
        } else {
            task = createTask(null, entity.getName(), "", route.getId(), selectedMode.getId(), false,
                    entity, device, route);
        }
        task.setSchedule(entity);
        entity.setTask(task);
    }

    @Override
    protected void afterSave(Schedule entity) {
        if (entity.getRepeatTask()) {
            taskService.setTaskSchedule(entity.getTask(), true);
        }
    }
    public Schedule addScheduleSocket(Schedule schedule){
        return save(schedule);
    }
    public Schedule updatScheduleSocket(long id,Schedule schedule){
        if (!scheRepo.existsById(id)) {
            return null;
        }
        schedule.setId(id);
        return save(schedule);
    }
    /* 
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
    */
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
    
    public boolean startRoutineSchedule(Board board){
        boolean exist=false;
        long [] devicesIds=deviceService.getDevicesById(board.getId());
        if(devicesIds.length>0){
            String devIdList=Arrays.toString(devicesIds).replace("[", "").replace("]", "");
            List<Long> activeStartups=new ArrayList<>();
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
