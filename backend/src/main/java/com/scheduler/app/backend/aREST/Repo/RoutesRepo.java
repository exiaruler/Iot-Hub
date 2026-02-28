package com.scheduler.app.backend.aREST.Repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.scheduler.app.backend.aREST.Models.Route;
public interface  RoutesRepo extends JpaRepository<Route, Long>{

    @Query(value="Select * from Route where route= :param and device_id= :id",nativeQuery = true)
    Route findExistingRouteByDevice(@Param("param")String param,@Param("id")long id);
}
