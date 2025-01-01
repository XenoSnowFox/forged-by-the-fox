package com.xenosnowfox.forgedbythefox.library.models.session;

import java.util.UUID;
import lombok.NonNull;

public record SessionIdentifier(@NonNull String value) {

    public SessionIdentifier {
        value = value.trim();
        if (value.isBlank()) {
            throw new IllegalArgumentException("Identifier value must not be blank or empty.");
        }
    }

    public static SessionIdentifier random() {
        return new SessionIdentifier(UUID.randomUUID().toString());
    }
}
