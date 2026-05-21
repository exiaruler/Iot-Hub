package com.scheduler.app.backend.aREST.Controller;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scheduler.Base.ControllerBase;
import com.scheduler.app.backend.aREST.Models.Schedule;
import com.scheduler.app.backend.aREST.Service.ScheduleService;



@RestController
@RequestMapping(value = "/schedule")
public class ScheduleController extends ControllerBase {
    @Autowired
    private ScheduleService service;

    public ScheduleController() {
        this.objectClass="com.scheduler.app.backend.aREST.Models.Schedule";
    }

    @PostMapping("/add-schedule-socket")
    public ResponseEntity<Schedule> addSchedule(@RequestBody Schedule payload) {
        Schedule add=service.addScheduleSocket(payload);
        return ResponseEntity.ok(add);
    }
    
    @PutMapping("update-schedule/{id}")
    public ResponseEntity<Schedule>  updateSchedule(@PathVariable long id, @RequestBody Schedule payload) {
        Schedule update=service.updatScheduleSocket(id, payload);
        if(update==null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(update);
    }
    @GetMapping("/get-schedules")
    public List<Schedule> getAllSchedule() {
        return service.getAllSchedule();
    }
    @GetMapping("/get-schedule/{id}")
    public ResponseEntity<Schedule> getSchedule(@PathVariable long id) {
        Schedule schedule=service.getSchedule(id);
        return ResponseEntity.ok(schedule);
    }
    @DeleteMapping("/delete-schedule/{id}")
    public ResponseEntity<?> deleteSchedule(@PathVariable long id){
        boolean del=service.deleteSchedule(id);
        if(del){
        }
        return ResponseEntity.ok("deleted");
    }
    
    @PostMapping("/startup")
    public boolean startup(@RequestBody Map<String, Object> entity) {
        boolean success=false;
        long id=(int) entity.get("id");
        success=service.startupTask(id);
        return success;
    }
    // boot route from device
    @PostMapping("/device-startup")
    public boolean startupDevice(@RequestBody Map<String, Object> entity) {
        boolean success=false;
        String id=(String) entity.get("id");
        int intId=Integer.valueOf(id);
        success=service.startupTask(intId);
        return success;
    }
    @PostMapping("/test-task")
    public boolean testTask(@RequestBody Map<String, Object> entity) {
        boolean success=false;
        long id=(int) entity.get("id");
        success=service.testTask(id);
        return success;
    }

    
    
}
