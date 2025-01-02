package com.xenosnowfox.forgedbythefox.library.models;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Attribute {
    INSIGHT("Insight"),
    PROWESS("Prowess"),
    RESOLVE("Resolve");

    private final String label;

    private final Set<Action> actions = new HashSet<>();

    public Set<Action> getActions() {
        if (this.actions.isEmpty()) {
            Arrays.stream(Action.values())
                    .filter(action -> action.getAttribute().equals(this))
                    .forEach(this.actions::add);
        }
        return Set.copyOf(this.actions);
    }
}
