package com.xenosnowfox.forgedbythefox.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum ShipModule {
    // Auxiliary Modules
    AI_MODULE(
            "AI Module",
            ShipModuleType.AUXILIARY,
            "Software connected to an Ur AI core, with fiber-optic filaments running throughout the ship. Can automate tasks or otherwise run the ship on behalf of the crew. Snarky personality module available for free."),
    ARMORY(
            "Armory",
            ShipModuleType.AUXILIARY,
            "A secure room holding the crew weapons and armor. All crew weapons and armor are considered to be Fine if not already so."),
    BRIG(
            "Brig",
            ShipModuleType.AUXILIARY,
            "Space jail. Not meant for long-term incarceration. Will prevent most attempts to escape."),
    GALLEY(
            "Galley",
            ShipModuleType.AUXILIARY,
            "A combination kitchen and serving area for meals. Greatly facilitates longer trips. Includes fresh food storage."),
    MEDICAL_BAY(
            "Medical Bay",
            ShipModuleType.AUXILIARY,
            "A clean room with medical equipment. Not a real hospital, but sufficient to patch most injuries. Storage for drugs and medical scanners. Add +1d to all crew recovery rolls."),
    SCIENCE_BAY(
            "Science Bay",
            ShipModuleType.AUXILIARY,
            "Laboratory that can be used to analyze anomalies and Precursor artifacts. Secure storage for things that may react oddly with the rest of the ship (or physics)."),
    SHIELDS(
            "Shields",
            ShipModuleType.AUXILIARY,
            "Particle sinks and EM deflectors. Can be overwhelmed with focused fire. Counts as armor against ship weapons and energy discharge. Completely absorbs blaster fire. Costs two upgrades instead of just one."),

    // Hull Modules
    CARGO_HOLD("Cargo Hold", ShipModuleType.HULL, ""),
    CREW_QUARTERS(
            "Crew Quarters",
            ShipModuleType.HULL,
            "You can sleep anywhere, but crew quarters are actually meant for it. Crew quarters afford privacy and comfort in a domain where such things are luxuries. Also you don't have to share, and you know the first mate snores."),
    LANDING_BAY(
            "Landing Bay",
            ShipModuleType.HULL,
            "Airlocks, bay doors, and takeoff ramps to accommodate shuttles and single-pilot small fighter craft for both land and space takeoff."),
    SMUGGLING_COMPARTMENTS("Smuggling Compartments", ShipModuleType.HULL, ""),

    // Engine Modules
    AFTERBURNERS("Afterburners", ShipModuleType.ENGINE, ""),
    CLOAKING_DEVICE("Cloaking Device", ShipModuleType.ENGINE, ""),
    GRAVITIC_FIELD_GENERATOR("Gravitic Field Generator", ShipModuleType.ENGINE, ""),
    JUMP_DRIVE(
            "Jump Drive",
            ShipModuleType.ENGINE,
            "A special engine that can activate the Ur gates that connect systems and translate ships into hyperspace lanes."),

    // Comms Modules
    FAKE_TRANSPONDER("Fake Transponder", ShipModuleType.COMMS, ""),
    LONG_RANGE_SCANNER("Long Range Scanner", ShipModuleType.COMMS, ""),
    NEXUS_LINK("Nexus Link", ShipModuleType.COMMS, ""),
    QUANTUM_ENCRYPTOR("Quantum Encryptor", ShipModuleType.COMMS, ""),
    TARGETING_COMPUTER("Targeting Computer", ShipModuleType.COMMS, ""),

    // Weapon Modules
    COHERENCE_CANNON(
            "Coherence Cannon",
            ShipModuleType.WEAPON,
            "A capital weapon. One shot only until repaired or recharged on ships smaller than dreadnoughts. May fry systems. Incredibly deadly. Suer not legal."),
    GRAPPLING_HOOKS("Grappling Hooks", ShipModuleType.WEAPON, ""),
    MINING_DRILL("Mining Drill", ShipModuleType.WEAPON, ""),
    MISSILES("Missiles", ShipModuleType.WEAPON, ""),
    PARTICLE_CANNONS(
            "Particle Cannons",
            ShipModuleType.WEAPON,
            "Pew! Pew! Usually fixed in one direction on personal vessels. Often cross linked. Not legal without a license.");

    final String label;
    private final ShipModuleType type;
    private final String markdown;
}
