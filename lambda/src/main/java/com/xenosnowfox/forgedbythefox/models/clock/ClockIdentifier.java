package com.xenosnowfox.forgedbythefox.models.clock;

import com.xenosnowfox.forgedbythefox.util.NanoId;
import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder")
public record ClockIdentifier(@NonNull String value) {

    public static ClockIdentifier fromUrn(final String withUrn) {
        final String[] parts = withUrn.split("CLOCK:", 2);
        if (parts.length != 2 || !parts[0].isBlank() || parts[1].isBlank()) {
            throw new IllegalArgumentException();
        }

        return new ClockIdentifier(parts[1].trim());
    }

    public ClockIdentifier {
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
        return "CLOCK:" + this.value;
    }

    public static ClockIdentifier random() {
        return new ClockIdentifier(NanoId.random());
    }
}
