package com.scheduler.app.backend.aREST.Models;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.scheduler.Base.ModelBase.ModelBase;
import com.scheduler.app.backend.Command.Models.Command;
import com.scheduler.app.backend.Messaging.Models.BoardTask;
// device functions
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Route extends ModelBase{
    @JsonBackReference("device-routes")
    @ManyToOne
    @JoinColumn(name="device_id")
    private Device device;
    // route/function of the device
    @Column
    private String route;
    // true if the route has modes
    @Column 
    private boolean modes=false;
    // electrode
    @Column
    private String electrode="";
    // arest Command route
    @JsonBackReference("command-route")
    @ManyToOne
    @JoinColumn(name="command_id")
    private Command command;
    // command id
    private long commandId;
    // switch device type
    @Column
    private boolean switchDevice=false;
    // list of modes
    @JsonManagedReference("route-mode")
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "route", cascade =CascadeType.ALL)
    private List<Mode> mode;
    // Board Task
    @JsonManagedReference("boardtask-route")
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "route", cascade = CascadeType.ALL)
    private BoardTask boardAction;
    // routes that are scheduled
    @JsonManagedReference("route-schedule")
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "route", cascade =CascadeType.ALL)
    private List<Schedule> scheduledRoutes;



    public Route() {
    }

    public Route(Device device, String route, boolean modes, String electrode, Command command, long commandId, boolean switchDevice, List<Mode> mode, BoardTask boardAction, List<Schedule> scheduledRoutes) {
        this.device = device;
        this.route = route;
        this.modes = modes;
        this.electrode = electrode;
        this.command = command;
        this.commandId = commandId;
        this.switchDevice = switchDevice;
        this.mode = mode;
        this.boardAction = boardAction;
        this.scheduledRoutes = scheduledRoutes;
    }

    public Device getDevice() {
        return this.device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getRoute() {
        return this.route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public boolean isModes() {
        return this.modes;
    }

    public boolean getModes() {
        return this.modes;
    }

    public void setModes(boolean modes) {
        this.modes = modes;
    }

    public String getElectrode() {
        return this.electrode;
    }

    public void setElectrode(String electrode) {
        this.electrode = electrode;
    }

    public Command getCommand() {
        return this.command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public long getCommandId() {
        return this.commandId;
    }

    public void setCommandId(long commandId) {
        this.commandId = commandId;
    }

    public boolean isSwitchDevice() {
        return this.switchDevice;
    }

    public boolean getSwitchDevice() {
        return this.switchDevice;
    }

    public void setSwitchDevice(boolean switchDevice) {
        this.switchDevice = switchDevice;
    }

    public List<Mode> getMode() {
        return this.mode;
    }

    public void setMode(List<Mode> mode) {
        this.mode = mode;
    }

    public BoardTask getBoardAction() {
        return this.boardAction;
    }

    public void setBoardAction(BoardTask boardAction) {
        this.boardAction = boardAction;
    }

    public List<Schedule> getScheduledRoutes() {
        return this.scheduledRoutes;
    }

    public void setScheduledRoutes(List<Schedule> scheduledRoutes) {
        this.scheduledRoutes = scheduledRoutes;
    }

    public Route device(Device device) {
        setDevice(device);
        return this;
    }

    public Route route(String route) {
        setRoute(route);
        return this;
    }

    public Route modes(boolean modes) {
        setModes(modes);
        return this;
    }

    public Route electrode(String electrode) {
        setElectrode(electrode);
        return this;
    }

    public Route command(Command command) {
        setCommand(command);
        return this;
    }

    public Route commandId(long commandId) {
        setCommandId(commandId);
        return this;
    }

    public Route switchDevice(boolean switchDevice) {
        setSwitchDevice(switchDevice);
        return this;
    }

    public Route mode(List<Mode> mode) {
        setMode(mode);
        return this;
    }

    public Route boardAction(BoardTask boardAction) {
        setBoardAction(boardAction);
        return this;
    }

    public Route scheduledRoutes(List<Schedule> scheduledRoutes) {
        setScheduledRoutes(scheduledRoutes);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Route)) {
            return false;
        }
        Route route = (Route) o;
        return Objects.equals(device, route.device) && Objects.equals(route, route.route) && modes == route.modes && Objects.equals(electrode, route.electrode) && Objects.equals(command, route.command) && commandId == route.commandId && switchDevice == route.switchDevice && Objects.equals(mode, route.mode) && Objects.equals(boardAction, route.boardAction) && Objects.equals(scheduledRoutes, route.scheduledRoutes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(device, route, modes, electrode, command, commandId, switchDevice, mode, boardAction, scheduledRoutes);
    }

    @Override
    public String toString() {
        return "{" +
            " device='" + getDevice() + "'" +
            ", route='" + getRoute() + "'" +
            ", modes='" + isModes() + "'" +
            ", electrode='" + getElectrode() + "'" +
            ", command='" + getCommand() + "'" +
            ", commandId='" + getCommandId() + "'" +
            ", switchDevice='" + isSwitchDevice() + "'" +
            ", mode='" + getMode() + "'" +
            ", boardAction='" + getBoardAction() + "'" +
            ", scheduledRoutes='" + getScheduledRoutes() + "'" +
            "}";
    }
    
}