package com.xenosnowfox.forgedbythefox.models.character;

import com.xenosnowfox.forgedbythefox.models.Attribute;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder", toBuilder = true)
public record CharacterExperience(int playbook, @NonNull Map<Attribute, Integer> attributes) {
    public static final int MAX_PLAYBOOK_EXPERIENCE = 8;
    public static final int MAX_ATTRIBUTE_EXPERIENCE = 6;

    public CharacterExperience {
        final Map<Attribute, Integer> attr = new HashMap<>();
        for (final Attribute value : Attribute.values()) {
            attr.put(value, attributes.getOrDefault(value, 0));
        }
        attributes = attr;
    }

    public int experienceForAttribute(final Attribute withAttribute) {
        return this.attributes.compute(
                withAttribute,
                (attributes, value) -> value == null ? 0 : Math.clamp(value, 0, MAX_ATTRIBUTE_EXPERIENCE));
    }

    public int insight() {
        return this.experienceForAttribute(Attribute.INSIGHT);
    }

    public int prowess() {
        return this.experienceForAttribute(Attribute.PROWESS);
    }

    public int resolve() {
        return this.experienceForAttribute(Attribute.RESOLVE);
    }
}
