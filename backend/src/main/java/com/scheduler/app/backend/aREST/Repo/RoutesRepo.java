package com.scheduler.app.backend.aREST.Repo;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.scheduler.app.backend.aREST.Models.Route;
public interface  RoutesRepo extends JpaRepository<Route, Long>{

    @Query(value="select * from route where route= :param and device_id= :id",nativeQuery = true)
    Route findExistingRouteByDevice(@Param("param")String param,@Param("id")long id);
    
    // update devices route to default if offline
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value="update route set selected_mode_id=default_mode_id where device_id in (:ids)",nativeQuery = true)
    void updateRoutesOffline(@Param("ids") List<Long> ids);
}
