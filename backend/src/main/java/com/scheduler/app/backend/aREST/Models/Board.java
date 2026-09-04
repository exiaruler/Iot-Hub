package com.scheduler.app.backend.aREST.Models;
import java.time.Duration;
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
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.scheduler.Base.ModelBase.ModelBase;
import com.scheduler.app.backend.Firmware.Model.Firmware;
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
    @NotBlank(message = "name required")
    private String name;
    // SSID/wifi name
    @Column
    private String ssid;
    // mac address
    @Column
    private String macAddress;
    // version
    @Column
    private String firmwareVersion;
    // firmware
    @JsonBackReference("firmware-board")
    @ManyToOne
    @JoinColumn(name="firmware_id")
    private Firmware firmware;
    // local ip address
    @Column
    private String ip;
    // active status
    @Column
    private boolean status=false;
    // arestFramework installed
    @Column 
    private boolean arest=false;
    // arestFramework command install (redundent)
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
    // current heap size
    @Column
    private int heap;
    // total heap
    @Column
    private int heapTotal;
    // board current millis
    @Column
    private long millis;
    // current millis at the current moment
    @Transient
    private long currentMillis;
    // board registered status
    @Column
    private boolean activated=false;
    // board websocket connection id
    @Column
    private String websocketId="";
    // development mode
    @Column
    private boolean devMode;
    // one time dev mode
    //private boolean devModeTrigger;
    // devmode server url
    @Column
    private String devServerUrl="";
    // devmode websocket url
    @Column
    private String devWsUrl="";
    // last connection date and time (HTTP or websocket)
    @Column(nullable = true)
    Instant lastConnectDateTime;
    // last login date and time
    @Column(nullable = true)
    Instant lastLoginDateTime;
    // board activated date and time
    @Column(nullable = true)
    Instant activatedDateTime;
    // restart device timeout (mins to hours)
    @Column(nullable = true)
    private long timeout;
    // restart device timeout enabled
    @Column(nullable = true)
    private boolean restartTimeout=false;
    // board offline period
    @Column
    private long offline=3600000;
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
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "board", cascade =CascadeType.ALL, orphanRemoval = true)
    private List<BoardQueue> boardOperations=new ArrayList<>();
    // request-only hardware id used by controller payloads
    @Transient
    private long hardwardId;
    // time for next board queue operation
    @Transient
    private Instant nextQueueOperation;

    @PostLoad
    public void loadBoard(){
        long id=0;
        if(this.getHardware()!=null) id=this.getHardware().getId();
        this.hardwardId=id;
        getCurrentMillis();
    }
    // get current millis at that moment
    public long getCurrentMillis(){
        long UINT32_MAX = 4_294_967_296L;
        long calculate=0;
        if(this.millis>0){
            long elapsedMillis = Duration.between(lastConnectDateTime,Instant.now()).toMillis();
            if (elapsedMillis < 0) {
                // Clock skew guard - never predict backwards
                elapsedMillis = 0;
            }
            calculate = (this.millis + elapsedMillis) % UINT32_MAX;
            setMillis(calculate);
            this.currentMillis=calculate;
        }
        return calculate;
    }

    public Board() {
    }




    public Board(String boardId, String boardKey, String name, String ssid, String macAddress, String firmwareVersion, Firmware firmware, String ip, boolean status, boolean arest, boolean arestCommand, boolean socket, int periodicCheck, int ramUsage, int heap, int heapTotal, long millis, long currentMillis, boolean activated, String websocketId, boolean devMode, String devServerUrl, String devWsUrl, Instant lastConnectDateTime, Instant lastLoginDateTime, Instant activatedDateTime, long timeout, boolean restartTimeout, long offline, int tasksExecuted, List<Device> device, Section section, Hardware hardware, List<BoardQueue> boardOperations, long hardwardId, Instant nextQueueOperation) {
        this.boardId = boardId;
        this.boardKey = boardKey;
        this.name = name;
        this.ssid = ssid;
        this.macAddress = macAddress;
        this.firmwareVersion = firmwareVersion;
        this.firmware = firmware;
        this.ip = ip;
        this.status = status;
        this.arest = arest;
        this.arestCommand = arestCommand;
        this.socket = socket;
        this.periodicCheck = periodicCheck;
        this.ramUsage = ramUsage;
        this.heap = heap;
        this.heapTotal = heapTotal;
        this.millis = millis;
        this.currentMillis = currentMillis;
        this.activated = activated;
        this.websocketId = websocketId;
        this.devMode = devMode;
        this.devServerUrl = devServerUrl;
        this.devWsUrl = devWsUrl;
        this.lastConnectDateTime = lastConnectDateTime;
        this.lastLoginDateTime = lastLoginDateTime;
        this.activatedDateTime = activatedDateTime;
        this.timeout = timeout;
        this.restartTimeout = restartTimeout;
        this.offline = offline;
        this.tasksExecuted = tasksExecuted;
        this.device = device;
        this.section = section;
        this.hardware = hardware;
        this.boardOperations = boardOperations;
        this.hardwardId = hardwardId;
        this.nextQueueOperation = nextQueueOperation;
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

    public String getSsid() {
        return this.ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getMacAddress() {
        return this.macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getFirmwareVersion() {
        return this.firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public Firmware getFirmware() {
        return this.firmware;
    }

    public void setFirmware(Firmware firmware) {
        this.firmware = firmware;
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

    public String getDevServerUrl() {
        return this.devServerUrl;
    }

    public void setDevServerUrl(String devServerUrl) {
        this.devServerUrl = devServerUrl;
    }

    public String getDevWsUrl() {
        return this.devWsUrl;
    }

    public void setDevWsUrl(String devWsUrl) {
        this.devWsUrl = devWsUrl;
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

    public long getOffline() {
        return this.offline;
    }

    public void setOffline(long offline) {
        this.offline = offline;
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

    public List<BoardQueue> getBoardOperations() {
        return this.boardOperations;
    }

    public void setBoardOperations(List<BoardQueue> boardOperations) {
        this.boardOperations = boardOperations;
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

    public Board ssid(String ssid) {
        setSsid(ssid);
        return this;
    }

    public Board macAddress(String macAddress) {
        setMacAddress(macAddress);
        return this;
    }

    public Board firmwareVersion(String firmwareVersion) {
        setFirmwareVersion(firmwareVersion);
        return this;
    }

    public Board firmware(Firmware firmware) {
        setFirmware(firmware);
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

    public Board devServerUrl(String devServerUrl) {
        setDevServerUrl(devServerUrl);
        return this;
    }

    public Board devWsUrl(String devWsUrl) {
        setDevWsUrl(devWsUrl);
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

    public Board offline(long offline) {
        setOffline(offline);
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
    
    public Board boardOperations(List<BoardQueue> boardOperations) {
        setBoardOperations(boardOperations);
        return this;
    }

    public Board hardwardId(long hardwardId) {
        setHardwardId(hardwardId);
        return this;
    }

    public int getHeap() {
        return this.heap;
    }

    public void setHeap(int heap) {
        this.heap = heap;
    }

    public int getHeapTotal() {
        return this.heapTotal;
    }

    public void setHeapTotal(int heapTotal) {
        this.heapTotal = heapTotal;
    }

    public long getMillis() {
        return this.millis;
    }

    public void setMillis(long millis) {
        this.millis = millis;
    }
    
    public Instant getNextQueueOperation() {
        return this.nextQueueOperation;
    }

    public void setNextQueueOperation(Instant nextQueueOperation) {
        this.nextQueueOperation = nextQueueOperation;
    }

    public void setCurrentMillis(long currentMillis) {
        this.currentMillis = currentMillis;
    }

    public Instant getLastLoginDateTime() {
        return this.lastLoginDateTime;
    }

    public void setLastLoginDateTime(Instant lastLoginDateTime) {
        this.lastLoginDateTime = lastLoginDateTime;
    }

    public Instant getActivatedDateTime() {
        return this.activatedDateTime;
    }

    public void setActivatedDateTime(Instant activatedDateTime) {
        this.activatedDateTime = activatedDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Board)) {
            return false;
        }
        Board board = (Board) o;
        return Objects.equals(boardId, board.boardId) && Objects.equals(boardKey, board.boardKey) && Objects.equals(name, board.name) && Objects.equals(ssid, board.ssid) && Objects.equals(macAddress, board.macAddress) && Objects.equals(firmwareVersion, board.firmwareVersion) && Objects.equals(firmware, board.firmware) && Objects.equals(ip, board.ip) && status == board.status && arest == board.arest && arestCommand == board.arestCommand && socket == board.socket && periodicCheck == board.periodicCheck && ramUsage == board.ramUsage && heap == board.heap && heapTotal == board.heapTotal && millis == board.millis && currentMillis == board.currentMillis && activated == board.activated && Objects.equals(websocketId, board.websocketId) && devMode == board.devMode && Objects.equals(devServerUrl, board.devServerUrl) && Objects.equals(devWsUrl, board.devWsUrl) && Objects.equals(lastConnectDateTime, board.lastConnectDateTime) && Objects.equals(lastLoginDateTime, board.lastLoginDateTime) && Objects.equals(activatedDateTime, board.activatedDateTime) && timeout == board.timeout && restartTimeout == board.restartTimeout && offline == board.offline && tasksExecuted == board.tasksExecuted && Objects.equals(device, board.device) && Objects.equals(section, board.section) && Objects.equals(hardware, board.hardware) && Objects.equals(boardOperations, board.boardOperations) && hardwardId == board.hardwardId && Objects.equals(nextQueueOperation, board.nextQueueOperation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardId, boardKey, name, ssid, macAddress, firmwareVersion, firmware, ip, status, arest, arestCommand, socket, periodicCheck, ramUsage, heap, heapTotal, millis, currentMillis, activated, websocketId, devMode, devServerUrl, devWsUrl, lastConnectDateTime, lastLoginDateTime, activatedDateTime, timeout, restartTimeout, offline, tasksExecuted, device, section, hardware, boardOperations, hardwardId, nextQueueOperation);
    }


    @Override
    public String toString() {
        return "{" +
            " boardId='" + getBoardId() + "'" +
            ", boardKey='" + getBoardKey() + "'" +
            ", name='" + getName() + "'" +
            ", ssid='" + getSsid() + "'" +
            ", macAddress='" + getMacAddress() + "'" +
            ", firmwareVersion='" + getFirmwareVersion() + "'" +
            ", firmware='" + getFirmware() + "'" +
            ", ip='" + getIp() + "'" +
            ", status='" + isStatus() + "'" +
            ", arest='" + isArest() + "'" +
            ", arestCommand='" + isArestCommand() + "'" +
            ", socket='" + isSocket() + "'" +
            ", periodicCheck='" + getPeriodicCheck() + "'" +
            ", ramUsage='" + getRamUsage() + "'" +
            ", heap='" + getHeap() + "'" +
            ", heapTotal='" + getHeapTotal() + "'" +
            ", millis='" + getMillis() + "'" +
            ", currentMillis='" + getCurrentMillis() + "'" +
            ", activated='" + isActivated() + "'" +
            ", websocketId='" + getWebsocketId() + "'" +
            ", devMode='" + isDevMode() + "'" +
            ", devServerUrl='" + getDevServerUrl() + "'" +
            ", devWsUrl='" + getDevWsUrl() + "'" +
            ", lastConnectDateTime='" + getLastConnectDateTime() + "'" +
            ", lastLoginDateTime='" + getLastLoginDateTime() + "'" +
            ", activatedDateTime='" + getActivatedDateTime() + "'" +
            ", timeout='" + getTimeout() + "'" +
            ", restartTimeout='" + isRestartTimeout() + "'" +
            ", offline='" + getOffline() + "'" +
            ", tasksExecuted='" + getTasksExecuted() + "'" +
            ", device='" + getDevice() + "'" +
            ", section='" + getSection() + "'" +
            ", hardware='" + getHardware() + "'" +
            ", boardOperations='" + getBoardOperations() + "'" +
            ", hardwardId='" + getHardwardId() + "'" +
            ", nextQueueOperation='" + getNextQueueOperation() + "'" +
            "}";
    }
    

   


   
}
