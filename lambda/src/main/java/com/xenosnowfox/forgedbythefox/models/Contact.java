package com.xenosnowfox.forgedbythefox.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum Contact {

    // Playbook: Mechanic
    SLICE("Slice", "a junkyard owner"),
    NISA("Nisa", "a previous employer"),
    STEV("Stev", "a gambler of ill repute"),
    LEN("Len", "a black market dealer"),
    KENN("Kenn", "a family member"),

    // Playbook: Muscle
    KRIEGER("Krieger", "a fine blaster pistol"),
    SHOD("Shod", "a weapons dealer"),
    CHON_ZEK("Chon-zek", "a bounty hunter"),
    YAZU("Yazu", "a crooked cop"),
    AYA("Aya", "an assassin"),

    // Playbook: Mystic
    HORUX("Horux", "a former teacher"),
    HICKS("Hicks", "a mystic goods supplier"),
    LAXX("Laxx", "a xeno"),
    RYE("Rye", "an unrequited love"),
    BLISH("Blish", "a fellow mystic"),

    // Playbook: Pilot
    YATTU("Yattu", "a gang boss"),
    TRIV("Triv", "a ship mechanic"),
    CHOSS("Choss", "a professional racer"),
    MERIS("Meris", "a scoundrel"),
    MAV("Mav", "a former mentor"),

    // Playbook: Scoundrel
    NYX("Myx", "a moneylender"),
    ORA("Ora", "an info broker"),
    JAL("Jal", "a ship mechanic"),
    RHIN("Rhin", "a smuggler"),
    BATTRO("Battro", "a bounty hunter"),

    // Playbook: Speaker
    ARRYN("Arryn", "a noble"),
    MANDA("Manda", "a guild member"),
    KERRY("Kerry", "a doctor"),
    JE_ZEE("Je-zee", "a diplomat"),

    // Playbook: Stitch
    JACKEV("Jackev", "a drug dealer"),
    ALBEN("Alben", "a former patient"),
    DITHA("Ditha", "a family member"),
    JUDA("Juda", "a doctor"),
    LYNIE("Lynie", "a hospital admin"),

    // Ship: Mastodon
    CONOR("Conor", "a union leader"),
    TKAL("T'kal", "a dock master"),
    MASON_RIDICK("Mason Ridick", "a lane-mapper"),
    WALTER_SINSTRO("Walter Sinstro", "a businessman"),
    STACY_WEATHERS("Stacy Weathers", "ace reporter")

    ;

    private final String name;
    private final String descriptor;
}
