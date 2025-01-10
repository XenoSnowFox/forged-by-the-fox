package com.xenosnowfox.forgedbythefox.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum HarmLevel {
	LESSER(1, "Lesser", "Less Effect"),
	MODERATE(2, "Moderate", "-1D"),
	SEVERE(3, "Severe", "Need Help"),
	FATAL(4, "Fatal", "Retire")
	;

	final int level;

	final String label;

	final String effect;
}
