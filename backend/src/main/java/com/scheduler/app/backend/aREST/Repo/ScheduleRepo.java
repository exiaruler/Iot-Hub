package com.scheduler.app.backend.aREST.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.scheduler.app.backend.aREST.Models.Schedule;

public interface ScheduleRepo extends JpaRepository<Schedule,Long>{

    @Query(value="SELECT id FROM Schedule where status=true and startup=true and deviceId in (?1)",nativeQuery=true)
    List<Long> getActiveStartupSchedules(String ids);

    @Query(value="SELECT id FROM Schedule where repeatTask=true and status=true and deviceId in (?1)",nativeQuery=true)
    List<Long> getActiveRoutineSchedules(String ids);
} 
