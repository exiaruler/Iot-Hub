package com.scheduler.app.backend.Command.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.scheduler.Base.Base;
import com.scheduler.app.backend.Command.Models.CommandParameter;
import com.scheduler.app.backend.Command.Repo.CommandParameterRepo;

@Service
public class CommandParameterService extends Base{
    public final CommandParameterRepo commandParameter;
    public CommandParameterService(CommandParameterRepo commandParameter) {
        this.commandParameter=commandParameter;
    }
    
    // electrode 
    public String electode[]={"anode","cathode"};

    public String rgbType[]={"COMMON_ANODE"};

    public CommandParameter getParameter(long id){
        return commandParameter.getReferenceById(id);
    }
    public CommandParameter getParameterInit(long id){
        return commandParameter.getParameter(id);
    }
    public List<CommandParameter> getParameterListInit(long id){
        return commandParameter.findParametersByCommand(id);
    }
    public CommandParameter saveParameter(CommandParameter rec){
        return commandParameter.save(rec);
    }

    

    
}
