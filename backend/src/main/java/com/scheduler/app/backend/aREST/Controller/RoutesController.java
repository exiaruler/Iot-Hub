package com.scheduler.app.backend.aREST.Controller;

import java.util.List;
import java.util.Map;

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
import com.scheduler.Base.MapCast.MapCast;
import com.scheduler.app.backend.aREST.Models.Mode;
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
        return service.getRouteById(id);
    }
    
    @PostMapping("/add-route")
    public Route postMethodName(@RequestBody Map<String, Object> payload) {
        int deviceId=(int) payload.get("deviceid");
        String route=(String) payload.get("route");
        boolean modes=(boolean)payload.get("modes");
        @SuppressWarnings("unchecked")
        List<Mode>list=(List<Mode>) payload.get("mode");
        
        return service.addRoute(deviceId,route,modes,list);
    }
    @PostMapping("/add-route-com/{id}")
    public Route addRouteCommand(@PathVariable long id,@RequestBody Map<String, Object> payload) {
        MapCast cast=mapCast.mapJson(payload); 
        return service.addRouteCommand(id,cast.getKeyString("route"),cast.getKeyInteger("command"),cast.getKeyArrayListString("pins"));
    }
    @PostMapping("/add-route-socket/{deviceId}/{commandId}")
    public ResponseEntity<Route> addRouteSocket(@PathVariable Long commandId,@PathVariable String deviceId,@RequestBody Route entity) {
        //TODO: process POST request
        Route add=service.addRouteandModes(entity, deviceId,commandId);
        return ResponseEntity.ok(add);
    }
    @PutMapping("/update-route-socket/{id}/{commandId}")
    public ResponseEntity<Route> updateRouteSocket(@PathVariable long id,@PathVariable Long commandId,@RequestBody Route entity) {
        Route update=service.updateRoute(entity, id, commandId);
        return ResponseEntity.ok(update);
    }
    @DeleteMapping("/delete-route/{id}")
    public void deleteRoute(@PathVariable long id){
        service.deleteRoute(id);
    }
    

    @PostMapping("/add-mode-com/{deviceId}/{routeId}")
    public Mode addModeCommand(@PathVariable long deviceId,@PathVariable long routeId,@RequestBody Map<String, Object> payload) {
        MapCast cast=mapCast.mapJson(payload); 
        return service.addModeCommand(deviceId,cast.getKeyString("name"),routeId,cast.getKeyArrayListString("params"));
    }


    
    
}
