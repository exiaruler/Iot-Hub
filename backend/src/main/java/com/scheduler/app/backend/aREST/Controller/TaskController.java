package com.scheduler.app.backend.aREST.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scheduler.Base.ControllerBase;
import com.scheduler.app.backend.aREST.Models.Route;
import com.scheduler.app.backend.aREST.Models.Task;
import com.scheduler.app.backend.aREST.Service.TaskService;

@RestController
@RequestMapping(value = "/task")
public class TaskController extends ControllerBase{
   
    @Autowired
    private TaskService service;
    @PostMapping(value="/addtask")
    public Task addTask(@RequestBody Map<String, Object> payload){
        String application=(String) payload.get("application");
        int boardId=(int) payload.get("board");
        int deviceId=(int) payload.get("deviceId");
        String url=(String) payload.get("url");
        String section=(String) payload.get("section");
        Task task=new Task();
        task.setApplication(application);
        task.setBoard(boardId);
        task.setDeviceId(deviceId);
        task.setUrl(url);
        task.setSection(section);
        return service.addTask(task);
    }
    @PostMapping("/test-function")
    public Task testRouteFunction(@RequestBody Route entity) {
        //TODO: process POST request
        
        return null;
    }
    
    @GetMapping(value="/get-task/{id}")
    public Optional<Task> getTask(@PathVariable long id){
        return service.getTask(id);
    }
    @GetMapping(value="/getalltask")
    public List<Task> getAllTask(){
        return service.getAllTask();
    }
    @GetMapping(value="/get-all-task-bystatus/{status}")
    public List<Task> getAllTaskAct(@PathVariable boolean status){
        return service.getAllTaskStat(status);
    }
    @GetMapping(value="/get-all-run-task")
    public List<Task> getAllRunningTask(){
        return service.getAllRunningTask();
    }
    // use in frontend to determine to update UI
    @GetMapping(value="/check-running-queue")
    public boolean checkRun(){
        return service.checkCurrentRun();
    }
    @DeleteMapping(value="/delete-task/{id}")
    public void deleteTask(@PathVariable long id){
        Task task=service.getTask(id).get();
        if(task!=null){
            service.deleteTask(task);
        }
    }
    @GetMapping(value = "/master-clear")
    public String masterClear(){
        service.masterDelete();
        return "Everything clear";
    }
    @GetMapping(value = "/task-clear")
    public String clearTasks(){
        service.clearQueue();
        return "tasks clear";
    }
    // route to start device
    @GetMapping(value="/start-device/{device}")
    public void addTaskFromRequestStart(@PathVariable String device){

    }
    // route to for device to send request during operation
    @GetMapping(value="/send-device/{device}")
    public void sendRequestDevice(@PathVariable String device){

    }
}
