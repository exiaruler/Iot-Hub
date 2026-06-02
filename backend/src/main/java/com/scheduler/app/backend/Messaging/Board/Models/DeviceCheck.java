package com.scheduler.app.backend.Messaging.Board.Models;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.scheduler.app.backend.Messaging.Board.Models.ArraySerial.BoardTaskSerial;
// use for routine checks
@JsonPropertyOrder({"taskTotal","tasks"})
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public class DeviceCheck{
    
    // websocket headers/ be in loging
    // http headers/ be in loing
    // total number of tasks
    private int taskTotal;
    // command tasks
    private List<BoardTaskSerial> tasks=new ArrayList<>();
    
    public DeviceCheck() {
    }

    public DeviceCheck(int taskTotal, List<BoardTaskSerial> tasks) {
        this.taskTotal = taskTotal;
        this.tasks = tasks;
    }

    public int getTaskTotal() {
        return this.taskTotal;
    }

    public void setTaskTotal(int taskTotal) {
        this.taskTotal = taskTotal;
    }

    public List<BoardTaskSerial> getTasks() {
        return this.tasks;
    }

    public void setTasks(List<BoardTaskSerial> tasks) {
        this.setTaskTotal(tasks.size());
        this.tasks = tasks;
    }

    public DeviceCheck taskTotal(int taskTotal) {
        setTaskTotal(taskTotal);
        return this;
    }

    public DeviceCheck tasks(List<BoardTaskSerial> tasks) {
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
        return taskTotal == deviceCheck.taskTotal && Objects.equals(tasks, deviceCheck.tasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskTotal, tasks);
    }

    @Override
    public String toString() {
        return "{" +
            " taskTotal='" + getTaskTotal() + "'" +
            ", tasks='" + getTasks() + "'" +
            "}";
    }
   
    

}
