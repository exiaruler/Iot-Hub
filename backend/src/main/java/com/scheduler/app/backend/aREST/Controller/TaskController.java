package com.scheduler.app.backend.aREST.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scheduler.Base.ControllerBase;
import com.scheduler.Base.ModelBase.TaskEventId;
import com.scheduler.app.backend.aREST.Models.Route;
import com.scheduler.app.backend.aREST.Models.Task;
import com.scheduler.app.backend.aREST.Service.TaskService;

@RestController
@RequestMapping(value = "/task")
public class TaskController extends ControllerBase{
   
    @Autowired
    private TaskService service;
    
    // run commands for board
    @PostMapping(value="/run-command/{board}/{action}/{command}/{system}")
    public ResponseEntity<String> runCommand(@PathVariable String board,@PathVariable String action,@PathVariable String command,@PathVariable boolean system) {
        String act=service.runCommand(board, command, action, system,false);
        return ResponseEntity.ok(act);
    }
    
    
    @GetMapping(value="/get-task/{id}")
    public Optional<Task> getTask(@PathVariable TaskEventId id){
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
    public void deleteTask(@PathVariable TaskEventId id){
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
