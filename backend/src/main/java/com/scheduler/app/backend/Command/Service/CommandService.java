package com.scheduler.app.backend.Command.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheduler.Base.Base;
import com.scheduler.app.backend.Command.Models.Command;
import com.scheduler.app.backend.Command.Models.CommandParameter;
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
        return command.findById(id).get();
    }
    
    @Transactional
    public void initDataSystem(){
        String [] filepaths={"json/commandsSystem.json","json/commands.json"};
        List<Command> jsonCom=new ArrayList<>();
        try {
            // Load the JSON file from resources
            for(int x=0; x<filepaths.length; x++){
                String path=filepaths[x];
                ClassPathResource resource = new ClassPathResource(path);
                List<Command> extComs=objectMapper.readValue(resource.getInputStream(),new TypeReference<List<Command>>() {});
                if(extComs!=null){
                    jsonCom.addAll(extComs);
                }
            }
        if(jsonCom==null){
            return;
        }
        for(int i=0; i<jsonCom.size(); i++){
            Command commandItem=jsonCom.get(i);
            if(commandItem==null){
                continue;
            }
            Command exist=command.findCommand(commandItem.getCommandType(),commandItem.getCommand(),commandItem.getSystemCommand());
            if(exist!=null){
                    /* 
                    mergeCommand(exist, commandItem);
                    command.save(exist);
                    */
                }else{
                    prepareCommandForSave(commandItem);
                    command.save(commandItem);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Commands init");

    }

    private void mergeCommand(Command target, Command source){
        target.setDisplayName(source.getDisplayName());
        target.setClassName(source.getClassName());
        target.setParams(source.getParams());
        target.setTotalParam(source.getTotalParam());
        target.setHasMotor(source.getHasMotor());
        target.setSystemCommand(source.getSystemCommand());

        syncCommandParameters(target, source.getCommandParameter());
        syncBoardTask(target, source.getBoardCommand());
    }

    private void prepareCommandForSave(Command commandItem){
        commandItem.setId(0);
        syncCommandParameters(commandItem, commandItem.getCommandParameter());
        syncBoardTask(commandItem, commandItem.getBoardCommand());
    }

    private void syncCommandParameters(Command parentCommand, List<CommandParameter> sourceParams){
        if(parentCommand.getCommandParameter()==null){
            parentCommand.setCommandParameter(new ArrayList<>());
        }
        if(sourceParams==null){
            return;
        }

        List<CommandParameter> existingParams=parentCommand.getCommandParameter();
        if(existingParams==null){
            existingParams=new ArrayList<>();
            parentCommand.setCommandParameter(existingParams);
        }
        Map<String, CommandParameter> existingByKey=new HashMap<>();
        if(existingParams!=null){
            for(int i=0; i<existingParams.size(); i++){
                CommandParameter existingParam=existingParams.get(i);
                if(existingParam==null){
                    continue;
                }
                existingByKey.put(buildParamKey(existingParam), existingParam);
            }
        }

        for(int i=0; i<sourceParams.size(); i++){
            CommandParameter sourceParam=sourceParams.get(i);
            if(sourceParam==null){
                continue;
            }
            CommandParameter targetParam=existingByKey.get(buildParamKey(sourceParam));
            if(targetParam==null){
                sourceParam.setId(0);
                sourceParam.setCommand(parentCommand);
                existingParams.add(sourceParam);
                continue;
            }

            targetParam.setParameterOrder(sourceParam.getParameterOrder());
            targetParam.setComponent(sourceParam.getComponent());
            targetParam.setLabel(sourceParam.getLabel());
            targetParam.setType(sourceParam.getType());
            targetParam.setPin(sourceParam.getPin());
            targetParam.setBackgroundKey(sourceParam.getBackgroundKey());
            targetParam.setSubKey(sourceParam.getSubKey());
            targetParam.setClassName(sourceParam.getClassName());
            targetParam.setCommand(parentCommand);
        }
    }

    private String buildParamKey(CommandParameter parameter){
        if(parameter==null){
            return "";
        }
        return Objects.toString(parameter.getLabel(), "")+"|"+
                Objects.toString(parameter.getBackgroundKey(), "")+"|"+
                Objects.toString(parameter.getComponent(), "");
    }

    private void syncBoardTask(Command parentCommand, BoardTask sourceTask){
        if(sourceTask==null){
            return;
        }

        BoardTask targetTask=parentCommand.getBoardCommand();
        if(targetTask==null){
            sourceTask.setId(0);
            parentCommand.setBoardCommand(sourceTask);
            sourceTask.initTaskId(0);
            attachBoardTaskGraph(parentCommand, sourceTask);
            return;
        }

        targetTask.setTaskId(sourceTask.getTaskId());
        targetTask.setTask(sourceTask.getTask());
        targetTask.setMethod(sourceTask.getMethod());
        targetTask.setParam(sourceTask.getParam());
        targetTask.setPins(sourceTask.getPins());
        targetTask.setPinsUsed(sourceTask.getPinsUsed());
        targetTask.setInput(sourceTask.getInput());
        targetTask.setOutput(sourceTask.getOutput());
        targetTask.setRgb(sourceTask.getRgb());
        targetTask.setStartAngle(sourceTask.getStartAngle());
        targetTask.setMoveAngle(sourceTask.getMoveAngle());
        targetTask.setLoops(sourceTask.getLoops());
        targetTask.setBeginDelay(sourceTask.getBeginDelay());
        targetTask.setDelayInterval(sourceTask.getDelayInterval());
        targetTask.setDeduction(sourceTask.getDeduction());
        targetTask.setRunTarget(sourceTask.getRunTarget());
        targetTask.setTargetAngle(sourceTask.getTargetAngle());
        targetTask.setStatus(sourceTask.getStatus());
        targetTask.setNextTask(sourceTask.getNextTask());
        targetTask.setRequestNext(sourceTask.getRequestNext());
        targetTask.setSystemTask(sourceTask.getSystemTask());
        targetTask.setVariable(sourceTask.getVariable());
        attachBoardTaskGraph(parentCommand, targetTask);
    }

    private void attachBoardTaskGraph(Command parentCommand, BoardTask boardTask){
        boardTask.setCommand(parentCommand);

        if(boardTask.getPins()!=null){
            for(int i=0; i<boardTask.getPins().size(); i++){
                boardTask.getPins().get(i).setBoardTask(boardTask);
            }
        }

        if(boardTask.getInput()!=null){
            for(int i=0; i<boardTask.getInput().size(); i++){
                boardTask.getInput().get(i).setBoardTaskInput(boardTask);
            }
        }

        if(boardTask.getOutput()!=null){
            for(int i=0; i<boardTask.getOutput().size(); i++){
                boardTask.getOutput().get(i).setBoardTaskOutput(boardTask);
            }
        }

        if(boardTask.getVariable()!=null){
            boardTask.getVariable().setTask(boardTask);
            if(boardTask.getVariable().getBrightArray()!=null){
                for(int i=0; i<boardTask.getVariable().getBrightArray().size(); i++){
                    boardTask.getVariable().getBrightArray().get(i).setBoardVariable(boardTask.getVariable());
                }
            }
        }
    }
    public void initData(){
        initDataSystem();
    }
    public List<Command> getCommands(){
        return command.findCommandBySystem(false);
    }
    public Command getCommandByCommand(String comm,String type,boolean system){
        Command com=command.findCommand(type, comm,system);
        if(com!=null&&com.getBoardCommand()!=null){
            BoardTask tsk=com.getBoardCommand();
            if(tsk.getPins()!=null)tsk.setPins(new ArrayList<>());
            if(tsk.getInput()!=null)tsk.setInput(new ArrayList<>());
            if(tsk.getOutput()!=null)tsk.setOutput(new ArrayList<>());
            com.setBoardCommand(tsk);
        }
        return com;
    }
    public void deleteCommand(long id){
        command.deleteById(id);
        
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
