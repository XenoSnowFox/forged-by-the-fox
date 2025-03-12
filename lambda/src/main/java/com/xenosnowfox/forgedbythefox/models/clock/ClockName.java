package com.xenosnowfox.forgedbythefox.models.clock;

public record ClockName(String value) {

    public static final int MAX_LENGTH = 50;

    public static final ClockName UNKNOWN = new ClockName("Unnamed Clock");

    public ClockName {
        value = value.trim();
        if (value.isBlank()) {
            throw new IllegalArgumentException("Clock Name cannot be blank or empty.");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Clock Name cannot be greater than %s characters in length.".formatted(MAX_LENGTH));
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
