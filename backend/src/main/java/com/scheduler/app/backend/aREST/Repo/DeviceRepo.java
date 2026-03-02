package com.scheduler.app.backend.aREST.Repo;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import com.scheduler.app.backend.aREST.Models.Device;
public interface DeviceRepo  extends JpaRepository<Device, Long> {
    @Modifying
    @Transactional
    @Query(value = "update device set state= :state,warning= :warning where id= :id",nativeQuery = true)
    int updateStateAndWarning(@Param("id")long id,@Param("state")String state,@Param("warning")String warning);
    
    @Query(value = "select * from device where id= :id and name= :name",nativeQuery =true)
    Device findExistingDevice(@Param("id")long id,@Param("name")String name);

    @Query(value = "select * from device where device_id=:id",nativeQuery =true)
    Device findDeviceByDeviceId(@Param("id")String id);

    @Query(value = "select id from device where board_id= :id",nativeQuery =true)
    long [] findDevicesByBoard(@Param("id") long id);

    @Query(value = "select distinct dev.* from device dev, route rou, board bo where dev.board_id=bo.id and bo.activated=true and rou.device_id=dev.id",nativeQuery =true)
    List<Device> getDevicesByRoutes();
}
