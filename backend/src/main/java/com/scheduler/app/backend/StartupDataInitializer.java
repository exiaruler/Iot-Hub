package com.scheduler.app.backend;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.scheduler.app.backend.Command.Service.CommandService;
import com.scheduler.app.backend.Hardware.Service.HardwareService;
import com.scheduler.app.backend.aREST.Service.BoardService;
// create inital data
@Component
public class StartupDataInitializer implements ApplicationRunner {
    private final CommandService commandService;
    private final HardwareService hardwareService;
    private final BoardService boardService;


    public StartupDataInitializer(CommandService commandService, HardwareService hardwareService, BoardService boardService) {
        this.commandService = commandService;
        this.hardwareService = hardwareService;
        this.boardService = boardService;
    }

    @Override
    public void run(ApplicationArguments args) {
        commandService.initData();
        hardwareService.initData();
        boardService.offlineBoardRoutine();

    }
}
