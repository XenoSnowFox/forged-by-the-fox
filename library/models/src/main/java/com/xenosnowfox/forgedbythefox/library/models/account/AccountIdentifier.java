package com.xenosnowfox.forgedbythefox.library.models.account;

import java.util.UUID;
import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder")
public record AccountIdentifier(@NonNull String value) {
    public AccountIdentifier {
        value = value.trim();
        if (value.isBlank()) {
            throw new IllegalArgumentException("Identifier value must not be blank or empty.");
        }
    }

    @Override
    public String toString() {
        return value;
    }

    public static AccountIdentifier random() {
        return new AccountIdentifier(UUID.randomUUID().toString());
    }
}
