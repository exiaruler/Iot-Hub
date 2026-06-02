package com.scheduler.Base.ModelBase;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;
import javax.persistence.PreUpdate;

import com.scheduler.app.backend.aREST.Models.Board;
import com.scheduler.app.backend.aREST.Models.Device;

@MappedSuperclass
public class QueueModelBase {

    @EmbeddedId
    private QueueEventId id;
    @Column
    private Instant createdDate=Instant.now();
    @Column
    private Instant updatedDate=Instant.now();

    public void initId(Board board,Device device){
        this.setId(new QueueEventId(board, device));
    }
    @PreUpdate
    protected void onUpdate() {
        this.setUpdatedDate(Instant.now());
    }
    public QueueEventId getId() {
        return this.id;
    }

    public void setId(QueueEventId id) {
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
