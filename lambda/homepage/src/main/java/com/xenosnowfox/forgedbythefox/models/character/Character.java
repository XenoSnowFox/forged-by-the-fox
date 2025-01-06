package com.xenosnowfox.forgedbythefox.models.character;

import com.xenosnowfox.forgedbythefox.models.Ability;
import com.xenosnowfox.forgedbythefox.models.Action;
import com.xenosnowfox.forgedbythefox.models.Attribute;
import com.xenosnowfox.forgedbythefox.models.Playbook;
import com.xenosnowfox.forgedbythefox.models.account.AccountIdentifier;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder", toBuilder = true)
public record Character(
        @NonNull CharacterIdentifier identifier,
        @NonNull AccountIdentifier accountIdentifier,
        @NonNull Instant timestampCreated,
        String name,
        String alias,
        String appearance,
        String heritage,
        String background,
        String vice,
        @NonNull Playbook playbook,
        @NonNull CharacterExperience experience,
        Map<Action, Integer> actionDots,
        Set<Ability> abilities) {

    public static Duration TIME_TO_LIVE = Duration.ofDays(31);

    public Character {
        actionDots = new HashMap<>(actionDots == null ? playbook.startingActions() : actionDots);
        abilities = Optional.ofNullable(abilities)
                .or(() -> Optional.ofNullable(playbook.startingAbility()).map(Set::of))
                .map(HashSet::new)
                .orElse(new HashSet<>());
    }

    public int dotsForAttribute(final Attribute withAttribute) {
        return Math.clamp(
                withAttribute.actions().stream()
                        .map(this::dotsForAction)
                        .filter(d -> d > 0)
                        .count(),
                0,
                4);
    }

    public int dotsForAction(final Action withAction) {
        return this.actionDots.compute(withAction, (attributes, value) -> value == null ? 0 : Math.clamp(value, 0, 3));
    }
}
