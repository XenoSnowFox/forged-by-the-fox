package com.xenosnowfox.forgedbythefox.models.faction;

import java.util.UUID;
import lombok.NonNull;

public record FactionIdentifier(@NonNull String value) {

    public static FactionIdentifier fromUrn(final String withUrn) {
        final String[] parts = withUrn.split("FACTION:", 2);
        if (parts.length != 2 || !parts[0].isBlank() || parts[1].isBlank()) {
            throw new IllegalArgumentException();
        }

        return new FactionIdentifier(parts[1].trim());
    }

    public FactionIdentifier {
        value = value.trim();
        if (value.isBlank()) {
            throw new IllegalArgumentException("Identifier value must not be blank or empty.");
        }
    }

    public static FactionIdentifier random() {
        return new FactionIdentifier(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String toUrn() {
        return "FACTION:" + this.value;
    }
}
