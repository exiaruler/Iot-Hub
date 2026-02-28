package com.scheduler.app.backend.aREST.Models;

import java.time.Instant;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.scheduler.Base.ModelBase.TaskEventId;
import com.scheduler.Base.ModelBase.TaskEventModelBase;

@Entity
public class Task extends TaskEventModelBase {
    // application
    @Column
    private String application;
    // device Id 
    @Column(insertable = false, updatable = false)
    private long deviceId;
    // board id 
    @Column(insertable = false, updatable = false)
    private long boardId;
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
    // board task json string
    @Lob
    @Column(columnDefinition ="MEDIUMTEXT")
    private String boardTaskJson="";
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
    private Instant scheduledTime=Instant.now();
    // task to repeat once
    @Column 
    private boolean oneTimeJob=true;
    // system task
    @Column
    private boolean systemTask=false;
    // parent task or associated task, associated by existing automated tasks
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "boardId", column = @Column(name = "parent_board_id")),
        @AttributeOverride(name = "deviceId", column = @Column(name = "parent_device_id")),
        @AttributeOverride(name = "eventTime", column = @Column(name = "parent_event_time"))
    })
    private TaskEventId parentTask;
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

    public Task(String application, long deviceId, long board, long routeId, long modeId, long commandId, long boardTaskId, String boardTaskJson, String url, String payload, String section, int priority, boolean motor, Instant scheduledTime, boolean oneTimeJob, boolean systemTask, TaskEventId parentTask, boolean updateDevice, boolean active, boolean httpTask, int retry, Schedule schedule) {
        this.initId(boardId, deviceId);
        this.application = application;
        this.deviceId = deviceId;
        this.boardId = board;
        this.routeId = routeId;
        this.modeId = modeId;
        this.commandId = commandId;
        this.boardTaskId = boardTaskId;
        this.boardTaskJson = boardTaskJson;
        this.url = url;
        this.payload = payload;
        this.section = section;
        this.priority = priority;
        this.motor = motor;
        this.scheduledTime = scheduledTime;
        this.oneTimeJob = oneTimeJob;
        this.systemTask = systemTask;
        this.parentTask = parentTask;
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
        return this.boardId;
    }

    public void setBoard(long board) {
        this.boardId = board;
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

    public String getBoardTaskJson() {
        return this.boardTaskJson;
    }

    public void setBoardTaskJson(String boardTaskJson) {
        this.boardTaskJson = boardTaskJson;
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

    public Instant getScheduledTime() {
        return this.scheduledTime;
    }

    public void setScheduledTime(Instant scheduledTime) {
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

    public boolean isSystemTask() {
        return this.systemTask;
    }

    public boolean getSystemTask() {
        return this.systemTask;
    }

    public void setSystemTask(boolean systemTask) {
        this.systemTask = systemTask;
    }

    public TaskEventId getParentTask() {
        return this.parentTask;
    }

    public void setParentTask(TaskEventId parentTask) {
        this.parentTask = parentTask;
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

    public Task boardTaskJson(String boardTaskJson) {
        setBoardTaskJson(boardTaskJson);
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

    public Task scheduledTime(Instant scheduledTime) {
        setScheduledTime(scheduledTime);
        return this;
    }

    public Task oneTimeJob(boolean oneTimeJob) {
        setOneTimeJob(oneTimeJob);
        return this;
    }

    public Task systemTask(boolean systemTask) {
        setSystemTask(systemTask);
        return this;
    }

    public Task parentTask(TaskEventId parentTask) {
        setParentTask(parentTask);
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
        return Objects.equals(application, task.application) && deviceId == task.deviceId && boardId == task.boardId && routeId == task.routeId && modeId == task.modeId && commandId == task.commandId && boardTaskId == task.boardTaskId && Objects.equals(boardTaskJson, task.boardTaskJson) && Objects.equals(url, task.url) && Objects.equals(payload, task.payload) && Objects.equals(section, task.section) && priority == task.priority && motor == task.motor && Objects.equals(scheduledTime, task.scheduledTime) && oneTimeJob == task.oneTimeJob && systemTask == task.systemTask && parentTask == task.parentTask && updateDevice == task.updateDevice && active == task.active && httpTask == task.httpTask && retry == task.retry && Objects.equals(schedule, task.schedule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(application, deviceId, boardId, routeId, modeId, commandId, boardTaskId, boardTaskJson, url, payload, section, priority, motor, scheduledTime, oneTimeJob, systemTask, parentTask, updateDevice, active, httpTask, retry, schedule);
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
            ", boardTaskJson='" + getBoardTaskJson() + "'" +
            ", url='" + getUrl() + "'" +
            ", payload='" + getPayload() + "'" +
            ", section='" + getSection() + "'" +
            ", priority='" + getPriority() + "'" +
            ", motor='" + isMotor() + "'" +
            ", scheduledTime='" + getScheduledTime() + "'" +
            ", oneTimeJob='" + isOneTimeJob() + "'" +
            ", systemTask='" + isSystemTask() + "'" +
            ", parentTask='" + getParentTask() + "'" +
            ", updateDevice='" + isUpdateDevice() + "'" +
            ", active='" + isActive() + "'" +
            ", httpTask='" + isHttpTask() + "'" +
            ", retry='" + getRetry() + "'" +
            ", schedule='" + getSchedule() + "'" +
            "}";
    }
    
    
}
