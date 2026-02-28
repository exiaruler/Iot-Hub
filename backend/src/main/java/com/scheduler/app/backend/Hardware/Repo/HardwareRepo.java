package com.scheduler.app.backend.Hardware.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.scheduler.app.backend.Hardware.Models.Hardware;

public interface HardwareRepo extends JpaRepository<Hardware, Long> {

    @Query(value="Select * from Hardware where boardName=?1",nativeQuery = true)
    Hardware findHardwareByBoardName(String board);

}
