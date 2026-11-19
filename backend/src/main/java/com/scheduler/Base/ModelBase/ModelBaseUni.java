package com.scheduler.Base.ModelBase;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@MappedSuperclass
public class ModelBaseUni {

    @Id
    private UUID id = UUID.randomUUID();
    @Column
    private Instant createdDate=Instant.now();
    @Column
    private Instant updatedDate=Instant.now();

    @PrePersist
    protected void onCreateSuper(){
        this.setCreatedDate(Instant.now());
        this.setUpdatedDate(Instant.now());
        this.prePersist();
    }
    protected void prePersist() {}
    @PreUpdate
    protected void onUpdateSuper() {
        this.setUpdatedDate(Instant.now());
        this.preUpdate();
    }
    protected void preUpdate() {}
    public ModelBaseUni(){
        this.setUpdatedDate(Instant.now());
    }    
    public Instant getUpdatedDate() {
        return this.updatedDate;
    }

    public void setUpdatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
    }


    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }
    
    public UUID getId() {
        return this.id;
    }
    public void setId(UUID id) {
        this.id = id;
    }



}
