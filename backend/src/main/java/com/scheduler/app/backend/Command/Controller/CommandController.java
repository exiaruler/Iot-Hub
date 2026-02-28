package com.scheduler.app.backend.Command.Controller;

import java.util.List;

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
import com.scheduler.app.backend.Command.Models.Command;
import com.scheduler.app.backend.Command.Service.CommandService;
import com.scheduler.app.backend.Messaging.Models.BoardTask;


@RestController
@RequestMapping(value = "/command")
public class CommandController extends ControllerBase{
    @Autowired
    private CommandService commandService;
    public CommandController(){
        this.objectClass=this.pathBase+".Command.Models.Command";
    }
    @GetMapping("/get-commands")
    public List<Command> getCommands() {
        return commandService.getCommands();
    }
    @GetMapping("/new-task-record-complete")
    public BoardTask newTaskRecord(){
        return commandService.newRecordComplete();
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCommand(@PathVariable long id){
        commandService.deleteCommand(id);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/init")
    public void initDataSystem() {
        commandService.initDataSystem();
    }
    public String postMethodName(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
    @DeleteMapping("/restart")
    public void restart(){
        commandService.restartCommands();
    }
}
