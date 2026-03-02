package com.scheduler.app.backend.Command.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.scheduler.Base.ModelBase.ModelBase;
import com.scheduler.app.backend.Messaging.Models.BoardTask;
import com.scheduler.app.backend.aREST.Models.Route;
// store route method and class origin
@Entity
@Table(indexes = @Index(columnList = "command"))
public class Command extends ModelBase{
    // type of command
    @Column
    private String commandType;
    // display name
    private String displayName="";
    // route or command name 
    @Column
    private String command;
    // does it have params
    @Column
    private boolean params=false;
    // total number params
    @Column
    private int totalParam=0;
    // has motor or servo
    @Column
    private boolean hasMotor=false;
    // is a system command
    @Column boolean systemCommand=false;
    // list of routes that use command
    //@JsonIgnore
    @JsonManagedReference("command-route")
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "command", cascade =CascadeType.ALL)
    private List<Route> commandUsedRoutes=new ArrayList<>();
    // list of parameters used for this command
    @JsonManagedReference("command-commandParameter")
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "command", cascade =CascadeType.ALL)
    private List<CommandParameter> commandParameter=new ArrayList<>();
    // command 
    @JsonManagedReference("command-boardtask")
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "command", cascade = CascadeType.ALL)
    private BoardTask boardCommand;


    public Command() {
    }

    public Command(String commandType, String displayName, String command, boolean params, int totalParam, boolean hasMotor, boolean systemCommand, List<Route> commandUsedRoutes, List<CommandParameter> commandParameter, BoardTask boardCommand) {
        this.commandType = commandType;
        this.displayName = displayName;
        this.command = command;
        this.params = params;
        this.totalParam = totalParam;
        this.hasMotor = hasMotor;
        this.systemCommand = systemCommand;
        this.commandUsedRoutes = commandUsedRoutes;
        this.commandParameter = commandParameter;
        this.boardCommand = boardCommand;
    }

    public String getCommandType() {
        return this.commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public boolean isParams() {
        return this.params;
    }

    public boolean getParams() {
        return this.params;
    }

    public void setParams(boolean params) {
        this.params = params;
    }

    public int getTotalParam() {
        return this.totalParam;
    }

    public void setTotalParam(int totalParam) {
        this.totalParam = totalParam;
    }

    public boolean isHasMotor() {
        return this.hasMotor;
    }

    public boolean getHasMotor() {
        return this.hasMotor;
    }

    public void setHasMotor(boolean hasMotor) {
        this.hasMotor = hasMotor;
    }

    public boolean isSystemCommand() {
        return this.systemCommand;
    }

    public boolean getSystemCommand() {
        return this.systemCommand;
    }

    public void setSystemCommand(boolean systemCommand) {
        this.systemCommand = systemCommand;
    }

    public List<Route> getCommandUsedRoutes() {
        return this.commandUsedRoutes;
    }

    public void setCommandUsedRoutes(List<Route> commandUsedRoutes) {
        this.commandUsedRoutes = commandUsedRoutes;
    }

    public List<CommandParameter> getCommandParameter() {
        return this.commandParameter;
    }

    public void setCommandParameter(List<CommandParameter> commandParameter) {
        this.commandParameter = commandParameter;
    }

    public BoardTask getBoardCommand() {
        return this.boardCommand;
    }

    public void setBoardCommand(BoardTask boardCommand) {
        this.boardCommand = boardCommand;
    }

    public Command commandType(String commandType) {
        setCommandType(commandType);
        return this;
    }

    public Command displayName(String displayName) {
        setDisplayName(displayName);
        return this;
    }

    public Command command(String command) {
        setCommand(command);
        return this;
    }

    public Command params(boolean params) {
        setParams(params);
        return this;
    }

    public Command totalParam(int totalParam) {
        setTotalParam(totalParam);
        return this;
    }

    public Command hasMotor(boolean hasMotor) {
        setHasMotor(hasMotor);
        return this;
    }

    public Command systemCommand(boolean systemCommand) {
        setSystemCommand(systemCommand);
        return this;
    }

    public Command commandUsedRoutes(List<Route> commandUsedRoutes) {
        setCommandUsedRoutes(commandUsedRoutes);
        return this;
    }

    public Command commandParameter(List<CommandParameter> commandParameter) {
        setCommandParameter(commandParameter);
        return this;
    }

    public Command boardCommand(BoardTask boardCommand) {
        setBoardCommand(boardCommand);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Command)) {
            return false;
        }
        Command command = (Command) o;
        return Objects.equals(commandType, command.commandType) && Objects.equals(displayName, command.displayName) && Objects.equals(command, command.command) && params == command.params && totalParam == command.totalParam && hasMotor == command.hasMotor && systemCommand == command.systemCommand && Objects.equals(commandUsedRoutes, command.commandUsedRoutes) && Objects.equals(commandParameter, command.commandParameter) && Objects.equals(boardCommand, command.boardCommand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandType, displayName, command, params, totalParam, hasMotor, systemCommand, commandUsedRoutes, commandParameter, boardCommand);
    }

    @Override
    public String toString() {
        return "{" +
            " commandType='" + getCommandType() + "'" +
            ", displayName='" + getDisplayName() + "'" +
            ", command='" + getCommand() + "'" +
            ", params='" + isParams() + "'" +
            ", totalParam='" + getTotalParam() + "'" +
            ", hasMotor='" + isHasMotor() + "'" +
            ", systemCommand='" + isSystemCommand() + "'" +
            ", commandUsedRoutes='" + getCommandUsedRoutes() + "'" +
            ", commandParameter='" + getCommandParameter() + "'" +
            ", boardCommand='" + getBoardCommand() + "'" +
            "}";
    }    
    
}
