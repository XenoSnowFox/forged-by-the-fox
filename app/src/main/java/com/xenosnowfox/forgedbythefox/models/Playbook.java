package com.xenosnowfox.forgedbythefox.models;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum Playbook {
    MECHANIC("Mechanic", Map.of(Action.RIG, 2, Action.STUDY, 1)),
    MUSCLE("Muscle", Map.of(Action.SCRAP, 2, Action.COMMAND, 1)),
    MYSTIC("Mystic", Map.of(Action.SCRAMBLE, 1, Action.ATTUNE, 2)),
    PILOT("Pilot", Map.of(Action.RIG, 1, Action.HELM, 2)),
    SCOUNDREL("Scoundrel", Map.of(Action.SKULK, 1, Action.SWAY, 2)),
    SPEAKER("Speaker", Map.of(Action.COMMAND, 1, Action.CONSORT, 2)),
    STITCH("Stitch", Map.of(Action.DOCTOR, 2, Action.STUDY, 1));

    private static Set<Item> commonItems;

    private final String label;

    private final Map<Action, Integer> startingActions;

    private Ability startingAbility;

    private Set<Ability> specialAbilities;

    private Set<Item> exclusiveItems;

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
