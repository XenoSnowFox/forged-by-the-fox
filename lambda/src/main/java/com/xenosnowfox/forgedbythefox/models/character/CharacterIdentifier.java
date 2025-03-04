package com.xenosnowfox.forgedbythefox.models.character;

import com.xenosnowfox.forgedbythefox.util.NanoId;
import lombok.NonNull;

public record CharacterIdentifier(@NonNull String value) {

    public static CharacterIdentifier fromUrn(final String withUrn) {
        final String[] parts = withUrn.split("CHARACTER:", 2);
        if (parts.length != 2 || !parts[0].isBlank() || parts[1].isBlank()) {
            throw new IllegalArgumentException();
        }

        return new CharacterIdentifier(parts[1].trim());
    }

    public CharacterIdentifier {
        value = value.trim();
        if (value.isBlank()) {
            throw new IllegalArgumentException("Identifier value must not be blank or empty.");
        }
    }

    public static CharacterIdentifier random() {
        return new CharacterIdentifier(NanoId.random());
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String toUrn() {
        return "CHARACTER:" + this.value;
    }
}
