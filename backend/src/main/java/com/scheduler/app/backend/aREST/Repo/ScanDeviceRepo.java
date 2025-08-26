package com.scheduler.app.backend.aREST.Repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.scheduler.app.backend.aREST.Models.ScanDevice;
public interface ScanDeviceRepo extends JpaRepository<ScanDevice, Long>{
    @Query(value="select Id from scheduler.scan_device where scan_version= ?1",nativeQuery=true)
    long getIdByCode(String code);


    
    
    
}