package com.scheduler.app.backend.aREST.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scheduler.Base.ControllerBase;
import com.scheduler.app.backend.aREST.Models.BoardQueue;
import com.scheduler.app.backend.aREST.Service.BoardQueueService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping(value = "/board/queue")
public class BoardQueueController extends ControllerBase{
    @Autowired
    private BoardQueueService service;

    @GetMapping("/get-queues/{board}")
    public ResponseEntity<List<BoardQueue>> getQueues(@PathVariable String board,@RequestParam(defaultValue = "") String device) {
        List<BoardQueue> que=service.getQueues(board, device);
        return ResponseEntity.ok(que);
    }
    
}
