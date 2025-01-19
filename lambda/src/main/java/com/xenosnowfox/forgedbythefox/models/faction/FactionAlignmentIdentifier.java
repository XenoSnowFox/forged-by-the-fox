package com.xenosnowfox.forgedbythefox.models.faction;

import java.util.UUID;
import lombok.NonNull;

public record FactionAlignmentIdentifier(@NonNull String value) {

    public static FactionAlignmentIdentifier fromUrn(final String withUrn) {
        final String[] parts = withUrn.split("FACTION-ALIGNMENT:", 2);
        if (parts.length != 2 || !parts[0].isBlank() || parts[1].isBlank()) {
            throw new IllegalArgumentException();
        }

        return new FactionAlignmentIdentifier(parts[1].trim());
    }

    public FactionAlignmentIdentifier {
        value = value.trim();
        if (value.isBlank()) {
            throw new IllegalArgumentException("Identifier value must not be blank or empty.");
        }
    }

    public static FactionAlignmentIdentifier random() {
        return new FactionAlignmentIdentifier(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String toUrn() {
        return "FACTION-ALIGNMENT:" + this.value;
    }
}
