package com.scheduler.app.backend.Hardware.Models;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.scheduler.Base.ModelBase.ModelBase;

@Entity
public class HardwarePins extends ModelBase {
    
    @ManyToOne
    @JoinColumn(name="hardware_id")
    @JsonBackReference
    private Hardware hardware;
    
    // pin label on board
    @Column
    private String boardPin;
    // GPIO pin
    @Column
    private int pin;


    public HardwarePins() {
    }

    public HardwarePins(Hardware hardware, String boardPin, int pin) {
        this.hardware = hardware;
        this.boardPin = boardPin;
        this.pin = pin;
    }

    public Hardware getHardware() {
        return this.hardware;
    }

    public void setHardware(Hardware hardware) {
        this.hardware = hardware;
    }

    public String getBoardPin() {
        return this.boardPin;
    }

    public void setBoardPin(String boardPin) {
        this.boardPin = boardPin;
    }

    public int getPin() {
        return this.pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public HardwarePins hardware(Hardware hardware) {
        setHardware(hardware);
        return this;
    }

    public HardwarePins boardPin(String boardPin) {
        setBoardPin(boardPin);
        return this;
    }

    public HardwarePins pin(int pin) {
        setPin(pin);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof HardwarePins)) {
            return false;
        }
        HardwarePins hardwarePins = (HardwarePins) o;
        return Objects.equals(hardware, hardwarePins.hardware) && Objects.equals(boardPin, hardwarePins.boardPin) && pin == hardwarePins.pin;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hardware, boardPin, pin);
    }

    @Override
    public String toString() {
        return "{" +
            " hardware='" + getHardware() + "'" +
            ", boardPin='" + getBoardPin() + "'" +
            ", pin='" + getPin() + "'" +
            "}";
    }
    
    
}
