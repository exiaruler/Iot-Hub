package com.scheduler.app.backend.aREST.Models;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.scheduler.Base.ModelBase.ModelBase;
// schedule http request and device tasks
@Entity
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Schedule extends ModelBase {
    // name
    @Column
    private String name="";
    // time interval
    @Column
    private long time;
    // if repeating
    @Column
    private boolean repeatTask;
    // task for startup
    @Column
    private boolean startup;
    // next task after schedule task is completed
    @Column
    private long nextTask;
    // mode
    @Column 
    private String modeValue="";
    // retry connection
    @Column
    private int retries=3;
    // schedule task enabled
    private boolean status=true;
    // url
    @Column
    private String url="";
    // http request body
    @Column
    private String body="";
    // link to task
    @JsonManagedReference("schedule-task")
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "schedule", cascade = CascadeType.ALL)
    private Task task;
    // link to device
    @JsonBackReference("device-schedules")
    @ManyToOne
    @JoinColumn(name="device_id")
    private Device device;
    // device id
    @Column
    private long deviceId;
    // link route from device
    @JsonBackReference("route-schedule")
    @ManyToOne
    @JoinColumn(name="route_id")
    private Route route;
    // route id
    @Column
    private long routeId;
    // link mode
    @JsonBackReference("schedule-mode")
    @ManyToOne
    @JoinColumn(name="mode_id")
    private Mode mode;
    // mode id
    @Column
    private long modeId;
    

    public Schedule() {
    }

    public Schedule(String name, long time, boolean repeatTask, boolean startup, long nextTask, String modeValue, int retries, boolean status, String url, String body, Task task, Device device, long deviceId, Route route, long routeId, Mode mode, long modeId) {
        this.name = name;
        this.time = time;
        this.repeatTask = repeatTask;
        this.startup = startup;
        this.nextTask = nextTask;
        this.modeValue = modeValue;
        this.retries = retries;
        this.status = status;
        this.url = url;
        this.body = body;
        this.task = task;
        this.device = device;
        this.deviceId = deviceId;
        this.route = route;
        this.routeId = routeId;
        this.mode = mode;
        this.modeId = modeId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isRepeatTask() {
        return this.repeatTask;
    }

    public boolean getRepeatTask() {
        return this.repeatTask;
    }

    public void setRepeatTask(boolean repeatTask) {
        this.repeatTask = repeatTask;
    }

    public boolean isStartup() {
        return this.startup;
    }

    public boolean getStartup() {
        return this.startup;
    }

    public void setStartup(boolean startup) {
        this.startup = startup;
    }

    public long getNextTask() {
        return this.nextTask;
    }

    public void setNextTask(long nextTask) {
        this.nextTask = nextTask;
    }

    public String getModeValue() {
        return this.modeValue;
    }

    public void setModeValue(String modeValue) {
        this.modeValue = modeValue;
    }

    public int getRetries() {
        return this.retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
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

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Task getTask() {
        return this.task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Device getDevice() {
        return this.device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public long getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public Route getRoute() {
        return this.route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public long getRouteId() {
        return this.routeId;
    }

    public void setRouteId(long routeId) {
        this.routeId = routeId;
    }

    public Mode getMode() {
        return this.mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public long getModeId() {
        return this.modeId;
    }

    public void setModeId(long modeId) {
        this.modeId = modeId;
    }

    public Schedule name(String name) {
        setName(name);
        return this;
    }

    public Schedule time(long time) {
        setTime(time);
        return this;
    }

    public Schedule repeatTask(boolean repeatTask) {
        setRepeatTask(repeatTask);
        return this;
    }

    public Schedule startup(boolean startup) {
        setStartup(startup);
        return this;
    }

    public Schedule nextTask(long nextTask) {
        setNextTask(nextTask);
        return this;
    }

    public Schedule modeValue(String modeValue) {
        setModeValue(modeValue);
        return this;
    }

    public Schedule retries(int retries) {
        setRetries(retries);
        return this;
    }

    public Schedule status(boolean status) {
        setStatus(status);
        return this;
    }

    public Schedule url(String url) {
        setUrl(url);
        return this;
    }

    public Schedule body(String body) {
        setBody(body);
        return this;
    }

    public Schedule task(Task task) {
        setTask(task);
        return this;
    }

    public Schedule device(Device device) {
        setDevice(device);
        return this;
    }

    public Schedule deviceId(long deviceId) {
        setDeviceId(deviceId);
        return this;
    }

    public Schedule route(Route route) {
        setRoute(route);
        return this;
    }

    public Schedule routeId(long routeId) {
        setRouteId(routeId);
        return this;
    }

    public Schedule mode(Mode mode) {
        setMode(mode);
        return this;
    }

    public Schedule modeId(long modeId) {
        setModeId(modeId);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Schedule)) {
            return false;
        }
        Schedule schedule = (Schedule) o;
        return Objects.equals(name, schedule.name) && time == schedule.time && repeatTask == schedule.repeatTask && startup == schedule.startup && nextTask == schedule.nextTask && Objects.equals(modeValue, schedule.modeValue) && retries == schedule.retries && status == schedule.status && Objects.equals(url, schedule.url) && Objects.equals(body, schedule.body) && Objects.equals(task, schedule.task) && Objects.equals(device, schedule.device) && deviceId == schedule.deviceId && Objects.equals(route, schedule.route) && routeId == schedule.routeId && Objects.equals(mode, schedule.mode) && modeId == schedule.modeId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, time, repeatTask, startup, nextTask, modeValue, retries, status, url, body, task, device, deviceId, route, routeId, mode, modeId);
    }

    @Override
    public String toString() {
        return "{" +
            " name='" + getName() + "'" +
            ", time='" + getTime() + "'" +
            ", repeatTask='" + isRepeatTask() + "'" +
            ", startup='" + isStartup() + "'" +
            ", nextTask='" + getNextTask() + "'" +
            ", modeValue='" + getModeValue() + "'" +
            ", retries='" + getRetries() + "'" +
            ", status='" + isStatus() + "'" +
            ", url='" + getUrl() + "'" +
            ", body='" + getBody() + "'" +
            ", task='" + getTask() + "'" +
            ", device='" + getDevice() + "'" +
            ", deviceId='" + getDeviceId() + "'" +
            ", route='" + getRoute() + "'" +
            ", routeId='" + getRouteId() + "'" +
            ", mode='" + getMode() + "'" +
            ", modeId='" + getModeId() + "'" +
            "}";
    }
    

}
