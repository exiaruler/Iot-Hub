package com.scheduler.app.backend.aREST.Models;

import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalTime;

// log commands sent and received
public class BoardLog {
    // task id
    private long taskId;
    // message
    private String message;
    // board Id long id
    private long boardIdNum;
    // board Id
    private String boardId;
    // command sent
    private Instant dateTimeSentUtc;
    private DateFormat dateSent;
    private LocalTime timeSent;
    // commmand received
    private Instant dateTimeReceivedUtc;
    private DateFormat dateReceived;
    private LocalTime timeReceived;
    // ip address
    private String ip;
    

}
