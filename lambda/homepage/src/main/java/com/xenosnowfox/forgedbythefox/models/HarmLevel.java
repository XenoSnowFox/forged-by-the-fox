package com.xenosnowfox.forgedbythefox.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum HarmLevel {
    LESSER(1, "Lesser", "Less Effect", 2),
    MODERATE(2, "Moderate", "-1 Dice", 2),
    SEVERE(3, "Severe", "Need Help", 1),
    FATAL(4, "Fatal", "Death/ Retire", 1);

    final int level;

    final String label;

    final String effect;

    final int maximum;
}
