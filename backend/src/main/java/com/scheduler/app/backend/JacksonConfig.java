package com.scheduler.app.backend;

import tools.jackson.core.StreamWriteConstraints;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import org.springframework.boot.jackson.autoconfigure.JsonFactoryBuilderCustomizer;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    private static final int MAX_JSON_NESTING_DEPTH = 1_000;

    @Bean
    JsonFactoryBuilderCustomizer jacksonFactoryBuilderCustomizer() {
        return builder -> builder.streamWriteConstraints(createStreamWriteConstraints());
    }

    @Bean
    JsonMapperBuilderCustomizer jacksonDeserializationCompatibilityCustomizer() {
        return builder -> builder.disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
    }

    public static ObjectMapper createObjectMapper() {
        return createJsonMapper();
    }

    private static JsonMapper createJsonMapper() {
        return JsonMapper.builder(createJsonFactory())
                .disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
                .build();
    }

    private static JsonFactory createJsonFactory() {
        return JsonFactory.builder()
                .streamWriteConstraints(createStreamWriteConstraints())
                .build();
    }

    private static StreamWriteConstraints createStreamWriteConstraints() {
        return StreamWriteConstraints.builder()
                .maxNestingDepth(MAX_JSON_NESTING_DEPTH)
                .build();
    }
}
