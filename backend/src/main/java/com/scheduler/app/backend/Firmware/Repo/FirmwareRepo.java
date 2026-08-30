package com.scheduler.app.backend.Firmware.Repo;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import com.scheduler.app.backend.Firmware.Model.Firmware;

public interface FirmwareRepo extends JpaRepository<Firmware,Long> {

}
