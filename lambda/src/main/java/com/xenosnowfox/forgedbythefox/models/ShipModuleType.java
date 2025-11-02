package com.xenosnowfox.forgedbythefox.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum ShipModuleType {
    AUXILIARY("Auxiliary"),
    HULL("Hull"),
    ENGINE("Engine"),
    COMMS("Comms"),
    WEAPON("Weapon");

    final String label;
}
