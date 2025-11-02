package com.xenosnowfox.forgedbythefox.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Ability {

    // Mechanic Abilities
    TINKER(
            "Tinker",
            "When you work on a clock with **rig** or **hack**, or when you **study** a schematic, fill **+1 segment**.",
            true),
    BAILING_WIRE_AND_MECHTAPE(
            "Bailing, Wire and Mech-Tape",
            "You get an extra **downtime activity** to **repair**, and the repair activity costs you **0 cred**.",
            false),
    CONSTRUCT_WHISPERER(
            "Construct Whisperer",
            "Machines speak to you when you **study** them. The first time you roll a **critical** while fixing or building a particular machine, you may add a **simple modification** to it.",
            false),
    JUNKYARD_HUNTER(
            "Junkyard Hunter",
            "When you **acquire** parts or equipment during **downtime**, you may either gain **two assets**, or one asset at **+1 quality**.",
            false),
    HACKER(
            "Hacker",
            "You may expend your **special armor** to resist the consequences of **hacking**, or to **push yourself** when **hacking** or **gathering info** electronically.",
            false),
    FIXED(
            "Fixed",
            "You may expend your **special armor** to resist a consequence from machines breaking or being damaged, or to **push yourself** when repairing or building a machine.",
            false),
    MECHANICS_HEART(
            "Mechanic's Heart",
            "When you speak from your heart, your words can reach even the most hardened criminal, and you gain **potency**.",
            true),
    OVERCLOCK(
            "Overclock",
            "When you spend a **gambit** on a **rig** roll to repair or upgrade, treat the system you worked on as **1 quality** higher for the remainder of the job.",
            false),
    ANALYST(
            "Analyst",
            "You can push yourself to do one of the following: perform a feat of physical force that verges on the superhuman - engage a small gang on equal footing in close combat.",
            false),

    // Muscle Abilities
    UNSTOPPABLE(
            "Unstoppable",
            "When you **hack** a system, you may also ask a question about the owner or location of the system as though you had rolled a **6** on **gather info**. When you **resist** the consequences of **hacking**, roll *+1d**.",
            true),
    WRECKING_CREW(
            "Wrecking Crew",
            "Your strength and ferocity are infamous. When striking in melee, you gain +1d. Whenever you spend a gambit in combat, you also gain +1 effect on that action.",
            false),
    BACKUP("Backup", "An ally's push costs 1 stress on any action you set up or assist.", false),
    BATTLEBORN(
            "Battleborn",
            "You may expend your special armor to reduce harm from an attack in combat, or to push yourself during a fight.",
            false),
    BODYGUARD("Bodyguard", "When you protect a crewmate, resist with +1d. When you take harm, clear 1 stress.", false),
    FLESH_WOUND(
            "Flesh Wound",
            "If you're wounded at the beginning of downtime, mark +3 segments on your healing clock. When you push yourself to ignore wound penalties, you take only 1 stress (not 2).",
            false),
    PREDATOR(
            "Predator",
            "Take +1d to rolls against weakened or vulnerable targets. Whenever you gather information on a weakness or vulnerability, the worst you can get is a 4/5 result.",
            false),
    READY_FOR_ANYTHING(
            "Ready for Anything",
            "When being ambushed, you gain potency to all actions during a flashback, and your first flashback costs 0 stress.",
            false),
    SCARY(
            "Scary",
            "You have an air of menace and danger obvious to even the most unobservant. You gain potency when trying to intimidate someone. If done immediately after a show of force, also take +1d.",
            false),

    // Mystic Abilities
    THE_WAY("The Way", "You can spend a **gambit** instead of paying any **stress** cost.", true),
    KINETICS(
            "Kinetics",
            "You can push yourself to do one of the following: use the Way to throw a table-sized object with dangerous force - propel yourself briefly with superhuman speed.",
            false),
    PSYBLADE(
            "Psy-Blade",
            "You can focus Way energy into your melee weapon. While charged, the weapon can cut through non-shielded materials with ease, and you gain potency on your melee attacks.",
            false),
    CENTER(
            "Center",
            "You gain Meditation as a vice. When you indulge this vice, clear +1 stress and add Dark Visions as a possible overindulgence.",
            false),
    WAY_SHIELD(
            "Way Shield",
            "You can block blaster bolts with the Way (resist with resolve). If you resist a blaster attack, you may spend 1 stress to redirect fire and make an attack of your own with it.",
            false),
    WARDED(
            "Warded",
            "You may expend your special armor to resist the consequences of a Way attack or artifact use, or push yourself when using mystic powers.",
            false),
    PSYDANCING(
            "Psy-Dancing",
            "You may push yourself to cloud a target's mind and sway them in the face of contradictory evidence. Spend 1 stress for each additional feature: they have only vague memories of the event - it works on a small group.",
            false),
    VISIONS(
            "Visions",
            "Spend 1 stress to remotely view a distant place or person tied to you in some intimate way. Spend 1 stress for each extra feature: It lasts for a minute rather than a moment—your target can also see and hear you—you may see something only familiar to you, not intimate.",
            false),
    SUNDERING(
            "Sundering",
            "You may push yourself to attune to the Way and twist it, causing psychic harm to anyone in the area vulnerable to your assault. You may spend 1 stress for each additional feature: it damages instead of stuns—you and anyone you choose get +2d to resist the effects.",
            false),

    // Pilot Abilities
    ACE_PILOT(
            "Ace Pilot",
            "You have **potency** on all speed-related rolls. When you roll to **resist** the consequences of piloting, gain **+1d**.",
            true),
    KEEN_EYE(
            "Keen Eye",
            "You have sharp eyes and notice small details many might overlook. Gain +1d when firing ship guns or making trick shots.",
            false),
    SIDE_JOB(
            "Side Job",
            "You may spend a downtime activity in port doing odd jobs. Gain 1 cred. If there are rumors floating about, the GM will tell you of them.",
            false),
    EXCEED_SPECS(
            "Exceed Specs",
            "While onboard a ship you may damage a ship system you have access to in order to gain +1d or +1 effect to a roll.",
            false),
    LEAF_ON_THE_WIND(
            "Leaf on the Wind",
            "When you push yourself, you may spend +1 stress (3 stress total) to gain both +1 effect and +1d instead of one or the other.",
            false),
    HEDONIST(
            "Hedonist",
            "When you indulge your vice, you may adjust the dice outcome by +/-2. An ally who joins you may do the same.",
            false),
    COMMANDER(
            "Commander",
            "Whenever you lead a group action, gain +1 scale (for example, a small group counts as a medium group). If you lead a group action in combat, you may count multiple 6s from different rolls as a critical.",
            false),
    TRAVELER(
            "Traveler",
            "You're comfortable around unusual cultures and xenos. You gain potency when attempting to consort with or sway them.",
            false),
    PUNCH_IT("Punch It!", "When you spend a gambit on a desperate roll, it counts as risky instead.", false),

    // Scoundrel Abilities
    SERENDIPITOUS("Serendipitous", "Your crew starts with +1 gambit when the pool resets.", true),
    NEVER_TELL_ME_THE_ODDS(
            "Never Tell Me the Odds",
            "You generate gambits on desperate rolls. You may also generate gambits even if you spent a gambit.",
            false),
    I_KNOW_A_GUY(
            "I Know a Guy",
            "When you first dock at a port after being away, pick one and ask the the GM about a job: it's not deadly—it pays well enough—it's not a rush job—it comes from a faction you trust—it targets an enemy you have. You may spend 1 cred per additional feature. ",
            false),
    TENACIOUS(
            "Tenacious", "Penalties from harm are one level less severe (though level 4 harm is still fatal).", false),
    WHEN_THE_CHIPS_ARE_DOWN(
            "When the Chips are Down", "You gain a second use of special armor between each downtime.", false),
    DEVILS_OWN_LUCK(
            "Devil's Own Luck",
            "You may expend your special armor to resist the consequences of blaster fire, or to push yourself when talking your way out of (or running from) trouble.",
            false),
    DAREDEVIL(
            "Daredevil",
            "When you make a desperate roll, you may take +1d. If you do so, do not mark xp in that action's attribute",
            false),
    SHOOT_FIRST(
            "Shoot First",
            "When you attack from hiding or spring a trap, take +1d. When there's a question about who acts first, the answer is you (two characters with Shoot First act simultaneously). ",
            false),
    ASK_QUESTIONS_LATER(
            "Ask Questions Later",
            "When you consort to gather info, you gain +1 effect and can in addition ask: Who might this benefit?",
            false),

    // Speaker Abilities
    AIR_OF_RESPECTABILITY(
            "Air of Respectability", "You get an extra downtime activity to acquire assets or lay low", true),
    FAVORS_OWED(
            "Favors Owed",
            "During downtime, you get +1 d when you acquire assets or lay low. Any time you gather info, take +1d.",
            false),
    PLAYER("Player", "You always know when someone is lying to you.", false),
    INFILTRATOR("Infiltrator", "You are not affected by quality or Tier when you bypass security measures.", false),
    SUBTERFUGE(
            "Subterfuge",
            "You may expend your special armor to resist a consequence of persuasion or suspicion. When you resist with insight, gain +1d.",
            false),
    HEART_TO_HEART(
            "Heart to Heart",
            "When you provide meaningful insight or heartfelt advice that a crewmate follows, you both clear 1 stress.",
            false),
    OLD_FRIENDS(
            "Old Friends",
            "Whenever you land in a new location, write down a friend you know there (see Influential Friends below).",
            false),
    DISARMING(
            "Disarming",
            "Whenever you use a gambit while speaking, hostilities and danger also pause while you speak.",
            false),
    PURPOSE(
            "Purpose",
            "You may expend your special armor to push yourself when outclassed by your opposition, or when under the effects of wounds. When you resist with resolve, gain +1d.",
            false),

    // Stitch Abilities
    IM_A_DOCTOR_NOT_A(
            "I'm a Doctor, Not a ...",
            "You can **push yourself** to roll your **doctor** rating while performing a different action. Say which patient, research, or posting taught you this trick.",
            true),
    PHYSICKER(
            "Physicker",
            "You may study a malady, wounds, or corpse, and gather info from a crime scene. Also, your crew gets +1d to recovery rolls.",
            false),
    PATCH("Patch", "You may doctor someone during a job to allow them to ignore the effects of a harm penalty.", false),

    WELCOME_ANYWHERE(
            "Welcome Anywhere",
            "While wearing your medic garb, you are welcome even in dangerous places. Gain +1d to consort and sway when offering to tend to anyone in need.",
            false),

    UNDER_PRESSURE(
            "Under Pressure",
            "Add a gambit to the pool whenever you or a crew member suffers level 2 or greater harm.",
            false),

    COMBAT_MEDIC(
            "Combat Medic",
            "You may expend your special armor to resist any consequence while tending to a patient. When you doctor someone in combat, clear 1 stress.",
            false),

    MORAL_COMPASS("Moral Compass", "When you do the right thing at cost to yourself, mark xp (any category).", false),

    DR_STRANGE(
            "Dr. Strange",
            "Your research and fields of study are fringe, esoteric, and focus on the mystical. You may always handle Precursor artifacts safely. When you study an artifact or doctor a strange substance, you may ask one: what could this do?—why could this be dangerous?",
            false),

    BOOK_LEARNING(
            "Book Learning",
            "You speak a multitude of languages and are broadly educated. Gain +1d when using study during a downtime activity.",
            false),

    // SHIP: Mastodon
    LABOR_UNION(
            "Labor Union",
            "So long as you don't have a wanted you can freely enter and blend in with any location with active construction going on.",
            false),
    DEMOLITION(
            "Demolition",
            "You gain potency when you attempt to violently destroy inanimate objects. Take +1d to the engagement roll if your plan involves blowing something up.",
            false),
    DUCTAPE_AND_CHEWING_GUM(
            "Ductape & Chewing Gum",
            "When you would lose access to a ship system due to it taking damage you may spend a gambit to have it remain operational until you're out of danger.",
            false),
    JUNK_DOGS(
            "Junk Dogs",
            "You have a keen eye for salvage. When on a salvaging job take +1d to all rolls towards looking for salvageable items.",
            false),
    DAY_JOB(
            "Day Job",
            "As a downtime action you can choose to work a day job and collect 1 cred and take 1 stress.",
            false),
    TOOLS_OF_THE_TRADE(
            "Tools of the Trade",
            "You may treat Communicators, Repair Tools and Plasma Cutters as if they cost 0 load.",
            false),
    BUILDERS_AND_SHAPERS("Builders & Shapers", "Each PC may add +1 action rating to Rig, Scramble or Scrap.", false),

    // SHIP: Firedrake
    OLD_HANDS(
            "Old Hands",
            "When you're at War (-3) with a Hegemony faction, all crew members get +1 to vice rolls and still get two downtime activities instead of just one.",
            false),
    FORGED_IN_FIRE(
            "Forged in Fire",
            "Your crew has been toughened by cruel experience. You each get +1d to all resistance rolls.",
            false),
    SYMPATHIZERS(
            "Sympathizers",
            "Your ideology is especially appealing. When you deal with a crew or faction, the GM will tell you who among them believes in your cause (one, a few, many, or all).",
            false),
    NATURAL_ENEMIES(
            "Natural Enemies", "When you run a job against Hegemony factions, take +1d to the engagement roll.", false),
    SPARK_OF_REBELLION(
            "Spark of Rebellion",
            "If you leave a calling card or highly visible symbol of resistance on your job, gain +2 heat. Your crew gains +1d to vice during the next downtime, and cannot overindulge.",
            false),
    JUST_CAUSE(
            "Just Cause", "When your crew does the right thing at cost to themselves, you may mark a crew xp.", false),
    HEARTS_AND_MINDS(
            "Hearts & Minds",
            "Each crew member may add 1 action rating to command, consort, or sway (up to a max of 3).",
            false),
    ;
    private final String label;
    //    private final Playbook playbook;
    private final String markdown;
    private final boolean isStartingAbility;
}
