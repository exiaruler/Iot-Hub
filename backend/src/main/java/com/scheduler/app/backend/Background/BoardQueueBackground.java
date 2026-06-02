package com.scheduler.app.backend.Background;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.scheduler.app.backend.aREST.Service.BoardQueueService;
// handle board queue operations
@Component
public class BoardQueueBackground {
    @Autowired
    BoardQueueService boardQueueService;

    @Scheduled(fixedRate = 60000)
    private void handleQueueExpiry(){
        boardQueueService.removeExpired();
    }
}
