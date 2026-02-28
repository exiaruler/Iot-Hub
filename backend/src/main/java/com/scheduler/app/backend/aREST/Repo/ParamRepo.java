package com.scheduler.app.backend.aREST.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.scheduler.app.backend.aREST.Models.Parameter;

public interface ParamRepo extends JpaRepository<Parameter,Long>{
    @Query(value="select value from Parameter where mode_id=:id order by parameterOrder",nativeQuery = true)
    String [] getAllParamValueArray(@Param("id")long id);
}
