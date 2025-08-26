package com.scheduler.app.backend.Command.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheduler.Base.Base;
import com.scheduler.app.backend.Command.CommandFunction;
import com.scheduler.app.backend.Command.Models.Command;
import com.scheduler.app.backend.Command.Repo.CommandRepo;
import com.scheduler.app.backend.Hardware.Service.HardwareService;
import com.scheduler.app.backend.Messaging.Models.BoardPin;
import com.scheduler.app.backend.Messaging.Models.BoardTask;
import com.scheduler.app.backend.Messaging.Models.BoardVariable;
import com.scheduler.app.backend.Messaging.Models.Brightness;
import com.scheduler.app.backend.Messaging.Models.InputCurrent;
import com.scheduler.app.backend.Messaging.Models.OutputCurrent;

@Configuration
@Service
public class CommandService extends Base {
    public final CommandRepo command;
    public CommandFunction function=new CommandFunction();
    public final CommandParameterService commandParaService;
    public final HardwareService hardwareService;
    private final ObjectMapper objectMapper;
    
    //public TaskService taskService;
    public CommandService(CommandRepo command,CommandParameterService commandParaService,HardwareService hardwareService, ObjectMapper objectMapper) {
        this.command = command;
        this.commandParaService=commandParaService;
        this.hardwareService=hardwareService;
        this.objectMapper = objectMapper;
    }
    public Command getCommand(long id){
        return command.getReferenceById(id);
    }
    private void initDataSystem(){
        List<Command> jsonCom=new ArrayList<>();
        try {
            // Load the JSON file from resources
            ClassPathResource resource = new ClassPathResource("json/commandsSystem.json");
            // Deserialize JSON array into a List
            jsonCom=objectMapper.readValue(resource.getInputStream(),new TypeReference<List<Command>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for(int i=0; i<jsonCom.size(); i++){
            Command commandItem=jsonCom.get(i);
            Command exist=command.findCommand(commandItem.getCommandType(),commandItem.getCommand(),commandItem.getSystemCommand());
            if(exist==null) exist=new Command();
            Optional<Command> rec=command.findById(exist.getId());
            if(commandItem!=null&&!rec.isPresent()){
                commandItem.setTotalParam(commandItem.getTotalParam());
                if(commandItem.getCommandParameter().size()>0){
                    commandItem.setParams(true);
                }else commandItem.setParams(false);
                Command save=command.save(commandItem);

            }
        }

    }
    //@Bean(initMethod="init")
    @PostConstruct
    @Transactional
    public void initData(){
        initDataSystem();
        List<Command> jsonCom=new ArrayList<>();
        try {
            // Load the JSON file from resources
            ClassPathResource resource = new ClassPathResource("json/commands.json");
            // Deserialize JSON array into a List
            jsonCom=objectMapper.readValue(resource.getInputStream(),new TypeReference<List<Command>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for(int i=0; i<jsonCom.size(); i++){
                Command commandItem=jsonCom.get(i);
                Command exist=command.findCommand(commandItem.getCommandType(),commandItem.getCommand(),commandItem.getSystemCommand());
                if(exist==null) exist=new Command();
                Optional<Command> rec=command.findById(exist.getId());
                if(commandItem!=null&&!rec.isPresent()){
                    commandItem.setTotalParam(commandItem.getTotalParam());
                    if(commandItem.getCommandParameter().size()>0){
                        commandItem.setParams(true);
                    }else commandItem.setParams(false);
                    Command save=command.save(commandItem);

                }else if(rec.isPresent()){
                    /* 
                    Command recValid=rec.get();
                    List <CommandParameter> existingParaList=exist.getCommandParameter();
                    exist.setTotalParam(commandItem.getTotalParam());
                    if(commandItem.getCommandParameter().size()>0){
                        exist.setParams(true);
                    }else exist.setParams(false);
                    exist.setCommandType(commandItem.getCommandType());
                    exist.setCommand(commandItem.getCommand());
                    exist.setHasMotor(commandItem.getHasMotor());
                    exist.setBoardCommand(commandItem.getBoardCommand());
                    List <CommandParameter> comparaList=commandItem.getCommandParameter();
                    List <Long> findIds=new ArrayList<>();
                    for(int x=0; x<comparaList.size(); x++){
                        CommandParameter para=comparaList.get(x);
                        String usedId=findIds.toString().replace("[","").replace("]", "");
                        String query="Select Id from scheduler.commandparameter where backgroundKey="+quoteParam(para.getBackgroundKey())+" and command_id="+exist.getId();
                        if(usedId!="")query+=" and id not in ("+usedId+")";
                        long existParaId=getDataLong(query);
                        if(existParaId>0){
                            CommandParameter existPara=existingParaList.stream().filter(commandParameter->commandParameter.getId()==existParaId).findFirst().orElse(null);
                            int index=existingParaList.indexOf(existPara);
                            existPara.setBackgroundKey(para.getBackgroundKey());
                            existPara.setClassName(para.getClassName());
                            existPara.setComponent(para.getComponent());
                            existPara.setPin(para.getPin());
                            existingParaList.set(index, existPara);
                            findIds.add(existParaId);   
                            
                        }else {
                            existingParaList.add(para);
                        }
                    }
                    exist.setCommandParameter(existingParaList);
                    command.save(exist);
                    */
                }

            }
        hardwareService.initData();
            
    }
    public List<Command> getCommands(){
        return command.findCommandBySystem(false);
    }
    public Command getCommandByCommand(String comm,String type,boolean system){
        Command com=command.findCommand(type, comm,system);
        if(com!=null&&com.getBoardCommand()!=null){
            BoardTask tsk=com.getBoardCommand();
            if( com.getBoardCommand().getPins() instanceof List==true)tsk.setPins(new ArrayList<>());
            if( com.getBoardCommand().getInput() instanceof List==true)tsk.setInput(new ArrayList<>());
            if( com.getBoardCommand().getOutput() instanceof List==true)tsk.setOutput(new ArrayList<>());
            com.setBoardCommand(tsk);
        }
        return com;
    }
    public void restartCommands(){
        command.deleteAll();
    }
    public BoardTask newRecordComplete(){
        BoardTask newTask=new BoardTask();
        BoardVariable variable=new BoardVariable();
        List <InputCurrent> input=new ArrayList<>();
        List <OutputCurrent> output=new ArrayList<>();
        List <Brightness> bright=new ArrayList<>();
        List <BoardPin> pins=new ArrayList<>();
        for(int i=1; i<=3; i++){
            InputCurrent in=new InputCurrent();
            in.setCurrent(0);
            input.add(in);
            OutputCurrent out=new OutputCurrent();
            out.setCurrent(0);
            output.add(out);
            Brightness bri=new Brightness();
            bri.setCurrent(0);
            bright.add(bri);
        }
        for(int i=1; i<=10; i++){
            BoardPin pin=new BoardPin();
            pin.setPin(0);
            pins.add(pin);
        }
        newTask.setInput(input);
        newTask.setOutput(output);
        variable.setBrightArray(bright);
        newTask.setPins(pins);
        newTask.setVariable(variable);
        return newTask;
    }
   
    
}
