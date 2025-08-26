package com.scheduler.app.backend.Messaging.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketSession;

import com.scheduler.app.backend.Messaging.Board.WebSocketHandlerRaw;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


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
