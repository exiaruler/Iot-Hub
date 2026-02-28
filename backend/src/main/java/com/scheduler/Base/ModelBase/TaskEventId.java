package com.scheduler.Base.ModelBase;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TaskEventId implements Serializable {
    // board id
    @Column(nullable = false)
    private long boardId;
    // device id
    @Column
    private long deviceId=0;
    // timestamp of event
    @Column(nullable = false)
    private Instant eventTime;

    public TaskEventId() {
    }

    public TaskEventId(long boardId, long deviceId) {
        this.boardId = boardId;
        this.deviceId = deviceId;
        this.eventTime = Instant.now();
    }

    public long getBoardId() {
        return this.boardId;
    }

    public void setBoardId(long boardId) {
        this.boardId = boardId;
    }

    public long getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public Instant getEventTime() {
        return this.eventTime;
    }

    public void setEventTime(Instant eventTime) {
        this.eventTime = eventTime;
    }

    public TaskEventId boardId(long boardId) {
        setBoardId(boardId);
        return this;
    }

    public TaskEventId deviceId(long deviceId) {
        setDeviceId(deviceId);
        return this;
    }

    public TaskEventId eventTime(Instant eventTime) {
        setEventTime(eventTime);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof TaskEventId)) {
            return false;
        }
        TaskEventId taskEventId = (TaskEventId) o;
        return boardId == taskEventId.boardId && deviceId == taskEventId.deviceId && Objects.equals(eventTime, taskEventId.eventTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardId, deviceId, eventTime);
    }

    @Override
    public String toString() {
        return "{" +
            " boardId='" + getBoardId() + "'" +
            ", deviceId='" + getDeviceId() + "'" +
            ", eventTime='" + getEventTime() + "'" +
            "}";
    }
    

}
