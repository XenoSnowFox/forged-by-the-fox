package com.xenosnowfox.forgedbythefox.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum Action {
    DOCTOR("Doctor", Attribute.INSIGHT),
    HACK("Hack", Attribute.INSIGHT),
    RIG("Rig", Attribute.INSIGHT),
    STUDY("Study", Attribute.INSIGHT),
    HELM("Helm", Attribute.PROWESS),
    SCRAMBLE("Scramble", Attribute.PROWESS),
    SCRAP("Scrap", Attribute.PROWESS),
    SKULK("Skulk", Attribute.PROWESS),
    ATTUNE("Attune", Attribute.RESOLVE),
    COMMAND("Command", Attribute.RESOLVE),
    CONSORT("Consort", Attribute.RESOLVE),
    SWAY("Sway", Attribute.RESOLVE);

    private final String label;

    private final Attribute attribute;
}
