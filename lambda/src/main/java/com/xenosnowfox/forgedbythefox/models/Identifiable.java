package com.xenosnowfox.forgedbythefox.models;

public interface Identifiable<IDENTIFIER extends Identifer<?>> {
    IDENTIFIER identifier();
}
