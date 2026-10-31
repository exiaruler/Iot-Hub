package com.scheduler.app.backend.InterfaceModels.Reference;

import java.time.Instant;

public interface TaskEventIdView {
    long getBoardId();
    long getDeviceId();
    Instant getEventTime();
}
