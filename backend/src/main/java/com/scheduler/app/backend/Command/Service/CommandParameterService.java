package com.scheduler.app.backend.Command.Service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.scheduler.Base.Service.BaseService;
import com.scheduler.app.backend.Command.Models.CommandParameter;
import com.scheduler.app.backend.Command.Repo.CommandParameterRepo;

@Service
public class CommandParameterService extends BaseService<CommandParameter, Long> {
    public final CommandParameterRepo commandParameter;
    public CommandParameterService(CommandParameterRepo commandParameter) {
        this.commandParameter=commandParameter;
    }

    @Override
    protected JpaRepository<CommandParameter, Long> repository() {
        return commandParameter;
    }

    
    private static final String[] ELECTRODES = {"anode", "cathode"};

    public String[] getElectrodes() {
        return ELECTRODES.clone();
    }

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
