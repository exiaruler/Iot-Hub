package com.scheduler.app.backend.aREST.Models;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.scheduler.Base.ModelBase.ModelBase;

@Entity
public class Task extends ModelBase {
    // application
    @Column
    private String application;
    // device Id 
    @Column
    private long deviceId;
    // board id 
    @Column
    private long board;
    // route id
    @Column long routeId;
    // mode id
    @Column
    private long modeId;
    // command id
    @Column
    private long commandId;
    // boardTask id
    @Column
    private long boardTaskId=0;
    // url
    @Column
    private String url;
    // body of http request
    @Column
    private String payload="";
    // section 
    @Column
    private String section="";
    // task priority
    @Column
    private int priority=1;
    // if motor/servo use in the task
    @Column
    private boolean motor=false;
    // scheduled run time for task
    @Column
    private LocalDateTime scheduledTime=LocalDateTime.now();
    // task to repeat once
    @Column 
    private boolean oneTimeJob=true;
    // update device status in database
    @Column
    private boolean updateDevice=false;
    // task status
    @Column boolean active=false;
    // http task
    @Column
    private boolean httpTask=false;
    // retry attempt
    @Column
    private int retry=0;
    // schedule task
    @JsonBackReference("schedule-task")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "schedule_id",referencedColumnName = "id")
    private Schedule schedule;


    public Task() {
    }

    public Task(String application, long deviceId, long board, long routeId, long modeId, long commandId, long boardTaskId, String url, String payload, String section, int priority, boolean motor, LocalDateTime scheduledTime, boolean oneTimeJob, boolean updateDevice, boolean active, boolean httpTask, int retry, Schedule schedule) {
        this.application = application;
        this.deviceId = deviceId;
        this.board = board;
        this.routeId = routeId;
        this.modeId = modeId;
        this.commandId = commandId;
        this.boardTaskId = boardTaskId;
        this.url = url;
        this.payload = payload;
        this.section = section;
        this.priority = priority;
        this.motor = motor;
        this.scheduledTime = scheduledTime;
        this.oneTimeJob = oneTimeJob;
        this.updateDevice = updateDevice;
        this.active = active;
        this.httpTask = httpTask;
        this.retry = retry;
        this.schedule = schedule;
    }

    public String getApplication() {
        return this.application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public long getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public long getBoard() {
        return this.board;
    }

    public void setBoard(long board) {
        this.board = board;
    }

    public long getRouteId() {
        return this.routeId;
    }

    public void setRouteId(long routeId) {
        this.routeId = routeId;
    }

    public long getModeId() {
        return this.modeId;
    }

    public void setModeId(long modeId) {
        this.modeId = modeId;
    }

    public long getCommandId() {
        return this.commandId;
    }

    public void setCommandId(long commandId) {
        this.commandId = commandId;
    }

    public long getBoardTaskId() {
        return this.boardTaskId;
    }

    public void setBoardTaskId(long boardTaskId) {
        this.boardTaskId = boardTaskId;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPayload() {
        return this.payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getSection() {
        return this.section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isMotor() {
        return this.motor;
    }

    public boolean getMotor() {
        return this.motor;
    }

    public void setMotor(boolean motor) {
        this.motor = motor;
    }

    public LocalDateTime getScheduledTime() {
        return this.scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public boolean isOneTimeJob() {
        return this.oneTimeJob;
    }

    public boolean getOneTimeJob() {
        return this.oneTimeJob;
    }

    public void setOneTimeJob(boolean oneTimeJob) {
        this.oneTimeJob = oneTimeJob;
    }

    public boolean isUpdateDevice() {
        return this.updateDevice;
    }

    public boolean getUpdateDevice() {
        return this.updateDevice;
    }

    public void setUpdateDevice(boolean updateDevice) {
        this.updateDevice = updateDevice;
    }

    public boolean isActive() {
        return this.active;
    }

    public boolean getActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isHttpTask() {
        return this.httpTask;
    }

    public boolean getHttpTask() {
        return this.httpTask;
    }

    public void setHttpTask(boolean httpTask) {
        this.httpTask = httpTask;
    }

    public int getRetry() {
        return this.retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public Schedule getSchedule() {
        return this.schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Task application(String application) {
        setApplication(application);
        return this;
    }

    public Task deviceId(long deviceId) {
        setDeviceId(deviceId);
        return this;
    }

    public Task board(long board) {
        setBoard(board);
        return this;
    }

    public Task routeId(long routeId) {
        setRouteId(routeId);
        return this;
    }

    public Task modeId(long modeId) {
        setModeId(modeId);
        return this;
    }

    public Task commandId(long commandId) {
        setCommandId(commandId);
        return this;
    }

    public Task boardTaskId(long boardTaskId) {
        setBoardTaskId(boardTaskId);
        return this;
    }

    public Task url(String url) {
        setUrl(url);
        return this;
    }

    public Task payload(String payload) {
        setPayload(payload);
        return this;
    }

    public Task section(String section) {
        setSection(section);
        return this;
    }

    public Task priority(int priority) {
        setPriority(priority);
        return this;
    }

    public Task motor(boolean motor) {
        setMotor(motor);
        return this;
    }

    public Task scheduledTime(LocalDateTime scheduledTime) {
        setScheduledTime(scheduledTime);
        return this;
    }

    public Task oneTimeJob(boolean oneTimeJob) {
        setOneTimeJob(oneTimeJob);
        return this;
    }

    public Task updateDevice(boolean updateDevice) {
        setUpdateDevice(updateDevice);
        return this;
    }

    public Task active(boolean active) {
        setActive(active);
        return this;
    }

    public Task httpTask(boolean httpTask) {
        setHttpTask(httpTask);
        return this;
    }

    public Task retry(int retry) {
        setRetry(retry);
        return this;
    }

    public Task schedule(Schedule schedule) {
        setSchedule(schedule);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Task)) {
            return false;
        }
        Task task = (Task) o;
        return Objects.equals(application, task.application) && deviceId == task.deviceId && board == task.board && routeId == task.routeId && modeId == task.modeId && commandId == task.commandId && boardTaskId == task.boardTaskId && Objects.equals(url, task.url) && Objects.equals(payload, task.payload) && Objects.equals(section, task.section) && priority == task.priority && motor == task.motor && Objects.equals(scheduledTime, task.scheduledTime) && oneTimeJob == task.oneTimeJob && updateDevice == task.updateDevice && active == task.active && httpTask == task.httpTask && retry == task.retry && Objects.equals(schedule, task.schedule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(application, deviceId, board, routeId, modeId, commandId, boardTaskId, url, payload, section, priority, motor, scheduledTime, oneTimeJob, updateDevice, active, httpTask, retry, schedule);
    }

    @Override
    public String toString() {
        return "{" +
            " application='" + getApplication() + "'" +
            ", deviceId='" + getDeviceId() + "'" +
            ", board='" + getBoard() + "'" +
            ", routeId='" + getRouteId() + "'" +
            ", modeId='" + getModeId() + "'" +
            ", commandId='" + getCommandId() + "'" +
            ", boardTaskId='" + getBoardTaskId() + "'" +
            ", url='" + getUrl() + "'" +
            ", payload='" + getPayload() + "'" +
            ", section='" + getSection() + "'" +
            ", priority='" + getPriority() + "'" +
            ", motor='" + isMotor() + "'" +
            ", scheduledTime='" + getScheduledTime() + "'" +
            ", oneTimeJob='" + isOneTimeJob() + "'" +
            ", updateDevice='" + isUpdateDevice() + "'" +
            ", active='" + isActive() + "'" +
            ", httpTask='" + isHttpTask() + "'" +
            ", retry='" + getRetry() + "'" +
            ", schedule='" + getSchedule() + "'" +
            "}";
    }

    
}
