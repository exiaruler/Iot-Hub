package com.scheduler.app.backend.aREST.Models;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jackson.autoconfigure.JsonFactoryBuilderCustomizer;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.scheduler.app.backend.JacksonConfig;
import com.scheduler.app.backend.aREST.Function.Models.Route;

import tools.jackson.core.json.JsonFactory;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

class RouteJacksonTest {

    private final ObjectMapper objectMapper = JacksonConfig.createObjectMapper();

    @Test
    void deserializesModesThroughManagedReference() throws Exception {
        Route route = objectMapper.readValue(
                "{\"mode\":[{\"mode\":\"Quiet\"}]}",
                Route.class);

        assertEquals(1, route.getMode().size());
        assertEquals("Quiet", route.getMode().get(0).getMode());
    }

    @Test
    void springCustomizedAndStandaloneMappersSerializeBeyondDefaultNestingLimit() throws Exception {
        List<Object> nested = List.of("value");
        for (int i = 0; i < 500; i++) {
            nested = List.of(nested);
        }

        assertNestedJsonSerializes(objectMapper, nested);

        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(JacksonConfig.class)) {
            var factoryBuilder = JsonFactory.builder();
            context.getBean(JsonFactoryBuilderCustomizer.class).customize(factoryBuilder);

            var mapperBuilder = JsonMapper.builder(factoryBuilder.build());
            context.getBean(JsonMapperBuilderCustomizer.class).customize(mapperBuilder);
            assertNestedJsonSerializes(mapperBuilder.build(), nested);
        }
    }

    private void assertNestedJsonSerializes(ObjectMapper mapper, Object value) throws Exception {
        String json = mapper.writeValueAsString(value);

        assertEquals(501L, json.chars().filter(character -> character == '[').count());
    }
}
