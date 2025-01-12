package com.xenosnowfox.forgedbythefox.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
@RequiredArgsConstructor
public enum Load {
    LIGHT("Light", 3),
    NORMAL("Normal", 5),
    HEAVY("Heavy", 8);

    public final String label;

    public final int limit;
}
