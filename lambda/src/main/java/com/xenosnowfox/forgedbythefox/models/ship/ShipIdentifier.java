package com.xenosnowfox.forgedbythefox.models.ship;

import com.xenosnowfox.forgedbythefox.util.NanoId;
import lombok.NonNull;

public record ShipIdentifier(@NonNull String value) {
    private static final String URN_PREFIX = "SHIP";

    public static ShipIdentifier fromUrn(final String withUrn) {
        final String[] parts = withUrn.split(URN_PREFIX + ":", 2);
        if (parts.length != 2 || !parts[0].isBlank() || parts[1].isBlank()) {
            throw new IllegalArgumentException();
        }

        return new ShipIdentifier(parts[1].trim());
    }

    public ShipIdentifier {
        value = value.trim();
        if (value.isBlank()) {
            throw new IllegalArgumentException("Identifier value must not be blank or empty.");
        }
    }

    public static ShipIdentifier random() {
        return new ShipIdentifier(NanoId.random());
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String toUrn() {
        return URN_PREFIX + ":" + this.value;
    }
}
