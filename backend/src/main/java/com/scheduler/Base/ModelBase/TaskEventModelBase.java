package com.scheduler.Base.ModelBase;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;
import javax.persistence.PreUpdate;
@MappedSuperclass
public class TaskEventModelBase {
    @EmbeddedId
    private TaskEventId id;
     @Column
    private Instant createdDate=Instant.now();
    @Column
    private Instant updatedDate=Instant.now();

    public void initId(long boardId,long deviceId){
        this.setId(new TaskEventId(boardId, deviceId));
    }
     @PreUpdate
    protected void onUpdate() {
        this.setUpdatedDate(Instant.now());
    }

    public TaskEventId getId() {
        return this.id;
    }

    public void setId(TaskEventId id) {
        this.id = id;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getUpdatedDate() {
        return this.updatedDate;
    }

    public void setUpdatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
    }

}
