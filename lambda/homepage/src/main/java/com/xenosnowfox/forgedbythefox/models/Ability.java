package com.xenosnowfox.forgedbythefox.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Ability {
    ACE_PILOT(
            "Ace Pilot",
            Playbook.PILOT,
            "You have **potency** on all speed-related rolls. When you roll to **resist** the consequences of piloting, gain **+1d**.",
            true),
    IM_A_DOCTOR_NOT_A(
            "I'm a Doctor, Not a ...",
            Playbook.STITCH,
            "You can **push yourself** to roll your **doctor** rating while performing a different action. Say which patient, research, or posting taught you this trick.",
            true),
    THE_WAY("The Way", Playbook.MYSTIC, "You can spend a **gambit** instead of paying any **stress** cost.", true),
    TINKER(
            "Tinker",
            Playbook.MECHANIC,
            "When you work on a clock with **rig** or **hack**, or when you **study** a schematic, fill **+1 segment**.",
            true);

    private final String title;
    private final Playbook playbook;
    private final String markdown;
    private final boolean isStartingAbility;
}
