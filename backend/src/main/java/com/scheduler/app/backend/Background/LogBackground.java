package com.scheduler.app.backend.Background;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.scheduler.app.backend.Log.Service.LogService;

@Component 
public class LogBackground {
    @Autowired
    private LogService logService;

    @Scheduled(cron = "0 * * * * ?")
    private void deleteExpiredLogs() {
        logService.deleteExpiredLogs();
    }
}
