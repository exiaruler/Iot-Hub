package com.scheduler.app.backend.aREST.Models;
import java.time.Instant;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.scheduler.Base.ModelBase.BoardEventModelBase;
import com.scheduler.app.backend.aREST.Function.Models.Mode;
import com.scheduler.app.backend.aREST.Function.Models.Route;

// board queue- store board operations
@Entity
@Table(indexes = {
    @Index(name = "idx_board_task_id", columnList = "board,boardTaskId", unique = false)
})
public class BoardQueue extends BoardEventModelBase{
    // board
    @ManyToOne
    @JoinColumn(name="board")
    @JsonBackReference("queue-board")
    private Board board;
    // device 
    @ManyToOne
    @JoinColumn(name="device")
    @JsonBackReference("queue-device")
    private Device device;
    // board task id
    @Column
    private long boardTaskId;
    // task name
    @Column
    private String taskName;
    // system task
    @Column
    private boolean systemTask;
    // processed
    @Column
    private boolean processed;
    // in board queue
    @Column
    private boolean inBoardQueue;
    // system queue
    @Column
    private boolean systemQueue;
    // task is a repeat/target=0
    @Column
    private boolean taskRepeat;
    // task delay
    @Column(columnDefinition="COLUMN_TYPE default '0'")
    private long delay;
    // expired
    @Column
    private boolean expired;
    // expired date and time
    @Column
    private Instant expiredDateTime;
    // next occurance date and time
    @Column
    private Instant nextOccurance;
    // command json string
    @Lob
    @Column(columnDefinition ="MEDIUMTEXT")
    private String commandJsonString;
    // function/route
    @ManyToOne
    @JoinColumn(name="route_id")
    @JsonBackReference("queue-route")
    private Route route;
    // mode 
    @ManyToOne
    @JoinColumn(name="mode_id")
    @JsonBackReference("queue-mode")
    private Mode mode;
    // current date and time for transaction
    @Transient 
    private Instant currentDateTime=null;

    @PrePersist
    protected void prePersist(){
        long deviceId=0;
        if(this.getDevice()!=null) deviceId=this.getDevice().getId();
        this.initId(this.getBoard().getId(),deviceId);
        if(this.getRoute()!=null) delay=this.getRoute().getBoardAction().getDelayInterval();
        if(this.getMode()!=null) delay=this.getMode().getBoardAction().getDelayInterval();
        if(this.expiredDateTime==null&&systemTask&&this.taskRepeat)this.nextOccurance=Instant.now().plusMillis(this.delay);   
    }
    @Override
    protected void preUpdate(){
        
    }
    @PostLoad
    protected void postLoad(){
        if(this.systemTask&&!this.taskRepeat&&this.device!=null&&nextOccurance==null){
            this.nextOccurance=this.expiredDateTime;
        }
    }


    public BoardQueue() {
    }

    public BoardQueue(Board board, Device device, long boardTaskId, String taskName, boolean systemTask, boolean processed, boolean inBoardQueue, boolean systemQueue, boolean taskRepeat, long delay, boolean expired, Instant expiredDateTime, Instant nextOccurance, String commandJsonString, Route route, Mode mode) {
        this.board = board;
        this.device = device;
        this.boardTaskId = boardTaskId;
        this.taskName = taskName;
        this.systemTask = systemTask;
        this.processed = processed;
        this.inBoardQueue = inBoardQueue;
        this.systemQueue = systemQueue;
        this.taskRepeat = taskRepeat;
        this.delay = delay;
        this.expired = expired;
        this.expiredDateTime = expiredDateTime;
        this.nextOccurance = nextOccurance;
        this.commandJsonString = commandJsonString;
        this.route = route;
        this.mode = mode;
    }


    public Board getBoard() {
        return this.board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Device getDevice() {
        return this.device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public long getBoardTaskId() {
        return this.boardTaskId;
    }

    public void setBoardTaskId(long boardTaskId) {
        this.boardTaskId = boardTaskId;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
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

    public boolean isProcessed() {
        return this.processed;
    }

    public boolean getProcessed() {
        return this.processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public boolean isInBoardQueue() {
        return this.inBoardQueue;
    }

    public boolean getInBoardQueue() {
        return this.inBoardQueue;
    }

    public void setInBoardQueue(boolean inBoardQueue) {
        this.inBoardQueue = inBoardQueue;
    }

    public boolean isSystemQueue() {
        return this.systemQueue;
    }

    public boolean getSystemQueue() {
        return this.systemQueue;
    }

    public void setSystemQueue(boolean systemQueue) {
        this.systemQueue = systemQueue;
    }

    public boolean isTaskRepeat() {
        return this.taskRepeat;
    }

    public boolean getTaskRepeat() {
        return this.taskRepeat;
    }

    public void setTaskRepeat(boolean taskRepeat) {
        this.taskRepeat = taskRepeat;
    }

    public long getDelay() {
        return this.delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public boolean isExpired() {
        return this.expired;
    }

    public boolean getExpired() {
        return this.expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public Instant getExpiredDateTime() {
        return this.expiredDateTime;
    }

    public void setExpiredDateTime(Instant expiredDateTime) {
        this.expiredDateTime = expiredDateTime;
    }

    public String getCommandJsonString() {
        return this.commandJsonString;
    }

    public void setCommandJsonString(String commandJsonString) {
        this.commandJsonString = commandJsonString;
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

    public BoardQueue board(Board board) {
        setBoard(board);
        return this;
    }

    public BoardQueue device(Device device) {
        setDevice(device);
        return this;
    }

    public BoardQueue boardTaskId(long boardTaskId) {
        setBoardTaskId(boardTaskId);
        return this;
    }

    public BoardQueue taskName(String taskName) {
        setTaskName(taskName);
        return this;
    }

    public BoardQueue systemTask(boolean systemTask) {
        setSystemTask(systemTask);
        return this;
    }

    public BoardQueue processed(boolean processed) {
        setProcessed(processed);
        return this;
    }

    public BoardQueue inBoardQueue(boolean inBoardQueue) {
        setInBoardQueue(inBoardQueue);
        return this;
    }

    public BoardQueue systemQueue(boolean systemQueue) {
        setSystemQueue(systemQueue);
        return this;
    }

    public BoardQueue taskRepeat(boolean taskRepeat) {
        setTaskRepeat(taskRepeat);
        return this;
    }

    public BoardQueue delay(long delay) {
        setDelay(delay);
        return this;
    }

    public BoardQueue expired(boolean expired) {
        setExpired(expired);
        return this;
    }

    public BoardQueue expiredDateTime(Instant expiredDateTime) {
        setExpiredDateTime(expiredDateTime);
        return this;
    }

    public BoardQueue commandJsonString(String commandJsonString) {
        setCommandJsonString(commandJsonString);
        return this;
    }

    public BoardQueue route(Route route) {
        setRoute(route);
        return this;
    }

    public BoardQueue mode(Mode mode) {
        setMode(mode);
        return this;
    }

    public Instant getNextOccurance() {
        return this.nextOccurance;
    }

    public void setNextOccurance(Instant nextOccurance) {
        this.nextOccurance = nextOccurance;
    }

    public Instant getCurrentDateTime() {
        return this.currentDateTime;
    }

    public void setCurrentDateTime(Instant currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    public BoardQueue nextOccurance(Instant nextOccurance) {
        setNextOccurance(nextOccurance);
        return this;
    }

    public BoardQueue currentDateTime(Instant currentDateTime) {
        setCurrentDateTime(currentDateTime);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof BoardQueue)) {
            return false;
        }
        BoardQueue boardQueue = (BoardQueue) o;
        return Objects.equals(board, boardQueue.board) && Objects.equals(device, boardQueue.device) && boardTaskId == boardQueue.boardTaskId && Objects.equals(taskName, boardQueue.taskName) && systemTask == boardQueue.systemTask && processed == boardQueue.processed && inBoardQueue == boardQueue.inBoardQueue && systemQueue == boardQueue.systemQueue && taskRepeat == boardQueue.taskRepeat && delay == boardQueue.delay && expired == boardQueue.expired && Objects.equals(expiredDateTime, boardQueue.expiredDateTime) && Objects.equals(nextOccurance, boardQueue.nextOccurance) && Objects.equals(commandJsonString, boardQueue.commandJsonString) && Objects.equals(route, boardQueue.route) && Objects.equals(mode, boardQueue.mode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, device, boardTaskId, taskName, systemTask, processed, inBoardQueue, systemQueue, taskRepeat, delay, expired, expiredDateTime, nextOccurance, commandJsonString, route, mode);
    }
   

    @Override
    public String toString() {
        return "{" +
            " board='" + getBoard() + "'" +
            ", device='" + getDevice() + "'" +
            ", boardTaskId='" + getBoardTaskId() + "'" +
            ", taskName='" + getTaskName() + "'" +
            ", systemTask='" + isSystemTask() + "'" +
            ", processed='" + isProcessed() + "'" +
            ", inBoardQueue='" + isInBoardQueue() + "'" +
            ", systemQueue='" + isSystemQueue() + "'" +
            ", taskRepeat='" + isTaskRepeat() + "'" +
            ", delay='" + getDelay() + "'" +
            ", expired='" + isExpired() + "'" +
            ", expiredDateTime='" + getExpiredDateTime() + "'" +
            ", nextOccurance='" + getNextOccurance() + "'" +
            ", commandJsonString='" + getCommandJsonString() + "'" +
            ", route='" + getRoute() + "'" +
            ", mode='" + getMode() + "'" +
            "}";
    }
    

}
