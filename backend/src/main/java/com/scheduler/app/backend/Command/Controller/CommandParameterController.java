package com.scheduler.app.backend.Command.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scheduler.Base.Service.ControllerBaseService;
import com.scheduler.app.backend.Command.Models.CommandParameter;
import com.scheduler.app.backend.Command.Service.CommandParameterService;

@RestController
@RequestMapping(value = "/command-parameter")
public class CommandParameterController extends ControllerBaseService<Long, CommandParameter> {
    @Autowired
    private CommandParameterService service;
    public CommandParameterController(){
        this.objectClass=this.pathBase+".Command.Models.CommandParameter";
    }
    @GetMapping(value={"/get-electrodes", "/get-electodes"})
    public String[] getElectrodes(){
        return service.getElectrodes();
    }
    
    
}   
