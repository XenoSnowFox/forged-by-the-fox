package com.xenosnowfox.forgedbythefox.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Set;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum ShipSheet {

    MASTODON(
            "Mastodon",
            "M-36 Medium Construction Corvette",
            "Salvages Smugglers Thieves",
            ShipSize.CORVETTE,
            Set.of(Ability.LABOR_UNION,Ability.DEMOLITION,Ability.DUCTAPE_AND_CHEWING_GUM,Ability.JUNK_DOGS, Ability.DAY_JOB, Ability.TOOLS_OF_THE_TRADE, Ability.BUILDERS_AND_SHAPERS),
            Set.of(Contact.CONOR, Contact.MASON_RIDICK, Contact.TKAL, Contact.STACY_WEATHERS, Contact.WALTER_SINSTRO)
    );

    private final String label;
    private final String designation;
    private final String tagline;
    private final ShipSize size;
    private final Set<Ability> abilities;
    private final Set<Contact> contacts;
}
