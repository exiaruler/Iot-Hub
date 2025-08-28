package com.scheduler.Base.ModelBase;

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
        LocalDate date=LocalDate.now();
        LocalTime time=LocalTime.now();
        String strId=""+boardId+date.getDayOfMonth()+date.getMonthValue()+date.getYear()+time.getNano();
        long taskId=Long.parseLong(strId);
        return taskId;
    }
}
