package com.scheduler.app.backend.Schedule.Models;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.scheduler.Base.ModelBase.ModelBase;

@Entity 
public class NextSchedule extends ModelBase{

    // schedule order
    @Column
    private int scheduleOrder;
    // schedule
    @JsonBackReference("route-next")
    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;
    // selected schedule to run next
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "next_schedule_id")
    private Schedule targetSchedule;
    // schedule Id
    @Transient 
    private long scheduleId;
    // schedule name
    @Transient 
    private String name="";

    @PostLoad 
    private void postLoad(){
        if(targetSchedule!=null){
            scheduleId=targetSchedule.getId();
            name=targetSchedule.getName();
        }
    }

    public NextSchedule() {
    }

    public NextSchedule(int scheduleOrder, Schedule schedule) {
        this.scheduleOrder = scheduleOrder;
        this.schedule = schedule;
    }

    public NextSchedule(int scheduleOrder, Schedule schedule, Schedule targetSchedule) {
        this.scheduleOrder = scheduleOrder;
        this.schedule = schedule;
        setTargetSchedule(targetSchedule);
    }

    public int getScheduleOrder() {
        return this.scheduleOrder;
    }

    public void setScheduleOrder(int scheduleOrder) {
        this.scheduleOrder = scheduleOrder;
    }

    public Schedule getSchedule() {
        return this.schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Schedule getTargetSchedule() {
        return this.targetSchedule;
    }

    public void setTargetSchedule(Schedule targetSchedule) {
        this.targetSchedule = targetSchedule;
        if (targetSchedule != null) {
            this.scheduleId = targetSchedule.getId();
            this.name = targetSchedule.getName();
        }
    }

    public NextSchedule scheduleOrder(int scheduleOrder) {
        setScheduleOrder(scheduleOrder);
        return this;
    }

    public NextSchedule schedule(Schedule schedule) {
        setSchedule(schedule);
        return this;
    }

    public NextSchedule targetSchedule(Schedule targetSchedule) {
        setTargetSchedule(targetSchedule);
        return this;
    }

    public long getScheduleId() {
        return this.scheduleId;
    }

    public void setScheduleId(long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    private Long getScheduleIdValue() {
        return this.schedule == null ? null : this.schedule.getId();
    }

    private Long getTargetScheduleIdValue() {
        return this.targetSchedule == null ? null : this.targetSchedule.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof NextSchedule)) {
            return false;
        }
        NextSchedule nextSchedule = (NextSchedule) o;
        return getId() == nextSchedule.getId()
            && Objects.equals(getCreatedDate(), nextSchedule.getCreatedDate())
            && Objects.equals(getUpdatedDate(), nextSchedule.getUpdatedDate())
            && scheduleOrder == nextSchedule.scheduleOrder
            && Objects.equals(getScheduleIdValue(), nextSchedule.getScheduleIdValue())
            && Objects.equals(getTargetScheduleIdValue(), nextSchedule.getTargetScheduleIdValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCreatedDate(), getUpdatedDate(), scheduleOrder, getScheduleIdValue(),
            getTargetScheduleIdValue());
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            ", scheduleOrder='" + getScheduleOrder() + "'" +
            ", scheduleId='" + getScheduleIdValue() + "'" +
            ", targetScheduleId='" + getTargetScheduleIdValue() + "'" +
            ", scheduleName='" + getName() + "'" +
            "}";
    }
    
}
