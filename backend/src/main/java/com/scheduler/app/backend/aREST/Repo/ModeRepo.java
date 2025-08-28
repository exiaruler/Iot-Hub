package com.scheduler.app.backend.aREST.Repo;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.scheduler.app.backend.aREST.Models.Mode;

public interface ModeRepo  extends JpaRepository<Mode, Long>{

    @Query(value="select * from scheduler.mode where mode= :mode and route_id= :id",nativeQuery = true)
    Mode findMode(@Param("mode")String mode,@Param("id")long id);

    @Query(value="select * from scheduler.mode where route_id=?1",nativeQuery = true)
    List<Mode> getModesByRouteId(long id);
}