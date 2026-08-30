package com.scheduler.app.backend.Firmware.Controller;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scheduler.Base.Service.ControllerBaseService;
import com.scheduler.app.backend.Firmware.Model.Firmware;
import com.scheduler.app.backend.Firmware.Service.FirmwareService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping(value = "/firmware")
public class FirmwareController extends ControllerBaseService<Long,Firmware> {

    @Autowired
    private FirmwareService service;
    
    public FirmwareController() {
        this.objectClass=this.pathBase+".Firmware.Model.Firmware";
    }
    @GetMapping(value = "/get-records")
    public ResponseEntity<List<Firmware>> getRecords() {
        return ResponseEntity.ok(service.getFirmwares());
    }
    
    
}
