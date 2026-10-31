package com.scheduler.app.backend.Task.Model;

import java.util.Objects;

import com.scheduler.app.backend.aREST.Models.Device;
import com.scheduler.app.backend.Task.Model.Task;

public class CompletedTask {
    private Device device;
    private String statusString;
    private String warning;
    private boolean status;
    private boolean oneTimeJob=true;
    private Task task;

    public CompletedTask() {
    }

    public CompletedTask(Device device, String statusString, String warning, boolean status, boolean oneTimeJob, Task task) {
        this.device = device;
        this.statusString = statusString;
        this.warning = warning;
        this.status = status;
        this.oneTimeJob = oneTimeJob;
        this.task = task;
    }

    public Device getDevice() {
        return this.device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getStatusString() {
        return this.statusString;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    public String getWarning() {
        return this.warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
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

    public boolean isOneTimeJob() {
        return this.oneTimeJob;
    }

    public boolean getOneTimeJob() {
        return this.oneTimeJob;
    }

    public void setOneTimeJob(boolean oneTimeJob) {
        this.oneTimeJob = oneTimeJob;
    }

    public Task getTask() {
        return this.task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public CompletedTask device(Device device) {
        setDevice(device);
        return this;
    }

    public CompletedTask statusString(String statusString) {
        setStatusString(statusString);
        return this;
    }

    public CompletedTask warning(String warning) {
        setWarning(warning);
        return this;
    }

    public CompletedTask status(boolean status) {
        setStatus(status);
        return this;
    }

    public CompletedTask oneTimeJob(boolean oneTimeJob) {
        setOneTimeJob(oneTimeJob);
        return this;
    }

    public CompletedTask task(Task task) {
        setTask(task);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CompletedTask)) {
            return false;
        }
        CompletedTask completedTask = (CompletedTask) o;
        return Objects.equals(device, completedTask.device) && Objects.equals(statusString, completedTask.statusString) && Objects.equals(warning, completedTask.warning) && status == completedTask.status && oneTimeJob == completedTask.oneTimeJob && Objects.equals(task, completedTask.task);
    }

    @Override
    public int hashCode() {
        return Objects.hash(device, statusString, warning, status, oneTimeJob, task);
    }

    @Override
    public String toString() {
        return "{" +
            " device='" + getDevice() + "'" +
            ", statusString='" + getStatusString() + "'" +
            ", warning='" + getWarning() + "'" +
            ", status='" + isStatus() + "'" +
            ", oneTimeJob='" + isOneTimeJob() + "'" +
            ", task='" + getTask() + "'" +
            "}";
    }
    
    
    
}
