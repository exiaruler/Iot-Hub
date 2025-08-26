package com.scheduler.app.backend.Messaging.Board;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.scheduler.app.backend.aREST.Models.Task;

public class SentToClientResponse {
    // command sent
    private boolean sent;
    // next tasks
    private List <Task> nextTasks=new ArrayList<>();

    public SentToClientResponse() {
    }

    public SentToClientResponse(boolean sent, List<Task> nextTasks) {
        this.sent = sent;
        this.nextTasks = nextTasks;
    }

    public boolean isSent() {
        return this.sent;
    }

    public boolean getSent() {
        return this.sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public List<Task> getNextTasks() {
        return this.nextTasks;
    }

    public void setNextTasks(List<Task> nextTasks) {
        this.nextTasks = nextTasks;
    }

    public SentToClientResponse sent(boolean sent) {
        setSent(sent);
        return this;
    }

    public SentToClientResponse nextTasks(List<Task> nextTasks) {
        setNextTasks(nextTasks);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof SentToClientResponse)) {
            return false;
        }
        SentToClientResponse sentToClientResponse = (SentToClientResponse) o;
        return sent == sentToClientResponse.sent && Objects.equals(nextTasks, sentToClientResponse.nextTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sent, nextTasks);
    }

    @Override
    public String toString() {
        return "{" +
            " sent='" + isSent() + "'" +
            ", nextTasks='" + getNextTasks() + "'" +
            "}";
    }
    
    
}
