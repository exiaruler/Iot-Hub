package com.scheduler.Base.ModelBase;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.scheduler.app.backend.aREST.Models.Board;
import com.scheduler.app.backend.aREST.Models.Device;

@Embeddable
public class QueueEventId implements Serializable  {
    // board id
    private Board board;
    // device id
    private Device device=null;
    // timestamp of event
    @Column(nullable = false)
    private Instant eventTime;


    public QueueEventId() {
    }

    public QueueEventId(Board board, Device device) {
        this.board = board;
        this.device = device;
        this.eventTime = Instant.now();
    }

    public Board getBoard() {
        return this.board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Device getDevice() {
        return this.device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Instant getEventTime() {
        return this.eventTime;
    }

    public void setEventTime(Instant eventTime) {
        this.eventTime = eventTime;
    }

    public QueueEventId board(Board board) {
        setBoard(board);
        return this;
    }

    public QueueEventId device(Device device) {
        setDevice(device);
        return this;
    }

    public QueueEventId eventTime(Instant eventTime) {
        setEventTime(eventTime);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof QueueEventId)) {
            return false;
        }
        QueueEventId queueEventId = (QueueEventId) o;
        return Objects.equals(board, queueEventId.board) && Objects.equals(device, queueEventId.device) && Objects.equals(eventTime, queueEventId.eventTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, device, eventTime);
    }

    @Override
    public String toString() {
        return "{" +
            " board='" + getBoard() + "'" +
            ", device='" + getDevice() + "'" +
            ", eventTime='" + getEventTime() + "'" +
            "}";
    }
    
}
