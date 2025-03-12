package com.xenosnowfox.forgedbythefox.models.clock;

import java.time.Instant;
import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder")
public record Clock(
        @NonNull ClockIdentifier identifier,
        @NonNull ClockName name,
        @NonNull Instant timestampCreated,
        int markedSegments,
        int totalSegments) {

    public Clock {
        if (markedSegments < 0) {
            throw new IllegalArgumentException("Marked Segments cannot be less than zero.");
        }

        if (totalSegments < 1) {
            throw new IllegalArgumentException("Total Segments cannot be less than one.");
        }
    }
}
