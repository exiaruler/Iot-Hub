package com.scheduler.app.backend.aREST.Models;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.scheduler.Base.ModelBase.ModelBase;
import com.scheduler.app.backend.Hardware.Models.Hardware;

@Entity
@Table(indexes = @Index(columnList = "board_id"))
public class Board extends ModelBase {
 
    // esp board id
    @Column(name = "board_id")
    private String boardId;
    // generated authotisation key
    @Column
    private String boardKey;
    // board name
    @Column
    @NotBlank(message = "Require name")
    private String name;
    // SSID
    
    // mac address

    // local ip address
    @Column
    private String ip;
    // active status
    @Column
    private boolean status=false;
    // arestFramework installed
    @Column 
    private boolean arest=false;
    // arestFramework command install
    @Column
    private boolean arestCommand=false;
    // socket framework 
    @Column
    private boolean socket=false;
    // periodic check
    @Column
    private int periodicCheck=60000;
    // current RAM usage
    @Column
    private int ramUsage=0;
    // board registered status
    @Column
    private boolean activated=false;
    // board websocket connection id
    @Column
    private String websocketId="";
    // development mode
    @Column
    private boolean devMode;
    // last connection date and time (HTTP or websocket)
    @Column(nullable = true)
    Instant lastConnectDateTime;
    // restart device timeout (mins to hours)
    @Column(nullable = true)
    private long timeout;
    // restart device timeout enabled
    @Column(nullable = true)
    private boolean restartTimeout=false;
    // total number of tasks executed
    @Column(nullable = true)
    private int tasksExecuted=0;
    // device list
    @JsonManagedReference("device-board")
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "board", cascade =CascadeType.ALL)
    private List<Device> device=new ArrayList<>();
    // section that the board belongs to
    @JsonBackReference("board-section")
    @ManyToOne
    @JoinColumn(name="section_id")
    private Section section;
    // hardware model
    @JsonBackReference("board-hardware")
    @ManyToOne
    @JoinColumn(name="hardware_id")
    private Hardware hardware;
    // Board queue
    @JsonManagedReference("queue-board")
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "board", cascade =CascadeType.ALL)
    private List<BoardQueue> boardOperations=new ArrayList<>();
    // request-only hardware id used by controller payloads
    @Column
    private long hardwardId;


    public Board() {
    }

    public Board(String boardId, String boardKey, String name, String ip, boolean status, boolean arest, boolean arestCommand, boolean socket, int periodicCheck, int ramUsage, boolean activated, String websocketId, boolean devMode, Instant lastConnectDateTime, long timeout, boolean restartTimeout, int tasksExecuted, List<Device> device, Section section, Hardware hardware, long hardwardId) {
        this.boardId = boardId;
        this.boardKey = boardKey;
        this.name = name;
        this.ip = ip;
        this.status = status;
        this.arest = arest;
        this.arestCommand = arestCommand;
        this.socket = socket;
        this.periodicCheck = periodicCheck;
        this.ramUsage = ramUsage;
        this.activated = activated;
        this.websocketId = websocketId;
        this.devMode = devMode;
        this.lastConnectDateTime = lastConnectDateTime;
        this.timeout = timeout;
        this.restartTimeout = restartTimeout;
        this.tasksExecuted = tasksExecuted;
        this.device = device;
        this.section = section;
        this.hardware = hardware;
        this.hardwardId = hardwardId;
    }

    public String getBoardId() {
        return this.boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public String getBoardKey() {
        return this.boardKey;
    }

    public void setBoardKey(String boardKey) {
        this.boardKey = boardKey;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    public boolean isArest() {
        return this.arest;
    }

    public boolean getArest() {
        return this.arest;
    }

    public void setArest(boolean arest) {
        this.arest = arest;
    }

    public boolean isArestCommand() {
        return this.arestCommand;
    }

    public boolean getArestCommand() {
        return this.arestCommand;
    }

    public void setArestCommand(boolean arestCommand) {
        this.arestCommand = arestCommand;
    }

    public boolean isSocket() {
        return this.socket;
    }

    public boolean getSocket() {
        return this.socket;
    }

    public void setSocket(boolean socket) {
        this.socket = socket;
    }

    public int getPeriodicCheck() {
        return this.periodicCheck;
    }

    public void setPeriodicCheck(int periodicCheck) {
        this.periodicCheck = periodicCheck;
    }

    public int getRamUsage() {
        return this.ramUsage;
    }

    public void setRamUsage(int ramUsage) {
        this.ramUsage = ramUsage;
    }

    public boolean isActivated() {
        return this.activated;
    }

    public boolean getActivated() {
        return this.activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getWebsocketId() {
        return this.websocketId;
    }

    public void setWebsocketId(String websocketId) {
        this.websocketId = websocketId;
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

    public Instant getLastConnectDateTime() {
        return this.lastConnectDateTime;
    }

    public void setLastConnectDateTime(Instant lastConnectDateTime) {
        this.lastConnectDateTime = lastConnectDateTime;
    }

    public long getTimeout() {
        return this.timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public boolean isRestartTimeout() {
        return this.restartTimeout;
    }

    public boolean getRestartTimeout() {
        return this.restartTimeout;
    }

    public void setRestartTimeout(boolean restartTimeout) {
        this.restartTimeout = restartTimeout;
    }

    public int getTasksExecuted() {
        return this.tasksExecuted;
    }

    public void setTasksExecuted(int tasksExecuted) {
        this.tasksExecuted = tasksExecuted;
    }

    public List<Device> getDevice() {
        return this.device;
    }

    public void setDevice(List<Device> device) {
        this.device = device;
    }

    public Section getSection() {
        return this.section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Hardware getHardware() {
        return this.hardware;
    }

    public void setHardware(Hardware hardware) {
        this.hardware = hardware;
    }

    public long getHardwardId() {
        return this.hardwardId;
    }

    public void setHardwardId(long hardwardId) {
        this.hardwardId = hardwardId;
    }

    public Board boardId(String boardId) {
        setBoardId(boardId);
        return this;
    }

    public Board boardKey(String boardKey) {
        setBoardKey(boardKey);
        return this;
    }

    public Board name(String name) {
        setName(name);
        return this;
    }

    public Board ip(String ip) {
        setIp(ip);
        return this;
    }

    public Board status(boolean status) {
        setStatus(status);
        return this;
    }

    public Board arest(boolean arest) {
        setArest(arest);
        return this;
    }

    public Board arestCommand(boolean arestCommand) {
        setArestCommand(arestCommand);
        return this;
    }

    public Board socket(boolean socket) {
        setSocket(socket);
        return this;
    }

    public Board periodicCheck(int periodicCheck) {
        setPeriodicCheck(periodicCheck);
        return this;
    }

    public Board ramUsage(int ramUsage) {
        setRamUsage(ramUsage);
        return this;
    }

    public Board activated(boolean activated) {
        setActivated(activated);
        return this;
    }

    public Board websocketId(String websocketId) {
        setWebsocketId(websocketId);
        return this;
    }

    public Board devMode(boolean devMode) {
        setDevMode(devMode);
        return this;
    }

    public Board lastConnectDateTime(Instant lastConnectDateTime) {
        setLastConnectDateTime(lastConnectDateTime);
        return this;
    }

    public Board timeout(long timeout) {
        setTimeout(timeout);
        return this;
    }

    public Board restartTimeout(boolean restartTimeout) {
        setRestartTimeout(restartTimeout);
        return this;
    }

    public Board tasksExecuted(int tasksExecuted) {
        setTasksExecuted(tasksExecuted);
        return this;
    }

    public Board device(List<Device> device) {
        setDevice(device);
        return this;
    }

    public Board section(Section section) {
        setSection(section);
        return this;
    }

    public Board hardware(Hardware hardware) {
        setHardware(hardware);
        return this;
    }

    public Board hardwardId(long hardwardId) {
        setHardwardId(hardwardId);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Board)) {
            return false;
        }
        Board board = (Board) o;
        return Objects.equals(boardId, board.boardId) && Objects.equals(boardKey, board.boardKey) && Objects.equals(name, board.name) && Objects.equals(ip, board.ip) && status == board.status && arest == board.arest && arestCommand == board.arestCommand && socket == board.socket && periodicCheck == board.periodicCheck && ramUsage == board.ramUsage && activated == board.activated && Objects.equals(websocketId, board.websocketId) && devMode == board.devMode && Objects.equals(lastConnectDateTime, board.lastConnectDateTime) && Objects.equals(timeout, board.timeout) && restartTimeout == board.restartTimeout && tasksExecuted == board.tasksExecuted && Objects.equals(device, board.device) && Objects.equals(section, board.section) && Objects.equals(hardware, board.hardware) && hardwardId == board.hardwardId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardId, boardKey, name, ip, status, arest, arestCommand, socket, periodicCheck, ramUsage, activated, websocketId, devMode, lastConnectDateTime, timeout, restartTimeout, tasksExecuted, device, section, hardware, hardwardId);
    }

    @Override
    public String toString() {
        return "{" +
            " boardId='" + getBoardId() + "'" +
            ", boardKey='" + getBoardKey() + "'" +
            ", name='" + getName() + "'" +
            ", ip='" + getIp() + "'" +
            ", status='" + isStatus() + "'" +
            ", arest='" + isArest() + "'" +
            ", arestCommand='" + isArestCommand() + "'" +
            ", socket='" + isSocket() + "'" +
            ", periodicCheck='" + getPeriodicCheck() + "'" +
            ", ramUsage='" + getRamUsage() + "'" +
            ", activated='" + isActivated() + "'" +
            ", websocketId='" + getWebsocketId() + "'" +
            ", devMode='" + isDevMode() + "'" +
            ", lastConnectDateTime='" + getLastConnectDateTime() + "'" +
            ", timeout='" + getTimeout() + "'" +
            ", restartTimeout='" + isRestartTimeout() + "'" +
            ", tasksExecuted='" + getTasksExecuted() + "'" +
            ", device='" + getDevice() + "'" +
            ", section='" + getSection() + "'" +
            ", hardware='" + getHardware() + "'" +
            ", hardwardId='" + getHardwardId() + "'" +
            "}";
    }
    
}
