package com.scheduler.app.backend.Messaging.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scheduler.app.backend.Messaging.Board.WebSocketHandlerRaw;



@RestController
@RequestMapping(value = "/websocket")
public class WebsocketController {
    @Autowired
    private WebSocketHandlerRaw websocketService;

    @GetMapping("/get-sessions")
    public List<String> getSessions() {
        return websocketService.getSessions();
    }
    
}
