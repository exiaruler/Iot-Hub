package com.scheduler.app.backend.Messaging.Board.Models;
import com.scheduler.app.backend.Messaging.Models.BoardTask;

import java.util.*;
import java.util.Objects;
// use for routine checks
public class DeviceCheck{
    // board long id
    private long id;
    // board id
    private String boardId="";
    // user is currently interfacing with device
    private boolean userActive;
    // server settings been updated
    private boolean updated;
    // time for regular board check
    private int routineCheck;
    // time to regulary close web socket connection
    private int closeConnection;
    // next task avaliable
    private boolean scheduleAvaliable;
    // development mode and configurations
    private boolean devMode;
    // server url
    private String devServerUrl="";
    // websocket url
    private String devWsPort="";
    // websocket headers
    // http headers
    // command tasks
    private List<BoardTask> tasks=new ArrayList<>();
    
    public DeviceCheck() {
    }

    public DeviceCheck(long id, String boardId, boolean userActive, boolean updated, int routineCheck, int closeConnection, boolean scheduleAvaliable, boolean devMode, String devServerUrl, String devWsPort, List<BoardTask> tasks) {
        this.id = id;
        this.boardId = boardId;
        this.userActive = userActive;
        this.updated = updated;
        this.routineCheck = routineCheck;
        this.closeConnection = closeConnection;
        this.scheduleAvaliable = scheduleAvaliable;
        this.devMode = devMode;
        this.devServerUrl = devServerUrl;
        this.devWsPort = devWsPort;
        this.tasks = tasks;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBoardId() {
        return this.boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public boolean isUserActive() {
        return this.userActive;
    }

    public boolean getUserActive() {
        return this.userActive;
    }

    public void setUserActive(boolean userActive) {
        this.userActive = userActive;
    }

    public boolean isUpdated() {
        return this.updated;
    }

    public boolean getUpdated() {
        return this.updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public int getRoutineCheck() {
        return this.routineCheck;
    }

    public void setRoutineCheck(int routineCheck) {
        this.routineCheck = routineCheck;
    }

    public int getCloseConnection() {
        return this.closeConnection;
    }

    public void setCloseConnection(int closeConnection) {
        this.closeConnection = closeConnection;
    }

    public boolean isScheduleAvaliable() {
        return this.scheduleAvaliable;
    }

    public boolean getScheduleAvaliable() {
        return this.scheduleAvaliable;
    }

    public void setScheduleAvaliable(boolean scheduleAvaliable) {
        this.scheduleAvaliable = scheduleAvaliable;
    }

    public boolean isDevMode() {
        return this.devMode;
    }

    public boolean getDevMode() {
        return this.devMode;
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    public String getDevServerUrl() {
        return this.devServerUrl;
    }

    public void setDevServerUrl(String devServerUrl) {
        this.devServerUrl = devServerUrl;
    }

    public String getDevWsPort() {
        return this.devWsPort;
    }

    public void setDevWsPort(String devWsPort) {
        this.devWsPort = devWsPort;
    }

    public List<BoardTask> getTasks() {
        return this.tasks;
    }

    public void setTasks(List<BoardTask> tasks) {
        this.tasks = tasks;
    }

    public DeviceCheck id(long id) {
        setId(id);
        return this;
    }

    public DeviceCheck boardId(String boardId) {
        setBoardId(boardId);
        return this;
    }

    public DeviceCheck userActive(boolean userActive) {
        setUserActive(userActive);
        return this;
    }

    public DeviceCheck updated(boolean updated) {
        setUpdated(updated);
        return this;
    }

    public DeviceCheck routineCheck(int routineCheck) {
        setRoutineCheck(routineCheck);
        return this;
    }

    public DeviceCheck closeConnection(int closeConnection) {
        setCloseConnection(closeConnection);
        return this;
    }

    public DeviceCheck scheduleAvaliable(boolean scheduleAvaliable) {
        setScheduleAvaliable(scheduleAvaliable);
        return this;
    }

    public DeviceCheck devMode(boolean devMode) {
        setDevMode(devMode);
        return this;
    }

    public DeviceCheck devServerUrl(String devServerUrl) {
        setDevServerUrl(devServerUrl);
        return this;
    }

    public DeviceCheck devWsPort(String devWsPort) {
        setDevWsPort(devWsPort);
        return this;
    }

    public DeviceCheck tasks(List<BoardTask> tasks) {
        setTasks(tasks);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof DeviceCheck)) {
            return false;
        }
        DeviceCheck deviceCheck = (DeviceCheck) o;
        return id == deviceCheck.id && Objects.equals(boardId, deviceCheck.boardId) && userActive == deviceCheck.userActive && updated == deviceCheck.updated && routineCheck == deviceCheck.routineCheck && closeConnection == deviceCheck.closeConnection && scheduleAvaliable == deviceCheck.scheduleAvaliable && devMode == deviceCheck.devMode && Objects.equals(devServerUrl, deviceCheck.devServerUrl) && Objects.equals(devWsPort, deviceCheck.devWsPort) && Objects.equals(tasks, deviceCheck.tasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, boardId, userActive, updated, routineCheck, closeConnection, scheduleAvaliable, devMode, devServerUrl, devWsPort, tasks);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", boardId='" + getBoardId() + "'" +
            ", userActive='" + isUserActive() + "'" +
            ", updated='" + isUpdated() + "'" +
            ", routineCheck='" + getRoutineCheck() + "'" +
            ", closeConnection='" + getCloseConnection() + "'" +
            ", scheduleAvaliable='" + isScheduleAvaliable() + "'" +
            ", devMode='" + isDevMode() + "'" +
            ", devServerUrl='" + getDevServerUrl() + "'" +
            ", devWsPort='" + getDevWsPort() + "'" +
            ", tasks='" + getTasks() + "'" +
            "}";
    }
    



}
