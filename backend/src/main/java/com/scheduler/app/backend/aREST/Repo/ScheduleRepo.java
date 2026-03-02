package com.scheduler.app.backend.aREST.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.scheduler.app.backend.aREST.Models.Schedule;

public interface ScheduleRepo extends JpaRepository<Schedule,Long>{

    @Query(value="select id from schedule where status=true and startup=true and device_id in (?1)",nativeQuery=true)
    List<Long> getActiveStartupSchedules(String ids);

    @Query(value="select id from schedule where repeat_task=true and status=true and device_id in (?1)",nativeQuery=true)
    List<Long> getActiveRoutineSchedules(String ids);
} 
