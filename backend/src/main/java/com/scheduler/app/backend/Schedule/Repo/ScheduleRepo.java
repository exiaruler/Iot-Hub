package com.scheduler.app.backend.Schedule.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.scheduler.app.backend.Schedule.Models.Schedule;

public interface ScheduleRepo extends JpaRepository<Schedule,Long>{

    // get startup and repeated schedule
    @Query(value="select id from schedule " +
                "where schedule_device_id in (:ids) " +
                "and status=true " +
                "and (repeat_task=true or startup=true)",nativeQuery=true)
    List<Long> getActiveRoutineSchedules(List<Long> ids);

 
        
    @Query(value="select id,name,repeat_task,mode_random,startup,status,time,schedule_device_id,schedule_mode_id,schedule_route_id from schedule",nativeQuery=true)
    List<Schedule> getSchedule();
} 
