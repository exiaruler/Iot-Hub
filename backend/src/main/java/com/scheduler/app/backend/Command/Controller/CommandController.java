package com.scheduler.app.backend.Command.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    @PostMapping("/run-command/{board}/{commmand}/{system}")
    public String runCommand(@PathVariable String board,@PathVariable String command,@PathVariable boolean system) {
        //TODO: process POST request
        
        return "";
    }
    
    @GetMapping("/new-task-record-complete")
    public BoardTask newTaskRecord(){
        return commandService.newRecordComplete();
    }
    @DeleteMapping("/restart")
    public void restart(){
        commandService.restartCommands();
    }
}
