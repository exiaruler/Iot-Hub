package com.scheduler.Base.ModelBase;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
// Base class for models 
@MappedSuperclass
public class ModelBase{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private Instant createdDate=Instant.now();
    @Column
    private Instant updatedDate=Instant.now();

    @PrePersist
    protected void onCreateSuper(){
        if(this.getId()>0){
            this.setId(0);
        }
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
    public ModelBase(){
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


    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

}