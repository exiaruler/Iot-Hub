package com.scheduler.app.backend.aREST.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scheduler.Base.ControllerBase;
import com.scheduler.app.backend.aREST.Models.Route;
import com.scheduler.app.backend.aREST.Service.RoutesService;

@RestController
@RequestMapping(value = "/route")
public class RoutesController extends ControllerBase {
    @Autowired
    private RoutesService service;

    public RoutesController() {
        objectClass="com.scheduler.app.backend.aREST.Models.Route";
    }

    @GetMapping(value="/get-all-routes")
    public List<Route> getAllRoutes(){
        return service.getAllRoutes();
    }
    @GetMapping("/get-route/{id}")
    public Route getRoute(@PathVariable long id) {
        return service.getRouteByIdDisplay(id);
    }
    @PostMapping("/add-route-socket/{deviceId}")
    public ResponseEntity<Route> addRouteSocket(@PathVariable String deviceId,@RequestBody Route entity) {
        Route add=service.addRouteandModes(entity, deviceId);
        return ResponseEntity.ok(add);
    }
    @PutMapping("/update-route-socket/{id}")
    public ResponseEntity<Route> updateRouteSocket(@PathVariable long id,@RequestBody Route entity) {
        Route update=service.updateRoute(entity, id);
        return ResponseEntity.ok(update);
    }
    @DeleteMapping("/delete-route/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable long id){
        service.deleteRoute(id);
        return ResponseEntity.ok().build();
    }
        
    
}
