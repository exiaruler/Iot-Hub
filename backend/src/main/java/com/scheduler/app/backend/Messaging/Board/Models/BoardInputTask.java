package com.scheduler.app.backend.Messaging.Board.Models;
import java.util.Objects;
// board task
public class BoardInputTask {
    // command exection sucess
    private boolean success;
    // taskId
    private long taskId;
    // position of task in array
    private int taskIndex;
    // system queue
    private boolean systemQueue;
    // normal queue
    private boolean queue;
    // data sent back
    private String data;


    public BoardInputTask() {
    }

    public BoardInputTask(boolean success, long taskId, int taskIndex, boolean systemQueue, boolean queue, String data) {
        this.success = success;
        this.taskId = taskId;
        this.taskIndex = taskIndex;
        this.systemQueue = systemQueue;
        this.queue = queue;
        this.data = data;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public int getTaskIndex() {
        return this.taskIndex;
    }

    public void setTaskIndex(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    public boolean isSystemQueue() {
        return this.systemQueue;
    }

    public boolean getSystemQueue() {
        return this.systemQueue;
    }

    public void setSystemQueue(boolean systemQueue) {
        this.systemQueue = systemQueue;
    }

    public boolean isQueue() {
        return this.queue;
    }

    public boolean getQueue() {
        return this.queue;
    }

    public void setQueue(boolean queue) {
        this.queue = queue;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public BoardInputTask success(boolean success) {
        setSuccess(success);
        return this;
    }

    public BoardInputTask taskId(long taskId) {
        setTaskId(taskId);
        return this;
    }

    public BoardInputTask taskIndex(int taskIndex) {
        setTaskIndex(taskIndex);
        return this;
    }

    public BoardInputTask systemQueue(boolean systemQueue) {
        setSystemQueue(systemQueue);
        return this;
    }

    public BoardInputTask queue(boolean queue) {
        setQueue(queue);
        return this;
    }

    public BoardInputTask data(String data) {
        setData(data);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof BoardInputTask)) {
            return false;
        }
        BoardInputTask boardInputTask = (BoardInputTask) o;
        return success == boardInputTask.success && taskId == boardInputTask.taskId && taskIndex == boardInputTask.taskIndex && systemQueue == boardInputTask.systemQueue && queue == boardInputTask.queue && Objects.equals(data, boardInputTask.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, taskId, taskIndex, systemQueue, queue, data);
    }

    @Override
    public String toString() {
        return "{" +
            " success='" + isSuccess() + "'" +
            ", taskId='" + getTaskId() + "'" +
            ", taskIndex='" + getTaskIndex() + "'" +
            ", systemQueue='" + isSystemQueue() + "'" +
            ", queue='" + isQueue() + "'" +
            ", data='" + getData() + "'" +
            "}";
    }

}
