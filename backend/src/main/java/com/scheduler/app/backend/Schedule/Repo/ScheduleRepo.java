package com.scheduler.app.backend.Schedule.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.scheduler.app.backend.Schedule.Models.Schedule;

public interface ScheduleRepo extends JpaRepository<Schedule,Long>{

    // get startup and repeated schedule
    @Query(value="select id\r\n" + //
                "from schedule where schedule_device_id in (?1)\r\n" + //
                "and repeat_task=true and status=true\r\n" + //
                "or status=true and startup=true and schedule_device_id in (?1)",nativeQuery=true)
    List<Long> getActiveRoutineSchedules(String ids);

 
        
    @Query(value="select id,name,repeat_task,mode_random,startup,status,time,schedule_device_id,schedule_mode_id,schedule_route_id from schedule",nativeQuery=true)
    List<Schedule> getSchedule();
} 
