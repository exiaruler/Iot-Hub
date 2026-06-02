package com.scheduler.app.backend.Messaging.Board.Models.ArraySerial;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scheduler.app.backend.Messaging.Board.Models.ArraySerial.ListSerial.SingleInputListSerializer;
import com.scheduler.app.backend.Messaging.Board.Models.ArraySerial.ListSerial.SingleOutputListSerializer;
import com.scheduler.app.backend.Messaging.Board.Models.ArraySerial.ListSerial.SinglePinListSerializer;
import com.scheduler.app.backend.Messaging.Models.BoardPin;
import com.scheduler.app.backend.Messaging.Models.BoardTask;
import com.scheduler.app.backend.Messaging.Models.InputCurrent;
import com.scheduler.app.backend.Messaging.Models.OutputCurrent;
@JsonPropertyOrder({"taskId","task","method","param","pinsUsed","pins","input","output","rgb","startAngle","moveAngle","loops","beginDelay","delayInterval","deduction","runTarget","targetAngle","status","systemTask"})
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public class BoardTaskSerial extends BoardTask{

    // serial output
    public BoardTaskSerial(BoardTask boardTask) {
        this.setTaskId(boardTask.getTaskId());
        this.setTask(boardTask.getTask());
        this.setMethod(boardTask.getMethod());
        this.setParam(boardTask.getParam());
        this.setPinsUsed(boardTask.getPinsUsed());
        this.setPins(boardTask.getPins());
        this.setInput(boardTask.getInput());
        this.setOutput(boardTask.getOutput());
        this.setRgb(boardTask.getRgb());
        this.setStartAngle(boardTask.getStartAngle());
        this.setMoveAngle(boardTask.getMoveAngle());
        this.setLoops(boardTask.getLoops());
        this.setBeginDelay(boardTask.getBeginDelay());
        this.setDelayInterval(boardTask.getDelayInterval());
        this.setDeduction(boardTask.getDeduction());
        this.setRunTarget(boardTask.getRunTarget());
        this.setTargetAngle(boardTask.getTargetAngle());
        this.setStatus(boardTask.getStatus());
        this.setSystemTask(boardTask.isSystemTask());
    }
    @Override
    public void setPins(List<BoardPin> pins) {
        List<BoardPin> serialPins = pins.stream()
            .map(BoardPinSerial::new)
            .collect(Collectors.toList());
            super.setPins(serialPins);
    }
   @Override
    @JsonSerialize(using = SinglePinListSerializer.class)
    public List<BoardPin> getPins() {
        return super.getPins();
    }
    @Override
    public void setOutput(List<OutputCurrent> outputs){
        List<OutputCurrent> serialPins = outputs.stream()
            .map(BoardOutputSerial::new)
            .collect(Collectors.toList());
            super.setOutput(serialPins);
    }

   @Override
    @JsonSerialize(using = SingleOutputListSerializer.class)
    public List<OutputCurrent> getOutput() {
        return super.getOutput();
    }
    @Override
    public void setInput(List<InputCurrent> inputs){
        List<InputCurrent> serialPins = inputs.stream()
            .map(BoardInputSerial::new)
            .collect(Collectors.toList());
            super.setInput(serialPins);
    }
    @Override
    @JsonSerialize(using = SingleInputListSerializer.class)
    public List<InputCurrent> getInput() {
        return super.getInput();
    }
  

    
   
    
    
}
