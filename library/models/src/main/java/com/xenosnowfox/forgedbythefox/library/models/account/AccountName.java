package com.xenosnowfox.forgedbythefox.library.models.account;

public record AccountName(String value) {

    public static final int MAX_LENGTH = 50;

    public static final AccountName UNKNOWN = new AccountName("Unknown User");

    public AccountName {
        value = value.trim();
        if (value.isBlank()) {
            throw new IllegalArgumentException("Display Name cannot be blank or empty.");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Display Name cannot be greater than %s characters in length.".formatted(MAX_LENGTH));
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
