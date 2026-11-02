package com.scheduler.Base;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class BackgroundTest {

    private final String key = "background-test-" + UUID.randomUUID();

    @AfterEach
    void cleanUp() {
        Background.removeGlobal(key);
    }

    @Test
    void storesAndReturnsGlobalValue() {
        Object value = new Object();

        Background.putGlobal(key, value);

        assertSame(value, Background.getGlobal(key));
        assertTrue(Background.globalExist(key));
    }

    @Test
    void ignoresNullValues() {
        Background.putGlobal(key, "existing");
        Background.putGlobal(key, null);

        assertEquals("existing", Background.getGlobal(key));
    }

    @Test
    void reportsMissingKeyAsAbsent() {
        assertNull(Background.getGlobal(key));
        assertFalse(Background.globalExist(key));
    }

    @Test
    void considersFalseValuePresent() {
        Background.putGlobal(key, false);

        assertFalse((Boolean) Background.getGlobal(key));
        assertTrue(Background.globalExist(key));
    }

    @Test
    void returnsSnapshotOfGlobals() {
        Background.putGlobal(key, "value");

        Map<String, Object> snapshot = Background.getGlobals();
        snapshot.remove(key);

        assertEquals("value", Background.getGlobal(key));
    }

    @Test
    void removesGlobalValue() {
        Background.putGlobal(key, "value");

        Background.removeGlobal(key);

        assertNull(Background.getGlobal(key));
        assertFalse(Background.globalExist(key));
    }
}