package com.scheduler.app.backend.Messaging.Models;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.scheduler.Base.ModelBase.TaskModelBase;
// rgb pins
@Entity
@Table(name="Board_pin")
public class BoardPin extends TaskModelBase {
    @ManyToOne
    @JoinColumn(name="board_task_id")
    @JsonBackReference("boardtask-pins")
    private BoardTask boardTask;
    @Column
    private int pin;
    @Column
    private int pinOrder;


    public BoardPin() {
    }

    public BoardPin(BoardTask boardTask, int pin, int pinOrder) {
        this.boardTask = boardTask;
        this.pin = pin;
        this.pinOrder = pinOrder;
    }

    public BoardTask getBoardTask() {
        return this.boardTask;
    }

    public void setBoardTask(BoardTask boardTask) {
        this.boardTask = boardTask;
    }

    public int getPin() {
        return this.pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public int getPinOrder() {
        return this.pinOrder;
    }

    public void setPinOrder(int pinOrder) {
        this.pinOrder = pinOrder;
    }

    public BoardPin boardTask(BoardTask boardTask) {
        setBoardTask(boardTask);
        return this;
    }

    public BoardPin pin(int pin) {
        setPin(pin);
        return this;
    }

    public BoardPin pinOrder(int pinOrder) {
        setPinOrder(pinOrder);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof BoardPin)) {
            return false;
        }
        BoardPin boardPin = (BoardPin) o;
        return Objects.equals(boardTask, boardPin.boardTask) && pin == boardPin.pin && pinOrder == boardPin.pinOrder;
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardTask, pin, pinOrder);
    }

    @Override
    public String toString() {
        return "{" +
            " boardTask='" + getBoardTask() + "'" +
            ", pin='" + getPin() + "'" +
            ", pinOrder='" + getPinOrder() + "'" +
            "}";
    }

    
    
}
