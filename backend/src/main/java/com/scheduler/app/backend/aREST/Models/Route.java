package com.scheduler.app.backend.aREST.Models;
import java.util.ArrayList;
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
import com.scheduler.app.backend.Messaging.Models.InputCurrent;
import com.scheduler.app.backend.Messaging.Models.OutputCurrent;
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
    private List<Mode> mode=new ArrayList<>();
    // Board Task
    @JsonManagedReference("boardtask-route")
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "route", cascade = CascadeType.ALL)
    private BoardTask boardAction;
    // routes that are scheduled
    @JsonManagedReference("route-schedule")
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "route", cascade =CascadeType.ALL)
    private List<Schedule> scheduledRoutes=new ArrayList<>();



    public Route() {
    }
    // calculate current to save
    public void calculateCurrent(){
        String elect=this.getElectrode();
        if(!elect.equals("")){
            BoardTask funcTask=this.getBoardAction();
            if(funcTask!=null){
                int length=funcTask.getOutput().size();
                int intputLength=funcTask.getInput().size();
                if(intputLength>length){
                    length=intputLength;
                }
                for(int i=0; i<length; i++){
                    if(i<funcTask.getOutput().size()){
                        OutputCurrent oc=funcTask.getOutput().get(i);
                        oc.setCurrent(electrodeCalculate(elect, oc.getCurrent()));
                        funcTask.getOutput().set(i, oc);
                    }
                    if(i<funcTask.getInput().size()){
                        InputCurrent oc=funcTask.getInput().get(i);
                        oc.setCurrent(electrodeCalculate(elect, oc.getCurrent()));
                        funcTask.getInput().set(i, oc);
                    }
                }
                
            }
            this.boardAction=funcTask;
        }
        // loop over modes
            List<Mode> modes=this.getMode();
            if(modes!=null&&modes.size()>0){
                for(Mode m:modes){
                    BoardTask modeTask=m.getBoardAction();
                    if(modeTask!=null){
                        int length=modeTask.getOutput().size();
                        int intputLength=modeTask.getInput().size();
                        if(intputLength>length){
                            length=intputLength;
                        }
                        for(int i=0; i<length; i++){
                            if(i<modeTask.getOutput().size()){
                                OutputCurrent oc=modeTask.getOutput().get(i);
                                oc.setCurrent(electrodeCalculate(elect, oc.getCurrent()));
                                modeTask.getOutput().set(i, oc);
                            }
                            if(i<modeTask.getInput().size()){
                                InputCurrent oc=modeTask.getInput().get(i);
                                oc.setCurrent(electrodeCalculate(elect, oc.getCurrent()));
                                modeTask.getInput().set(i, oc);
                            }
                        }
                        
                    }
                    m.setBoardAction(modeTask);
                }
                this.setMode(modes);
            }
    }
    // calculate output display for frontend
    public void calculateOutputDisplay(){
        String elect=this.getElectrode();
        if(!elect.equals("")){
            BoardTask funcTask=this.getBoardAction();
            if(funcTask!=null){
                int length=funcTask.getOutput().size();
                int intputLength=funcTask.getInput().size();
                if(intputLength>length){
                    length=intputLength;
                }
                for(int i=0; i<length; i++){
                    if(i<funcTask.getOutput().size()){
                        OutputCurrent oc=funcTask.getOutput().get(i);
                        oc.setCurrent(electrodeCalculateDisplay(elect, oc.getCurrent()));
                        funcTask.getOutput().set(i, oc);
                    }
                    if(i<funcTask.getInput().size()){
                        InputCurrent oc=funcTask.getInput().get(i);
                        oc.setCurrent(electrodeCalculateDisplay(elect, oc.getCurrent()));
                        funcTask.getInput().set(i, oc);
                    }
                }
                
            }
            this.boardAction=funcTask;
        }
        // loop over modes
            List<Mode> modes=this.getMode();
            if(modes!=null&&modes.size()>0){
                for(Mode m:modes){
                    BoardTask modeTask=m.getBoardAction();
                    if(modeTask!=null){
                        int length=modeTask.getOutput().size();
                        int intputLength=modeTask.getInput().size();
                        if(intputLength>length){
                            length=intputLength;
                        }
                        for(int i=0; i<length; i++){
                            if(i<modeTask.getOutput().size()){
                                OutputCurrent oc=modeTask.getOutput().get(i);
                                oc.setCurrent(electrodeCalculateDisplay(elect, oc.getCurrent()));
                                modeTask.getOutput().set(i, oc);
                            }
                            if(i<modeTask.getInput().size()){
                                InputCurrent oc=modeTask.getInput().get(i);
                                oc.setCurrent(electrodeCalculateDisplay(elect, oc.getCurrent()));
                                modeTask.getInput().set(i, oc);
                            }
                        }
                        
                    }
                    m.setBoardAction(modeTask);
                }
                this.setMode(modes);
            }
    }
    private int electrodeCalculate(String electrode,int current){
        if(electrode.equals("cathode")){
            current=255-current;
        }   
        return current;
    }
    private int electrodeCalculateDisplay(String electrode,int current){
        if(electrode.equals("cathode")){
            current+=255;
        }   
        return current;
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