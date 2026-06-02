package com.scheduler.app.backend.Background;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.scheduler.app.backend.aREST.Service.BoardService;

@Component
public class BoardBackground {
    @Autowired
    BoardService boardService;
    //cron = "0 0/30 * * * ?"
    @Scheduled(cron = "0 0/30 * * * ?")
    private void offlineBoard(){
        System.out.println("Checking for offline boards...");
        boardService.offlineBoard();
    }
}
