package com.scheduler.app.backend;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.scheduler.app.backend.Command.Service.CommandService;
import com.scheduler.app.backend.Hardware.Service.HardwareService;
// create inital data
@Component
public class StartupDataInitializer implements ApplicationRunner {
    private final CommandService commandService;
    private final HardwareService hardwareService;

    public StartupDataInitializer(CommandService commandService, HardwareService hardwareService) {
        this.commandService = commandService;
        this.hardwareService = hardwareService;
    }

    @Override
    public void run(ApplicationArguments args) {
        commandService.initData();
        hardwareService.initData();
    }
}
