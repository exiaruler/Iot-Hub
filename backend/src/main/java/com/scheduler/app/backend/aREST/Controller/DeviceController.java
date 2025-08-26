package com.scheduler.app.backend.aREST.Controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scheduler.Base.ControllerBase;
import com.scheduler.app.backend.aREST.Models.Device;
import com.scheduler.app.backend.aREST.Service.DeviceService;
import org.springframework.web.bind.annotation.RequestParam;
@RestController
@RequestMapping(value = "/device")
public class DeviceController extends ControllerBase {
    @Autowired
    private DeviceService service;
     public DeviceController() {
        this.objectClass=this.pathBase+".aREST.Models.Device";
    }
    @PostMapping(value="/add-device/{id}",consumes = "application/json")
    public ResponseEntity<Device> addDevice(@PathVariable long id,@RequestBody Device payload){
        Device saveDevice=service.addDeviceSocket(payload,id);
        return ResponseEntity.ok(saveDevice);
    }
    
    @GetMapping(value="/get-devices")
    public List<Device> getAllDevices(@RequestParam(name="query", required = false)String query){
        if(query!=null){
            if(query=="device-routes"){
                return service.getAllDevicesWithRoutes();
            }
        }
        return service.getAllDevice();
    }
    
    @GetMapping(value="/get-device/{id}")
    public Device getDevice(@PathVariable long id){
        return service.getDevice(id);
    }
    @DeleteMapping(value="/delete-device/{id}")
    public ResponseEntity<String> deleteDevice(@PathVariable long id){
        String result=service.deleteDevice(id);
        return ResponseEntity.ok(result);
    }
    
}
