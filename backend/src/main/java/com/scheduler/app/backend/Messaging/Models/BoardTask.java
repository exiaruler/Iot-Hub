package com.scheduler.app.backend.Messaging.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.scheduler.Base.ModelBase.TaskModelBase;
import com.scheduler.app.backend.Command.Models.Command;
import com.scheduler.app.backend.aREST.Models.Mode;
import com.scheduler.app.backend.aREST.Models.Route;
// background task data structure in device
@Entity
@Table(name="board_task")
public class BoardTask extends TaskModelBase {
    // task id (taskId|deviceId|date(day,month,year)|time(hour,minute,second))
    @Column
    private long taskId;
    // type of task and name of task
    @Column
    private String task="";
    // method name
    @Column
    private String method="";
    // param
    @Column
    private String param="";
    // GPIO pin
    @Column
    private int pin=-1;
    // GPIO pins array (max is 10)
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "boardTask", cascade =CascadeType.ALL)
    @JsonManagedReference("boardtask-pins")
    private List<BoardPin> pins=new ArrayList<>();
    // pins used
    @Column
    private int pinsUsed; 
    // current input
    @OneToMany(mappedBy = "boardTaskInput", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("boardvariable-input")
    private List<InputCurrent> input=new ArrayList<>();
    //current output
    @OneToMany(mappedBy = "boardTaskOutput", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("boardvariable-output")
    private List<OutputCurrent> output=new ArrayList<>();
    // RGB used in task
    @Column
    private boolean rgb=false;
    // servo move actions
    // start angle
    @Column
    private int startAngle;
    // angle to move to
    @Column
    private int moveAngle;
    // how many times to move
    @Column
    private int loops;
    // servo move delay at the start
    @Column
    private int beginDelay;
    //
    // delay interval
    @Column
    private long delayInterval=100;
    // RGB output for red
    @Column
    private int rgbRed=0;
    // RGB output for green
    @Column
    private int rgbGreen=0;
    //RGB output for blue
    @Column
    private int rgbBlue=0;
    // RGB type
    @Column
    private String rgbType;
    // deduction of current
    @Column
    private int deduction=5;
    // background run target
    @Column
    private int runTarget=0;
    // target angle
    @Column
    private int targetAngle=-1;
    // background task active
    @Column
    private boolean status=true;
    // next background task to enable
    @Column
    private int nextTask=-1;
    // http request to retrieve the next task
    @Column
    private boolean requestNext=false;
    // task that run in the sysQueue
    @Column
    private boolean systemTask=false;
    // background variables
    @JsonManagedReference("boardtask-variable")
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "task", cascade = CascadeType.ALL)
    private BoardVariable variable;
    // route
    @JsonBackReference("boardtask-route")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "route_id",referencedColumnName = "id")
    private Route route;
    // mode
    @JsonBackReference("boardtask-mode")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mode_id",referencedColumnName = "id")
    private Mode mode;
    // command associated
    @JsonBackReference("command-boardtask")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "command_id",referencedColumnName = "id")
    private Command command;

    public void initTaskId(long id){
        this.setTaskId(taskIdGenerate(id));
    }
    public BoardTask() {
        long deviceId=0;
        if(this.getMode()!=null){
            deviceId=this.getMode().getRoute().getDevice().getBoard().getId();
        }
        if(this.getRoute()!=null){
            deviceId=this.getRoute().getDevice().getBoard().getId();
        }
        this.setTaskId(taskIdGenerate(deviceId));
    }

    public BoardTask(BoardTask boardTask) {
        this.taskId = boardTask.taskId;
        this.task = boardTask.task;
        this.method = boardTask.method;
        this.param = boardTask.param;
        this.pin = boardTask.pin;
        this.pins = boardTask.pins;
        this.pinsUsed = boardTask.pinsUsed;
        this.input = boardTask.input;
        this.output = boardTask.output;
        this.rgb = boardTask.rgb;
        this.startAngle = boardTask.startAngle;
        this.moveAngle = boardTask.moveAngle;
        this.loops = boardTask.loops;
        this.beginDelay = boardTask.beginDelay;
        this.delayInterval = boardTask.delayInterval;
        this.rgbRed = boardTask.rgbRed;
        this.rgbGreen = boardTask.rgbGreen;
        this.rgbBlue = boardTask.rgbBlue;
        this.rgbType = boardTask.rgbType;
        this.deduction = boardTask.deduction;
        this.runTarget = boardTask.runTarget;
        this.targetAngle = boardTask.targetAngle;
        this.status = boardTask.status;
        this.nextTask = boardTask.nextTask;
        this.requestNext = boardTask.requestNext;
        this.systemTask = boardTask.systemTask;
        this.variable = boardTask.variable;
        this.route = boardTask.route;
        this.mode = boardTask.mode;
        this.command = boardTask.command;
    }

    public long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getTask() {
        return this.task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParam() {
        return this.param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public int getPin() {
        return this.pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public List<BoardPin> getPins() {
        return this.pins;
    }

    public void setPins(List<BoardPin> pins) {
        this.pins = pins;
    }

    public int getPinsUsed() {
        return this.pinsUsed;
    }

    public void setPinsUsed(int pinsUsed) {
        this.pinsUsed = pinsUsed;
    }

    public List<InputCurrent> getInput() {
        return this.input;
    }

    public void setInput(List<InputCurrent> input) {
        this.input = input;
    }

    public List<OutputCurrent> getOutput() {
        return this.output;
    }

    public void setOutput(List<OutputCurrent> output) {
        this.output = output;
    }

    public boolean isRgb() {
        return this.rgb;
    }

    public boolean getRgb() {
        return this.rgb;
    }

    public void setRgb(boolean rgb) {
        this.rgb = rgb;
    }

    public int getStartAngle() {
        return this.startAngle;
    }

    public void setStartAngle(int startAngle) {
        this.startAngle = startAngle;
    }

    public int getMoveAngle() {
        return this.moveAngle;
    }

    public void setMoveAngle(int moveAngle) {
        this.moveAngle = moveAngle;
    }

    public int getLoops() {
        return this.loops;
    }

    public void setLoops(int loops) {
        this.loops = loops;
    }

    public int getBeginDelay() {
        return this.beginDelay;
    }

    public void setBeginDelay(int beginDelay) {
        this.beginDelay = beginDelay;
    }

    public long getDelayInterval() {
        return this.delayInterval;
    }

    public void setDelayInterval(long delayInterval) {
        this.delayInterval = delayInterval;
    }

    public int getRgbRed() {
        return this.rgbRed;
    }

    public void setRgbRed(int rgbRed) {
        this.rgbRed = rgbRed;
    }

    public int getRgbGreen() {
        return this.rgbGreen;
    }

    public void setRgbGreen(int rgbGreen) {
        this.rgbGreen = rgbGreen;
    }

    public int getRgbBlue() {
        return this.rgbBlue;
    }

    public void setRgbBlue(int rgbBlue) {
        this.rgbBlue = rgbBlue;
    }

    public String getRgbType() {
        return this.rgbType;
    }

    public void setRgbType(String rgbType) {
        this.rgbType = rgbType;
    }

    public int getDeduction() {
        return this.deduction;
    }

    public void setDeduction(int deduction) {
        this.deduction = deduction;
    }

    public int getRunTarget() {
        return this.runTarget;
    }

    public void setRunTarget(int runTarget) {
        this.runTarget = runTarget;
    }

    public int getTargetAngle() {
        return this.targetAngle;
    }

    public void setTargetAngle(int targetAngle) {
        this.targetAngle = targetAngle;
    }

    public boolean isStatus() {
        return this.status;
    }

    public boolean getStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getNextTask() {
        return this.nextTask;
    }

    public void setNextTask(int nextTask) {
        this.nextTask = nextTask;
    }

    public boolean isRequestNext() {
        return this.requestNext;
    }

    public boolean getRequestNext() {
        return this.requestNext;
    }

    public void setRequestNext(boolean requestNext) {
        this.requestNext = requestNext;
    }

    public boolean isSystemTask() {
        return this.systemTask;
    }

    public boolean getSystemTask() {
        return this.systemTask;
    }

    public void setSystemTask(boolean systemTask) {
        this.systemTask = systemTask;
    }

    public BoardVariable getVariable() {
        return this.variable;
    }

    public void setVariable(BoardVariable variable) {
        this.variable = variable;
    }

    public Route getRoute() {
        return this.route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Mode getMode() {
        return this.mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Command getCommand() {
        return this.command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public BoardTask taskId(long taskId) {
        setTaskId(taskId);
        return this;
    }

    public BoardTask task(String task) {
        setTask(task);
        return this;
    }

    public BoardTask method(String method) {
        setMethod(method);
        return this;
    }

    public BoardTask param(String param) {
        setParam(param);
        return this;
    }

    public BoardTask pin(int pin) {
        setPin(pin);
        return this;
    }

    public BoardTask pins(List<BoardPin> pins) {
        setPins(pins);
        return this;
    }

    public BoardTask pinsUsed(int pinsUsed) {
        setPinsUsed(pinsUsed);
        return this;
    }

    public BoardTask input(List<InputCurrent> input) {
        setInput(input);
        return this;
    }

    public BoardTask output(List<OutputCurrent> output) {
        setOutput(output);
        return this;
    }

    public BoardTask rgb(boolean rgb) {
        setRgb(rgb);
        return this;
    }

    public BoardTask startAngle(int startAngle) {
        setStartAngle(startAngle);
        return this;
    }

    public BoardTask moveAngle(int moveAngle) {
        setMoveAngle(moveAngle);
        return this;
    }

    public BoardTask loops(int loops) {
        setLoops(loops);
        return this;
    }

    public BoardTask beginDelay(int beginDelay) {
        setBeginDelay(beginDelay);
        return this;
    }

    public BoardTask delayInterval(long delayInterval) {
        setDelayInterval(delayInterval);
        return this;
    }

    public BoardTask rgbRed(int rgbRed) {
        setRgbRed(rgbRed);
        return this;
    }

    public BoardTask rgbGreen(int rgbGreen) {
        setRgbGreen(rgbGreen);
        return this;
    }

    public BoardTask rgbBlue(int rgbBlue) {
        setRgbBlue(rgbBlue);
        return this;
    }

    public BoardTask rgbType(String rgbType) {
        setRgbType(rgbType);
        return this;
    }

    public BoardTask deduction(int deduction) {
        setDeduction(deduction);
        return this;
    }

    public BoardTask runTarget(int runTarget) {
        setRunTarget(runTarget);
        return this;
    }

    public BoardTask targetAngle(int targetAngle) {
        setTargetAngle(targetAngle);
        return this;
    }

    public BoardTask status(boolean status) {
        setStatus(status);
        return this;
    }

    public BoardTask nextTask(int nextTask) {
        setNextTask(nextTask);
        return this;
    }

    public BoardTask requestNext(boolean requestNext) {
        setRequestNext(requestNext);
        return this;
    }

    public BoardTask systemTask(boolean systemTask) {
        setSystemTask(systemTask);
        return this;
    }

    public BoardTask variable(BoardVariable variable) {
        setVariable(variable);
        return this;
    }

    public BoardTask route(Route route) {
        setRoute(route);
        return this;
    }

    public BoardTask mode(Mode mode) {
        setMode(mode);
        return this;
    }

    public BoardTask command(Command command) {
        setCommand(command);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof BoardTask)) {
            return false;
        }
        BoardTask boardTask = (BoardTask) o;
        return taskId == boardTask.taskId && Objects.equals(task, boardTask.task) && Objects.equals(method, boardTask.method) && Objects.equals(param, boardTask.param) && pin == boardTask.pin && Objects.equals(pins, boardTask.pins) && pinsUsed == boardTask.pinsUsed && Objects.equals(input, boardTask.input) && Objects.equals(output, boardTask.output) && rgb == boardTask.rgb && startAngle == boardTask.startAngle && moveAngle == boardTask.moveAngle && loops == boardTask.loops && beginDelay == boardTask.beginDelay && delayInterval == boardTask.delayInterval && rgbRed == boardTask.rgbRed && rgbGreen == boardTask.rgbGreen && rgbBlue == boardTask.rgbBlue && Objects.equals(rgbType, boardTask.rgbType) && deduction == boardTask.deduction && runTarget == boardTask.runTarget && targetAngle == boardTask.targetAngle && status == boardTask.status && nextTask == boardTask.nextTask && requestNext == boardTask.requestNext && systemTask == boardTask.systemTask && Objects.equals(variable, boardTask.variable) && Objects.equals(route, boardTask.route) && Objects.equals(mode, boardTask.mode) && Objects.equals(command, boardTask.command);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, task, method, param, pin, pins, pinsUsed, input, output, rgb, startAngle, moveAngle, loops, beginDelay, delayInterval, rgbRed, rgbGreen, rgbBlue, rgbType, deduction, runTarget, targetAngle, status, nextTask, requestNext, systemTask, variable, route, mode, command);
    }

    @Override
    public String toString() {
        return "{" +
            " taskId='" + getTaskId() + "'" +
            ", task='" + getTask() + "'" +
            ", method='" + getMethod() + "'" +
            ", param='" + getParam() + "'" +
            ", pin='" + getPin() + "'" +
            ", pins='" + getPins() + "'" +
            ", pinsUsed='" + getPinsUsed() + "'" +
            ", input='" + getInput() + "'" +
            ", output='" + getOutput() + "'" +
            ", rgb='" + isRgb() + "'" +
            ", startAngle='" + getStartAngle() + "'" +
            ", moveAngle='" + getMoveAngle() + "'" +
            ", loops='" + getLoops() + "'" +
            ", beginDelay='" + getBeginDelay() + "'" +
            ", delayInterval='" + getDelayInterval() + "'" +
            ", rgbRed='" + getRgbRed() + "'" +
            ", rgbGreen='" + getRgbGreen() + "'" +
            ", rgbBlue='" + getRgbBlue() + "'" +
            ", rgbType='" + getRgbType() + "'" +
            ", deduction='" + getDeduction() + "'" +
            ", runTarget='" + getRunTarget() + "'" +
            ", targetAngle='" + getTargetAngle() + "'" +
            ", status='" + isStatus() + "'" +
            ", nextTask='" + getNextTask() + "'" +
            ", requestNext='" + isRequestNext() + "'" +
            ", systemTask='" + isSystemTask() + "'" +
            ", variable='" + getVariable() + "'" +
            ", route='" + getRoute() + "'" +
            ", mode='" + getMode() + "'" +
            ", command='" + getCommand() + "'" +
            "}";
    }
    
}