package com.xenosnowfox.forgedbythefox.models;

import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum ShipSheet {
    FIREDRAKE(
            "Firedrake",
            "Converted Khanjigar-class Corvette",
            "Rebels and Hegemonic Criminals",
            ShipSize.CORVETTE,
            Set.of(
                    Ability.OLD_HANDS,
                    Ability.FORGED_IN_FIRE,
                    Ability.SYMPATHIZERS,
                    Ability.NATURAL_ENEMIES,
                    Ability.SPARK_OF_REBELLION,
                    Ability.JUST_CAUSE,
                    Ability.HEARTS_AND_MINDS),
            Set.of(Contact.GARIN, Contact.TYURA, Contact.ADA_BLACK, Contact.TIKO_LUX, Contact.IBO_ONE),
            Map.of(
                    ShipModuleType.COMMS,
                    4,
                    ShipModuleType.WEAPON,
                    3,
                    ShipModuleType.ENGINE,
                    3,
                    ShipModuleType.HULL,
                    2)),
    MASTODON(
            "Mastodon",
            "M-36 Medium Construction Corvette",
            "Salvages Smugglers Thieves",
            ShipSize.CORVETTE,
            Set.of(
                    Ability.LABOR_UNION,
                    Ability.DEMOLITION,
                    Ability.DUCTAPE_AND_CHEWING_GUM,
                    Ability.JUNK_DOGS,
                    Ability.DAY_JOB,
                    Ability.TOOLS_OF_THE_TRADE,
                    Ability.BUILDERS_AND_SHAPERS),
            Set.of(Contact.CONOR, Contact.MASON_RIDICK, Contact.TKAL, Contact.STACY_WEATHERS, Contact.WALTER_SINSTRO),
            Map.of(
                    ShipModuleType.COMMS,
                    4,
                    ShipModuleType.WEAPON,
                    3,
                    ShipModuleType.ENGINE,
                    3,
                    ShipModuleType.HULL,
                    2));

    private final String label;
    private final String designation;
    private final String tagline;
    private final ShipSize size;
    private final Set<Ability> abilities;
    private final Set<Contact> contacts;
    private final Map<ShipModuleType, Integer> moduleLimits;

    public Integer maxCommsModules() {
        return this.moduleLimits.get(ShipModuleType.COMMS);
    }

    public Integer maxWeaponModules() {
        return this.moduleLimits.get(ShipModuleType.WEAPON);
    }

    public Integer maxHullModules() {
        return this.moduleLimits.get(ShipModuleType.HULL);
    }

    public Integer maxEngineModules() {
        return this.moduleLimits.get(ShipModuleType.ENGINE);
    }
}
