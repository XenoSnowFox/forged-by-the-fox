package com.xenosnowfox.forgedbythefox.models.identity;

import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder")
public record IdentityIdentifier(@NonNull IdentityProvider provider, @NonNull String value) {
    public IdentityIdentifier {
        value = value.trim();
        if (value.isBlank()) {
            throw new IllegalArgumentException("Identifier value must not be blank or empty.");
        }
    }

    @Override
    public String toString() {
        return provider + ":" + value;
    }
}
