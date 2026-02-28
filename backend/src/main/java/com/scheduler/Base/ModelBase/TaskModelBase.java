package com.scheduler.Base.ModelBase;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class TaskModelBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public long taskIdGenerate(long boardId){
        Instant utcdt=Instant.now();
        LocalDate date=LocalDate.now();
        LocalTime time=LocalTime.now();
        long genId=boardId+date.getDayOfMonth()+date.getMonthValue()+date.getYear()+time.getNano();
        return genId;
    }
}
