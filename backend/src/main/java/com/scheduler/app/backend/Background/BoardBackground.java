package com.scheduler.app.backend.Background;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.scheduler.app.backend.aREST.Models.Board;
import com.scheduler.app.backend.aREST.Service.BoardService;

@Component
public class BoardBackground {
    //private static final Map<String, Board> global = new ConcurrentHashMap<>();

    @Autowired
    BoardService boardService;
    //cron = "0 0/30 * * * ?"
    @Scheduled(cron = "0 0/30 * * * ?")
    private void offlineBoard(){
        System.out.println("Checking for offline boards...");
        boardService.offlineBoardRoutine();
    }
}
