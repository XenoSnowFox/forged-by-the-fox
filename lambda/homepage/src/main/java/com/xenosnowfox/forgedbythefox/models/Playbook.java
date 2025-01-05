package com.xenosnowfox.forgedbythefox.models;

import java.util.Arrays;
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

    private final String title;

    private final Map<Action, Integer> startingActions;

    private Ability startingAbility;

    private Set<Ability> specialAbilities;

    public Ability getStartingAbility() {
        if (this.startingAbility == null) {
            this.startingAbility = Arrays.stream(Ability.values())
                    .filter(Ability::isStartingAbility)
                    .filter(x -> x.getPlaybook().equals(this))
                    .findFirst()
                    .orElse(null);
        }
        return this.startingAbility;
    }

    public Set<Ability> getSpecialAbilities() {
        if (this.specialAbilities == null) {
            this.specialAbilities = Arrays.stream(Ability.values())
                    .filter(ability -> !ability.isStartingAbility())
                    .filter(x -> x.getPlaybook().equals(this))
                    .collect(Collectors.toSet());
        }
        return this.specialAbilities;
    }
}
