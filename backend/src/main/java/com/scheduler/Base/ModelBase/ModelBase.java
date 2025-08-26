package com.scheduler.Base.ModelBase;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
// Base class for models 
@MappedSuperclass
public class ModelBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private LocalDateTime createdDate=LocalDateTime.now(ZoneId.of("Australia/Sydney"));
    @Column
    private LocalDateTime updatedDate=LocalDateTime.now(ZoneId.of("Australia/Sydney"));

    public ModelBase(){
        this.setUpdatedDate(LocalDateTime.now());
    }    
    public LocalDateTime getUpdatedDate() {
        return this.updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }


    public LocalDateTime getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }


    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

}