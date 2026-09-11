package com.scheduler.app.backend.Firmware.Service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class FirmwareServiceTest {

    private final FirmwareService service = new FirmwareService(null);

    @Test
    void patchReleaseIsNewerThanCurrentLatest() {
        assertTrue(service.compareVersions("1.5.1", "1.5.0") > 0);
    }
}
