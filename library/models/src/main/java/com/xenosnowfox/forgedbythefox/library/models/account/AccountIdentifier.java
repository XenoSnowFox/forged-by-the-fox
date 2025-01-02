package com.xenosnowfox.forgedbythefox.library.models.account;

import java.util.UUID;
import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder")
public record AccountIdentifier(@NonNull String value) {

    public static AccountIdentifier fromUrn(final String withUrn) {
        final String[] parts = withUrn.split("ACCOUNT:", 2);
        if (parts.length != 2 || !parts[0].isBlank() || parts[1].isBlank()) {
            throw new IllegalArgumentException();
        }

        return new AccountIdentifier(parts[1].trim());
    }

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

    public String toUrn() {
        return "ACCOUNT:" + this.value;
    }

    public static AccountIdentifier random() {
        return new AccountIdentifier(UUID.randomUUID().toString());
    }
}
