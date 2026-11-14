package com.scheduler.app.backend.aREST.Models;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.scheduler.app.backend.JacksonConfig;
import com.scheduler.app.backend.Schedule.Models.Schedule;
import com.scheduler.app.backend.Task.Model.Task;
import com.scheduler.app.backend.aREST.Function.Models.Mode;
import com.scheduler.app.backend.aREST.Function.Models.Route;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class BoardJacksonTest {

    private final ObjectMapper objectMapper = JacksonConfig.createObjectMapper();

    @Test
    void serializesBoardWithScheduledTaskWithoutRecursingThroughTransientMode() throws Exception {
        Board board = new Board();
        Device device = new Device();
        Route route = new Route();
        Schedule schedule = new Schedule();
        Task task = new Task();
        Mode mode = new Mode();

        board.setDevice(List.of(device));
        device.setBoard(board);
        device.setRoutes(List.of(route));
        route.setDevice(device);
        route.setMode(List.of(mode));
        mode.setRoute(route);
        mode.setScheduledModes(List.of(schedule));
        schedule.setDevice(device);
        schedule.setTask(task);
        schedule.setMode(mode);
        task.setSchedule(schedule);
        task.setModeTransient(mode);

        String json = objectMapper.writeValueAsString(board);

        assertTrue(json.contains("\"scheduledModes\""));
        assertFalse(json.contains("\"modeTransient\""));
    }
}
