package com.xenosnowfox.forgedbythefox.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum Trauma {
    COLD("Cold", "You're not moved by emotional appeals or social bonds."),
    HAUNTED(
            "Haunted",
            "You're often lost in reverie, reliving past horrors, seeing things from your past or that others may not see."),
    OBSESSED("Obsessed", "You're enthralled by one thing: an activity, a person, a goal, an ideology."),
    PARANOID("Paranoid", "You imagine danger everywhere; you can't trust others."),
    RECKLESS("Reckless", "You have little regard for you own safety, best interest, or well-being."),
    SOFT("Soft", "You lose your edge; you become sentimental, passive, gentle."),
    UNSTABLE(
            "Unstable",
            "Your emotional state is volatile. You can instantly rage, fall into despair, act impulsively, or freeze up."),
    VICIOUS("Vicious", "You seek out opportunities to hurt people, even for no good reason.");

    private final String label;

    private final String description;
}
