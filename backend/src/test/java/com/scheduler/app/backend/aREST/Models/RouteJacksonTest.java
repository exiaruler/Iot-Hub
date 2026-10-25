package com.scheduler.app.backend.aREST.Models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class RouteJacksonTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void deserializesModesThroughManagedReference() throws Exception {
        Route route = objectMapper.readValue(
                "{\"mode\":[{\"mode\":\"Quiet\"}]}",
                Route.class);

        assertEquals(1, route.getMode().size());
        assertEquals("Quiet", route.getMode().get(0).getMode());
    }
}
