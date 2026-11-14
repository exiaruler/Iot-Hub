package com.scheduler.app.backend.aREST.Models;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import com.scheduler.Base.ModelBase.ModelBase;
@Entity
public class Setting extends ModelBase  {
    @Column
    private boolean schedulerOn=false;
    @Column
    private boolean startOn=true;
    @Column
    private String ipAddress;

    public Setting(boolean schedulerOn, boolean startOn, String ipAddress) {
        this.schedulerOn = schedulerOn;
        this.startOn = startOn;
        this.ipAddress = ipAddress;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Setting() {
    }


    public boolean isSchedulerOn() {
        return this.schedulerOn;
    }

    public boolean getSchedulerOn() {
        return this.schedulerOn;
    }

    public void setSchedulerOn(boolean schedulerOn) {
        this.schedulerOn = schedulerOn;
    }

    public boolean isStartOn() {
        return this.startOn;
    }

    public boolean getStartOn() {
        return this.startOn;
    }

    public void setStartOn(boolean startOn) {
        this.startOn = startOn;
    }



    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Setting)) {
            return false;
        }
        Setting setting = (Setting) o;
        return schedulerOn == setting.schedulerOn && startOn == setting.startOn && Objects.equals(ipAddress, setting.ipAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schedulerOn, startOn, ipAddress);
    }


}
