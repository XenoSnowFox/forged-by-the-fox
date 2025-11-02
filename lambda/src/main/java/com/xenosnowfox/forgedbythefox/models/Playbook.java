package com.xenosnowfox.forgedbythefox.models;

import com.xenosnowfox.forgedbythefox.models.character.CharacterFriend;
import com.xenosnowfox.forgedbythefox.models.character.CharacterFriends;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum Playbook {
    MECHANIC(
            "Mechanic",
            "A gearhead and hacker",
            Map.of(Action.RIG, 2, Action.STUDY, 1),
            Set.of(
                    Ability.TINKER,
                    Ability.BAILING_WIRE_AND_MECHTAPE,
                    Ability.CONSTRUCT_WHISPERER,
                    Ability.JUNKYARD_HUNTER,
                    Ability.HACKER,
                    Ability.FIXED,
                    Ability.MECHANICS_HEART,
                    Ability.OVERCLOCK,
                    Ability.ANALYST),
            Set.of(Contact.SLICE, Contact.NISA, Contact.STEV, Contact.LEN, Contact.KENN),
            new CharacterFriends(
                    "Colorful",
                    Map.ofEntries(
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Slice", "a junkyard owner", FriendDisposition.FRIEND)),
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Nisa", "a previous employer", FriendDisposition.FRIEND)),
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Stev", "a gambler of ill repute", FriendDisposition.FRIEND)),
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Len", "a black market dealer", FriendDisposition.FRIEND)),
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Kenn", "a family member", FriendDisposition.FRIEND)))),
            Set.of("You addressed a tough challenge with technical skill or ingenuity.")),
    MUSCLE(
            "Muscle",
            "A dangerous and intimidating fighter",
            Map.of(Action.SCRAP, 2, Action.COMMAND, 1),
            Set.of(
                    Ability.UNSTOPPABLE,
                    Ability.WRECKING_CREW,
                    Ability.BACKUP,
                    Ability.BATTLEBORN,
                    Ability.BODYGUARD,
                    Ability.FLESH_WOUND),
            Set.of(Contact.KRIEGER, Contact.SHOD, Contact.CHON_ZEK, Contact.YAZU, Contact.AYA),
            new CharacterFriends(
                    "Deadly",
                    Map.ofEntries(
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Krieger", "a fine blaster pistol", FriendDisposition.FRIEND)),
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Shod", "a weapons dealer", FriendDisposition.FRIEND)),
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Chon-zek", "a bounty hunter", FriendDisposition.FRIEND)),
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Yazu", "a crooked cop", FriendDisposition.FRIEND)),
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Aya", "an assassin", FriendDisposition.FRIEND)))),
            Set.of("You addressed a tough challenge with force or threats.")),
    MYSTIC(
            "Mystic",
            "A galactic wanderer in touch with the Way",
            Map.of(Action.SCRAMBLE, 1, Action.ATTUNE, 2),
            Set.of(),
            Set.of(Contact.HORUX, Contact.HICKS, Contact.LAXX, Contact.RYE, Contact.BLISH),
            new CharacterFriends(
                    "Weird",
                    Map.ofEntries(
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Horux", "a former teacher", FriendDisposition.FRIEND)),
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Hicks", "a mystic goods supplier", FriendDisposition.FRIEND)),
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Laxx", "a xeno", FriendDisposition.FRIEND)),
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Rye", "an unrequited love", FriendDisposition.FRIEND)),
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Blish", "a fellow mystic", FriendDisposition.FRIEND)))),
            Set.of("You addressed a tough challenge with wisdom or the Way.")),
    PILOT(
            "Pilot",
            "A ship-handling wizard and danger addict",
            Map.of(Action.RIG, 1, Action.HELM, 2),
            Set.of(),
            Set.of(Contact.YATTU, Contact.TRIV, Contact.CHOSS, Contact.MERIS, Contact.MAV),
            new CharacterFriends(
                    "Fast",
                    Map.ofEntries(
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Yattu", "a gang boss", FriendDisposition.FRIEND)),
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Triv", "a ship mechanic", FriendDisposition.FRIEND)),
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Choss", "a professional racer", FriendDisposition.FRIEND)),
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Meris", "a scoundrel", FriendDisposition.FRIEND)),
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Mav", "a former mentor", FriendDisposition.FRIEND)))),
            Set.of("You addressed a tough challenge with speed or flair.")),
    SCOUNDREL(
            "Scoundrel",
            "A scrappy and lucky survivor",
            Map.of(Action.SKULK, 1, Action.SWAY, 2),
            Set.of(),
            Set.of(Contact.NYX, Contact.ORA, Contact.JAL, Contact.RHIN, Contact.BATTRO),
            new CharacterFriends(
                    "Quote/Unquote ",
                    Map.ofEntries(
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Nyx", "a moneylender", FriendDisposition.FRIEND)),
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Ora", "an info broker", FriendDisposition.FRIEND)),
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Jal", "a ship mechanic", FriendDisposition.FRIEND)),
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Rhin", "a smuggler", FriendDisposition.FRIEND)),
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Battro", "a bounty hunter", FriendDisposition.FRIEND)))),
            Set.of("You addressed a tough challenge with charm or audacity.")),
    SPEAKER(
            "Speaker",
            "A respectable person on the take",
            Map.of(Action.COMMAND, 1, Action.CONSORT, 2),
            Set.of(),
            Set.of(Contact.ARRYN, Contact.MANDA, Contact.KERRY, Contact.JE_ZEE),
            new CharacterFriends(
                    "Influential",
                    Map.ofEntries(
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Arryn", "a Noble", FriendDisposition.FRIEND)),
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Manda", "a Guild member", FriendDisposition.FRIEND)),
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Kerry", "a doctor", FriendDisposition.FRIEND)),
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Je-zee", "a diplomat", FriendDisposition.FRIEND)))),
            Set.of("You addressed a tough challenge with deception or influence.")),
    STITCH(
            "Stitch",
            "Spacefaring healer or scientist",
            Map.of(Action.DOCTOR, 2, Action.STUDY, 1),
            Set.of(),
            Set.of(Contact.JACKEV, Contact.ALBEN, Contact.DITHA, Contact.JUDA, Contact.LYNIE),
            new CharacterFriends(
                    "Old",
                    Map.ofEntries(
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Jackev", "a drug dealer", FriendDisposition.FRIEND)),
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Alben", "a former patient", FriendDisposition.FRIEND)),
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Ditha", "a family member", FriendDisposition.FRIEND)),
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Juda", "a doctor", FriendDisposition.FRIEND)),
                            Map.entry(
                                    UUID.randomUUID().toString(),
                                    new CharacterFriend("Lynie", "a hospital admin", FriendDisposition.FRIEND)))),
            Set.of("You addressed a tough challenge with insight or compassion."));

    private static Set<Item> commonItems;

    private final String label;

    private final String tagline;

    private final Map<Action, Integer> startingActions;

    private Ability startingAbility;

    private Set<Ability> specialAbilities;

    private Set<Item> exclusiveItems;

    private final @NonNull Set<Ability> abilities;

    private final @NonNull Set<Contact> contacts;

    private final @NonNull CharacterFriends initialFriends;

    private final @NonNull Set<String> xpTriggers;

    public Ability startingAbility() {
        if (this.startingAbility == null) {
            this.startingAbility = Arrays.stream(Ability.values())
                    .filter(Ability::isStartingAbility)
                    .filter(x -> x.getPlaybook().equals(this))
                    .findFirst()
                    .orElse(null);
        }
        return this.startingAbility;
    }

    public Set<Ability> specialAbilities() {
        if (this.specialAbilities == null) {
            this.specialAbilities = Arrays.stream(Ability.values())
                    .filter(ability -> !ability.isStartingAbility())
                    .filter(x -> x.getPlaybook().equals(this))
                    .sorted(Comparator.comparing(Enum::name)) // sort while streaming
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        return this.specialAbilities;
    }

    public Set<Item> exclusiveItems() {
        if (this.exclusiveItems == null) {
            this.exclusiveItems = Arrays.stream(Item.values())
                    .filter(item -> this.equals(item.playbook()))
                    .sorted(Comparator.comparing(Item::label)) // sort while streaming
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        return this.exclusiveItems;
    }

    public static Set<Item> commonItems() {
        if (Playbook.commonItems == null) {
            Playbook.commonItems = Arrays.stream(Item.values())
                    .filter(item -> item.playbook() == null)
                    .sorted(Comparator.comparing(Item::label)) // sort while streaming
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        return Playbook.commonItems;
    }
}
