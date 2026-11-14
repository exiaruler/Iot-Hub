package com.scheduler.app.backend.aREST.Models;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.scheduler.Base.ModelBase.ModelBase;

// component of device. not use within the application but for mapping data structure for aREST v2.
@Entity
public class Component extends ModelBase {
    @JsonBackReference("device-components")
    @ManyToOne
    @JoinColumn(name="device_id")
    private Device device;
    // part name
    @Column
    private String part="";
    @Column
    private String pin="0";
    @Column
    private boolean rgb=false;
    @Column
    private boolean main=false;
    @Column
    private String rgbRed="";
    @Column
    private String rgbBlue="";
    @Column
    private String rgbGreen="";
    @Column
    private boolean motor=false;
    @Column
    private boolean pinmodeOut=false;
    @Column
    private boolean pinmodeIn=false;
    @Column
    private String servo="";
    @Column
    private boolean active=false;
    @Column
    private String status="";
    

    public Component() {
    }

    public Component(Device device, String part, String pin, boolean rgb, boolean main, String rgbRed, String rgbBlue, String rgbGreen, boolean motor, boolean pinmodeOut, boolean pinmodeIn, String servo, boolean active, String status) {
        this.device = device;
        this.part = part;
        this.pin = pin;
        this.rgb = rgb;
        this.main = main;
        this.rgbRed = rgbRed;
        this.rgbBlue = rgbBlue;
        this.rgbGreen = rgbGreen;
        this.motor = motor;
        this.pinmodeOut = pinmodeOut;
        this.pinmodeIn = pinmodeIn;
        this.servo = servo;
        this.active = active;
        this.status = status;
    }

    public Device getDevice() {
        return this.device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getPart() {
        return this.part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getPin() {
        return this.pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public boolean isRgb() {
        return this.rgb;
    }

    public boolean getRgb() {
        return this.rgb;
    }

    public void setRgb(boolean rgb) {
        this.rgb = rgb;
    }

    public boolean isMain() {
        return this.main;
    }

    public boolean getMain() {
        return this.main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }

    public String getRgbRed() {
        return this.rgbRed;
    }

    public void setRgbRed(String rgbRed) {
        this.rgbRed = rgbRed;
    }

    public String getRgbBlue() {
        return this.rgbBlue;
    }

    public void setRgbBlue(String rgbBlue) {
        this.rgbBlue = rgbBlue;
    }

    public String getRgbGreen() {
        return this.rgbGreen;
    }

    public void setRgbGreen(String rgbGreen) {
        this.rgbGreen = rgbGreen;
    }

    public boolean isMotor() {
        return this.motor;
    }

    public boolean getMotor() {
        return this.motor;
    }

    public void setMotor(boolean motor) {
        this.motor = motor;
    }

    public boolean isPinmodeOut() {
        return this.pinmodeOut;
    }

    public boolean getPinmodeOut() {
        return this.pinmodeOut;
    }

    public void setPinmodeOut(boolean pinmodeOut) {
        this.pinmodeOut = pinmodeOut;
    }

    public boolean isPinmodeIn() {
        return this.pinmodeIn;
    }

    public boolean getPinmodeIn() {
        return this.pinmodeIn;
    }

    public void setPinmodeIn(boolean pinmodeIn) {
        this.pinmodeIn = pinmodeIn;
    }

    public String getServo() {
        return this.servo;
    }

    public void setServo(String servo) {
        this.servo = servo;
    }

    public boolean isActive() {
        return this.active;
    }

    public boolean getActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Component device(Device device) {
        setDevice(device);
        return this;
    }

    public Component part(String part) {
        setPart(part);
        return this;
    }

    public Component pin(String pin) {
        setPin(pin);
        return this;
    }

    public Component rgb(boolean rgb) {
        setRgb(rgb);
        return this;
    }

    public Component main(boolean main) {
        setMain(main);
        return this;
    }

    public Component rgbRed(String rgbRed) {
        setRgbRed(rgbRed);
        return this;
    }

    public Component rgbBlue(String rgbBlue) {
        setRgbBlue(rgbBlue);
        return this;
    }

    public Component rgbGreen(String rgbGreen) {
        setRgbGreen(rgbGreen);
        return this;
    }

    public Component motor(boolean motor) {
        setMotor(motor);
        return this;
    }

    public Component pinmodeOut(boolean pinmodeOut) {
        setPinmodeOut(pinmodeOut);
        return this;
    }

    public Component pinmodeIn(boolean pinmodeIn) {
        setPinmodeIn(pinmodeIn);
        return this;
    }

    public Component servo(String servo) {
        setServo(servo);
        return this;
    }

    public Component active(boolean active) {
        setActive(active);
        return this;
    }

    public Component status(String status) {
        setStatus(status);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Component)) {
            return false;
        }
        Component component = (Component) o;
        return Objects.equals(device, component.device) && Objects.equals(part, component.part) && Objects.equals(pin, component.pin) && rgb == component.rgb && main == component.main && Objects.equals(rgbRed, component.rgbRed) && Objects.equals(rgbBlue, component.rgbBlue) && Objects.equals(rgbGreen, component.rgbGreen) && motor == component.motor && pinmodeOut == component.pinmodeOut && pinmodeIn == component.pinmodeIn && Objects.equals(servo, component.servo) && active == component.active && Objects.equals(status, component.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(device, part, pin, rgb, main, rgbRed, rgbBlue, rgbGreen, motor, pinmodeOut, pinmodeIn, servo, active, status);
    }

    @Override
    public String toString() {
        return "{" +
            " device='" + getDevice() + "'" +
            ", part='" + getPart() + "'" +
            ", pin='" + getPin() + "'" +
            ", rgb='" + isRgb() + "'" +
            ", main='" + isMain() + "'" +
            ", rgbRed='" + getRgbRed() + "'" +
            ", rgbBlue='" + getRgbBlue() + "'" +
            ", rgbGreen='" + getRgbGreen() + "'" +
            ", motor='" + isMotor() + "'" +
            ", pinmodeOut='" + isPinmodeOut() + "'" +
            ", pinmodeIn='" + isPinmodeIn() + "'" +
            ", servo='" + getServo() + "'" +
            ", active='" + isActive() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }

}
