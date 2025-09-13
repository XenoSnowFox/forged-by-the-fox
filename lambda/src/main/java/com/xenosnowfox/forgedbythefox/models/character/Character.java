package com.xenosnowfox.forgedbythefox.models.character;

import com.xenosnowfox.forgedbythefox.models.Ability;
import com.xenosnowfox.forgedbythefox.models.Action;
import com.xenosnowfox.forgedbythefox.models.Attribute;
import com.xenosnowfox.forgedbythefox.models.Item;
import com.xenosnowfox.forgedbythefox.models.Load;
import com.xenosnowfox.forgedbythefox.models.Playbook;
import com.xenosnowfox.forgedbythefox.models.Trauma;
import com.xenosnowfox.forgedbythefox.models.account.AccountIdentifier;
import com.xenosnowfox.forgedbythefox.models.campaign.CampaignIdentifier;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder", toBuilder = true)
public record Character(
        @NonNull CharacterIdentifier identifier,
        @NonNull AccountIdentifier accountIdentifier,
        CampaignIdentifier campaignIdentifier,
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
        Set<Ability> abilities,
        int stress,
        Set<Trauma> trauma,
        CharacterHarm harm,
        Load load,
        Set<Item> items,
        CharacterFriends friends,
        int credits,
        int stash)
        implements Comparable<Character> {

    public static Duration TIME_TO_LIVE = Duration.ofDays(31);

    public static String UNNAMED_CHARACTER = "Unnamed Character";

    public Character {
        actionDots = new HashMap<>(actionDots == null ? playbook.startingActions() : actionDots);

        abilities = Optional.ofNullable(abilities)
                .or(() -> Optional.ofNullable(playbook.startingAbility()).map(Set::of))
                .map(HashSet::new)
                .orElse(new HashSet<>())
                .stream()
                .sorted(Comparator.comparing(Enum::name)) // sort while streaming
                .collect(Collectors.toCollection(LinkedHashSet::new));

        trauma = Optional.ofNullable(trauma).map(HashSet::new).orElse(new HashSet<>());

        stress = Math.clamp(stress, 0, 9);

        harm = new CharacterHarm(harm);

        load = load == null ? Load.NORMAL : load;

        items = Optional.ofNullable(items).map(HashSet::new).orElse(new HashSet<>());

        heritage = Optional.ofNullable(heritage).map(String::trim).orElse("");
        background = Optional.ofNullable(background).map(String::trim).orElse("");
        vice = Optional.ofNullable(vice).map(String::trim).orElse("");

        friends = Optional.ofNullable(friends).orElse(new CharacterFriends(playbook.initialFriends()));
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

    public int currentLoadAmount() {
        return this.items.stream().map(Item::loadCost).reduce(Integer::sum).orElse(0);
    }

    @Override
    public int compareTo(final Character o) {
        return Optional.ofNullable(this.name)
                .orElse(UNNAMED_CHARACTER)
                .compareTo(Optional.ofNullable(o.name).orElse(UNNAMED_CHARACTER));
    }
}
