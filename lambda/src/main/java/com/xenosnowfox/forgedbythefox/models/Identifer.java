package com.xenosnowfox.forgedbythefox.models;

public interface Identifer<T> {
    String toUrn();

    T value();
}
