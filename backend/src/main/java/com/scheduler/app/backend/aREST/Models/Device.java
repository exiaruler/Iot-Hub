package com.scheduler.app.backend.aREST.Models;
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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.scheduler.Base.ModelBase.ModelBase;

@Entity
@Table(indexes = @Index(columnList = "name,device_id"))
public class Device extends ModelBase{

    // device which the board is belong to
    @ManyToOne
    @JoinColumn(name="board_id")
    @JsonBackReference("device-board")
    private Board board;
    // boardId+id
    @Column(name="device_id")
    private String deviceId;
    // name of device
    @Column
    private String name;
    // state which the device is in (redundent)
    @Column
    private String state;
    // error from device
    @Column
    private String warning;
    // device type optional
    @Column 
    private String type;
    // subtype of device optional
    @Column 
    private String subtype;
    // followed arest v2 framework
    @Column
    private boolean frameworkFollowed;
    // custom device framework
    @Column
    private boolean custom;
    // switch device type
    @Column
    private boolean switchDevice=false;
    // animation route active
    @Column
    private boolean animationActive=false;
    // list of routes
    @JsonManagedReference("device-routes")
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "device", cascade =CascadeType.ALL)
    private List<Route> routes=new ArrayList<>();
    // list of schedule task
    @JsonManagedReference("device-schedules")
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "device", cascade =CascadeType.ALL)
    private List<Schedule> schedules=new ArrayList<>();
    // list of components that device uses. mapping purpose
    @JsonManagedReference("device-components")
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "device",cascade =CascadeType.ALL)
    private List<Component> components=new ArrayList<>();
  

    public Device() {
    }

    public Device(Board board, String deviceId, String name, String state, String warning, String type, String subtype, boolean frameworkFollowed, boolean custom, boolean switchDevice, boolean animationActive, List<Route> routes, List<Schedule> schedules, List<Component> components) {
        this.board = board;
        this.deviceId = deviceId;
        this.name = name;
        this.state = state;
        this.warning = warning;
        this.type = type;
        this.subtype = subtype;
        this.frameworkFollowed = frameworkFollowed;
        this.custom = custom;
        this.switchDevice = switchDevice;
        this.animationActive = animationActive;
        this.routes = routes;
        this.schedules = schedules;
        this.components = components;
    }

    public Board getBoard() {
        return this.board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getWarning() {
        return this.warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return this.subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public boolean isFrameworkFollowed() {
        return this.frameworkFollowed;
    }

    public boolean getFrameworkFollowed() {
        return this.frameworkFollowed;
    }

    public void setFrameworkFollowed(boolean frameworkFollowed) {
        this.frameworkFollowed = frameworkFollowed;
    }

    public boolean isCustom() {
        return this.custom;
    }

    public boolean getCustom() {
        return this.custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
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

    public boolean isAnimationActive() {
        return this.animationActive;
    }

    public boolean getAnimationActive() {
        return this.animationActive;
    }

    public void setAnimationActive(boolean animationActive) {
        this.animationActive = animationActive;
    }

    public List<Route> getRoutes() {
        return this.routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public List<Schedule> getSchedules() {
        return this.schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public List<Component> getComponents() {
        return this.components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public Device board(Board board) {
        setBoard(board);
        return this;
    }

    public Device deviceId(String deviceId) {
        setDeviceId(deviceId);
        return this;
    }

    public Device name(String name) {
        setName(name);
        return this;
    }

    public Device state(String state) {
        setState(state);
        return this;
    }

    public Device warning(String warning) {
        setWarning(warning);
        return this;
    }

    public Device type(String type) {
        setType(type);
        return this;
    }

    public Device subtype(String subtype) {
        setSubtype(subtype);
        return this;
    }

    public Device frameworkFollowed(boolean frameworkFollowed) {
        setFrameworkFollowed(frameworkFollowed);
        return this;
    }

    public Device custom(boolean custom) {
        setCustom(custom);
        return this;
    }

    public Device switchDevice(boolean switchDevice) {
        setSwitchDevice(switchDevice);
        return this;
    }

    public Device animationActive(boolean animationActive) {
        setAnimationActive(animationActive);
        return this;
    }

    public Device routes(List<Route> routes) {
        setRoutes(routes);
        return this;
    }

    public Device schedules(List<Schedule> schedules) {
        setSchedules(schedules);
        return this;
    }

    public Device components(List<Component> components) {
        setComponents(components);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Device)) {
            return false;
        }
        Device device = (Device) o;
        return Objects.equals(board, device.board) && Objects.equals(deviceId, device.deviceId) && Objects.equals(name, device.name) && Objects.equals(state, device.state) && Objects.equals(warning, device.warning) && Objects.equals(type, device.type) && Objects.equals(subtype, device.subtype) && frameworkFollowed == device.frameworkFollowed && custom == device.custom && switchDevice == device.switchDevice && animationActive == device.animationActive && Objects.equals(routes, device.routes) && Objects.equals(schedules, device.schedules) && Objects.equals(components, device.components);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, deviceId, name, state, warning, type, subtype, frameworkFollowed, custom, switchDevice, animationActive, routes, schedules, components);
    }

    @Override
    public String toString() {
        return "{" +
            " board='" + getBoard() + "'" +
            ", deviceId='" + getDeviceId() + "'" +
            ", name='" + getName() + "'" +
            ", state='" + getState() + "'" +
            ", warning='" + getWarning() + "'" +
            ", type='" + getType() + "'" +
            ", subtype='" + getSubtype() + "'" +
            ", frameworkFollowed='" + isFrameworkFollowed() + "'" +
            ", custom='" + isCustom() + "'" +
            ", switchDevice='" + isSwitchDevice() + "'" +
            ", animationActive='" + isAnimationActive() + "'" +
            ", routes='" + getRoutes() + "'" +
            ", schedules='" + getSchedules() + "'" +
            ", components='" + getComponents() + "'" +
            "}";
    }
   


}
