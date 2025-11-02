package com.xenosnowfox.forgedbythefox.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum ShipSize {
    PERSONAL("Personal"),
    FREIGHTER("Freighter"),
    CORVETTE("Corvette"),
    FRIGATE("Frigate"),
    DREADNOUGHT("Dreadnought");

    final String label;
}
