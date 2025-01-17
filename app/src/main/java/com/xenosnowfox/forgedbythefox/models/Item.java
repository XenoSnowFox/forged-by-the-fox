package com.xenosnowfox.forgedbythefox.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum Item {
    // Common Items
    BLASTER_PISTOL(
            "Blaster Pistol",
            "A pistol that shoots bolts of hot plasma at high speeds. Accurate only at close range. Makes \"pew pew\" noises (mandatory).",
            1,
            null),
    SECOND_BLASTER_PISTOL(
            "Blaster Pistol (2nd)",
            "A pistol that shoots bolts of hot plasma at high speeds. Accurate only at close range. Makes \"pew pew\" noises (mandatory).",
            1,
            null),
    MELEE_WEAPON(
            "Melee Weapon",
            "Sharp. Blunt. Pointy. Stabby. Slicy. All different sizes. Some come with laser edges. Some vibrate ...ohh. Batteries included.",
            1,
            null),
    HEAVY_BLASTER(
            "Heavy Blaster",
            "Can do considerable damage to vehicles, heavy armor, and constructions like unshielded doors. Has about a dozen shots.",
            2,
            null),
    DETONATOR(
            "Detonator",
            "Extremely deadly explosive weapon. Fits in the palm of your hand and can be thrown. Takes care of those shielded doors that heavy blasters can't handle. Illegal. You shouldn't have this. No, really.",
            1,
            null),
    HACKING_TOOLS(
            "Hacking Tools",
            "Deck, splicing wires, plugs and ports, keypad crackers, specialized software, custom-modifier chips, rainbow dictionaries, automated exploits. What every growing hacker needs.",
            1,
            null),
    REPAIR_TOOLS(
            "Repair Tools",
            "Things you need to fix ship engines, speeders, hovercars, and the like. Also, tools to hot-splice consoles and tweak machinery. Hammers, a welder, screwdrivers, wrenches, battery chargers, spray-painters.",
            1,
            null),
    MEDKIT(
            "Medkit",
            "Blood for a few common races, gauze, anti-radiation injector, laser scalpel, antiseptics, thread, painkillers.",
            2,
            null),
    SPY_GEAR(
            "Spy Gear",
            "Disguises, voice modulators, mini-cameras, thermal scanners, false thumbprints, and audio filters.",
            1,
            null),
    ILLICIT_DRUGS("Illicit Drugs", "What's your poison, space cowboy?", 0, null),
    COMMUNICATOR(
            "Communicator", "Has a few bands, likely even a few encrypted. Works only when within orbit.", 0, null),
    ARMOR(
            "Armor",
            "Really unsubtle, full body stuff. Stops a few bolts. Will shrug off a knife without noticing. Powered. Assists in movement.",
            2,
            null),
    SPACESUIT(
            "Spacesuit",
            "Some radiation protection, survival in toxic atmospheres, EVA. Half a day of oxygen (or other substance you breathe).",
            2,
            null),

    // Mechanic Items
    FINE_HACKING_RIG(
            "Fine Hacking Rig",
            "Visualization goggles, unpublished exploits, overclocked non-market chips, optical vampire taps.",
            1,
            Playbook.MECHANIC),
    FINE_SHIP_REPAIR_TOOL(
            "Fine Ship Repair Tool",
            "Power-assisted wrenches, a sonic drill, testing probes, power calibrators, a rivet gun.",
            2,
            Playbook.MECHANIC),
    SMALL_DRONE(
            "Small Drone",
            "Small, remote-controlled drone with cameras. May be able to carry something light.",
            0,
            Playbook.MECHANIC),
    VISION_ENHANCING_GOGGLES(
            "Vision Enhancing Goggles",
            "Eyewear with settings for thermal and ultraviolet, and magnification levels in the thousands.",
            1,
            Playbook.MECHANIC),
    SPARE_PARTS(
            "Spare Parts",
            "Usually for ship repairs and electronics. Often forgotten in a pocket or tool belt.",
            1,
            Playbook.MECHANIC),
    GENIUS_PET(
            "Genius Pet",
            "Incapable of speaking, but can understand language and assist with basic tasks. Likes you. Really cute. Anticipates your actions.",
            0,
            Playbook.MECHANIC),

    // Muscle Items
    VERA_A_FINE_SNIPER_RIFLE(
            "Vera, a Fine Sniper Rifle",
            "A full-bore auto-lock with customized trigger, double cartridge, thorough gauge. Can fire mystic ammo.",
            2,
            Playbook.MUSCLE),
    ZMEI_A_FINE_FLAMETHROWER(
            "Zmei, a Fine Flamethrower",
            "For those times when you really need to heat things up. Settings for regular and extra crispy.",
            2,
            Playbook.MUSCLE),
    SUNDER_A_FINE_VIBRO_BLADE(
            "Sunder, a Fine Vibro-Blade", "Cuts through almost any material. Decorated blade.", 1, Playbook.MUSCLE),
    ZARATHUSTRA_DETONATOR_LAUNCHER(
            "Zarathustra, Detonator Launcher", "Fires detonators at high velocity.", 2, Playbook.MUSCLE),
    FINE_MARTIAL_ART_STYLE("Fine Martial Art Style", "Your own custom blend of combat techniques.", 0, Playbook.MUSCLE),
    MYSTIC_AMMUNITION(
            "Mystic Ammunition",
            "A large-caliber shell fired from a specialized gun that releases mystic energies when it hits. Grants potency against mystic targets.",
            0,
            Playbook.MUSCLE),

    // Mystic Items
    FINE_MELEE_WEAPON(
            "Fine Melee Weapon", "Antiquated weapon that acts as an extension of your body.", 2, Playbook.MYSTIC),
    OFFERINGS(
            "Offerings",
            "A candle, oil lamp, flowers, food, water, incense, pebbles from your journey.",
            1,
            Playbook.MYSTIC),
    TRAPPINGS_OF_RELIGION("Trappings of Religion", "Scrolls, texts, icons, cups and bowls, bells.", 1, Playbook.MYSTIC),
    OUTDATED_RELIGIOUS_OUTFIT("Outdated Religious Outfit", "Robes, worn cloaks, sandals, etc.", 0, Playbook.MYSTIC),
    MEMENTO_OF_YOUR_TRAVELS(
            "Memento of Your Travels",
            "A small statue, outdated currency, a lock of hair, a picture.",
            0,
            Playbook.MYSTIC),
    PRECURSOR_ARTIFACT(
            "Precursor Artifact", "A small object made of ancient materials. Precursor tech.", 1, Playbook.MYSTIC),

    // Pilot Items
    FINE_CUSTOMIZED_SPACESUIT(
            "Fine Customized Spacesuit", "Sweet decals, emergency beacon, some thrust.", 2, Playbook.PILOT),
    FINE_SMALL_URBOT(
            "Fine Small Urbot",
            "Supports piloting and can carry a few items. Seems eerily sentient.",
            2,
            Playbook.PILOT),
    FINE_MECHANICS_KIT(
            "Fine Mechanics Kit", "Hand-held scanners, hull patch kit, assortment of hand tools.", 1, Playbook.PILOT),
    GRAPPLING_HOOK("Grappling Hook", "Small, but mechanized. Can pull you up. Fits in your belt.", 1, Playbook.PILOT),
    GUILD_LICENSE(
            "Guild License",
            "Legit pilot certification (may not be yours). Will allow you passage through a jumpgate.",
            1,
            Playbook.PILOT),
    VICTORY_CIGARS("Victory Cigars", "Enough to share with a few choice people.", 0, Playbook.PILOT),

    // Scoundrel Items
    FINE_BLASTER_PISTOL(
            "Fine Blaster Pistol", "Customized or strange. Can fire mystic ammunition.", 1, Playbook.SCOUNDREL),
    PAIR_OF_FINE_BLASTER_PISTOLS(
            "Pair of Fine Blaster Pistols",
            "Customized or strange. Can fire mystic ammunition.",
            2,
            Playbook.SCOUNDREL),
    FINE_COAT(
            "Fine Coat",
            "A heavy but well-made and well-kept coat. Distinctive and with a history.",
            1,
            Playbook.SCOUNDREL),
    LOADED_DICE_OR_TRICK_HOLO_CARDS(
            "Loaded Dice or Trick Holo-Cards",
            "Gambling accoutrement's subtly altered to favor particular outcomes.",
            0,
            Playbook.SCOUNDREL),
    FORGED_DOCUMENTS(
            "Forged Documents",
            "Reasonably well-made facsimiles of documents that would never actually be given to someone like you.",
            0,
            Playbook.SCOUNDREL),
    MYSTIC_AMMUNITION_2(
            "Mystic Ammunition",
            "A large-caliber shell fired from a specialized gun that releases mystic energies when it hits. Grants potency against mystic targets.",
            0,
            Playbook.SCOUNDREL),
    PERSONAL_MEMENTO(
            "Personal Memento",
            "A keepsake you cherish. A locket, small holo, music from your homeworld.",
            0,
            Playbook.SCOUNDREL),

    // Speaker Items
    FINE_CLOTHES("Fine Clothes", "Silk sarongs, suits, fine blue capes.", 1, Playbook.SPEAKER),
    LEGITIMATE_ID(
            "Legitimate ID",
            "A properly encoded Hegemonic ID indicating your legitimate station in the Hegemony.",
            0,
            Playbook.SPEAKER),
    LUXURY_ITEM_FREE(
            "Luxury Item (Free)",
            "Fine brandies, small but thoughtful gifts, spices and perfumes, fine instruments, popular games etc.",
            0,
            Playbook.SPEAKER),
    LUXURY_ITEM(
            "Luxury Item",
            "Fine brandies, small but thoughtful gifts, spices and perfumes, fine instruments, popular games etc.",
            1,
            Playbook.SPEAKER),
    LARGE_LUXURY_ITEM(
            "Large Luxury Item",
            "Fine brandies, small but thoughtful gifts, spices and perfumes, fine instruments, popular games etc.",
            2,
            Playbook.SPEAKER),
    MEMENTO_OF_A_PAST_ENCOUNTER(
            "Memento of a Past Encounter",
            "A distinctive piece of jewelery, a fine blade with a House crest, a signet ring, a small statue.",
            0,
            Playbook.SPEAKER),

    // Stitch Items
    FINE_MEDKIT(
            "Fine Medkit",
            "Better stocked than the standard. Skin staples, diagnostic hand scanners, synthflesh, bone stabilizers, spray hypos, anti-venom, and a wider selection of drugs.",
            2,
            Playbook.STITCH),
    FINE_BEDSIDE_MANNER(
            "Fine Bedside Manner",
            "Charm that sets patients at ease. Some Stitches never bother to bring this.",
            0,
            Playbook.STITCH),
    FINE_CLOTHING("Fine Clothing", "A suit or outfit for fancy dinner parties and high society.", 1, Playbook.STITCH),
    RECOGNIZABLE_MEDIC_GARB(
            "Recognizable Medic Garb",
            "The common red medic outfit bearing the official white medic seal of the Hegemony. Recognizable from a distance.",
            0,
            Playbook.STITCH),
    CANDIES_AND_TREATS("Candies and Treats", "For those extra braze customers.", 1, Playbook.STITCH),
    SYRINGES_AND_APPLICATORS(
            "Syringes and Applicators",
            "Syringes, injectors, patch applicators. Many can be palmed easily.",
            0,
            Playbook.STITCH);

    private final String label;

    private final String description;

    private final int loadCost;

    private final Playbook playbook;
}
