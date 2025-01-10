package com.xenosnowfox.forgedbythefox.models;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum Attribute {
    INSIGHT("Insight"),
    PROWESS("Prowess"),
    RESOLVE("Resolve");

    private final String label;

    private final Set<Action> actions = new LinkedHashSet<>();

    public Set<Action> actions() {
        if (this.actions.isEmpty()) {
            Arrays.stream(Action.values())
                    .filter(action -> action.attribute().equals(this))
                    .sorted(Comparator.comparing(Enum::name))
                    .forEach(this.actions::add);
        }
        return new LinkedHashSet<>(this.actions);
    }
}
