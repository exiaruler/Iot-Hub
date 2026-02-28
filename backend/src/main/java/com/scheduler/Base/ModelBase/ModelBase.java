package com.scheduler.Base.ModelBase;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
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
    protected void onCreate(){
        if(this.getId()>0){
            this.setId(0);
        }
        this.setCreatedDate(Instant.now());
        this.setUpdatedDate(Instant.now());
    }
    @PreUpdate
    protected void onUpdate() {
        this.setUpdatedDate(Instant.now());
    }
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