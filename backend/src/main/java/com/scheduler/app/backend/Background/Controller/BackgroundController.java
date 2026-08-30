package com.scheduler.app.backend.Background.Controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scheduler.app.backend.Background.Background;

@RestController
@RequestMapping(value = "/background")
public class BackgroundController {

    @GetMapping(value = "/get-global/{key}")
    public ResponseEntity<Object> getGlobal(@PathVariable String key) {
        Object value = Background.getGlobal(key);
        if (value == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(value);
    }

    @GetMapping(value = "/get-globals")
    public ResponseEntity<Map<String, Object>> getGlobals() {
        return ResponseEntity.ok(Background.getGlobals());
    }

    @DeleteMapping(value = "/delete-global/{key}")
    public ResponseEntity<Void> deleteGlobal(@PathVariable String key) {
        if (!Background.globalExist(key)) {
            return ResponseEntity.notFound().build();
        }
        Background.removeGlobal(key);
        return ResponseEntity.noContent().build();
    }
}