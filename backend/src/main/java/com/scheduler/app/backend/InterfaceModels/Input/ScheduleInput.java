package com.scheduler.app.backend.InterfaceModels.Input;

import java.util.Objects;

import com.scheduler.app.backend.Schedule.Models.Schedule;

public class ScheduleInput extends Schedule {
    private long deviceId;
    private long routeId;
    private long modeId;

    public ScheduleInput() {
    }

    public ScheduleInput(long deviceId, long routeId, long modeId) {
        this.deviceId = deviceId;
        this.routeId = routeId;
        this.modeId = modeId;
    }

    public long getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public long getRouteId() {
        return this.routeId;
    }

    public void setRouteId(long routeId) {
        this.routeId = routeId;
    }

    public long getModeId() {
        return this.modeId;
    }

    public void setModeId(long modeId) {
        this.modeId = modeId;
    }

    public ScheduleInput deviceId(long deviceId) {
        setDeviceId(deviceId);
        return this;
    }

    public ScheduleInput routeId(long routeId) {
        setRouteId(routeId);
        return this;
    }

    public ScheduleInput modeId(long modeId) {
        setModeId(modeId);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ScheduleInput)) {
            return false;
        }
        ScheduleInput scheduleInput = (ScheduleInput) o;
        return deviceId == scheduleInput.deviceId && routeId == scheduleInput.routeId && modeId == scheduleInput.modeId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceId, routeId, modeId);
    }

    @Override
    public String toString() {
        return "{" +
            " deviceId='" + getDeviceId() + "'" +
            ", routeId='" + getRouteId() + "'" +
            ", modeId='" + getModeId() + "'" +
            "}";
    }
    
}
