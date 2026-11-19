package com.scheduler.app.backend.Log.Controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scheduler.Base.Service.ControllerBaseService;
import com.scheduler.app.backend.Log.Model.Log;
import com.scheduler.app.backend.Log.Service.LogService;

@RestController
@RequestMapping (value = "/log")
public class LogController extends ControllerBaseService<UUID,Log> {
    boolean disableAdd=true;
    boolean disableUpdate=true;
    boolean disableDelete=true;
    @Autowired
    private LogService logService;
    
    @GetMapping(value="/get-record-logs/{entity}/{entityId}")
    public ResponseEntity<List<Log>> getLogsByEntityAndEntityId(@PathVariable String entity, @PathVariable String entityId) {
        List<Log> logs = logService.getLogsByEntityAndEntityId(entity, entityId);
        return ResponseEntity.ok(logs);
    }
    


}
