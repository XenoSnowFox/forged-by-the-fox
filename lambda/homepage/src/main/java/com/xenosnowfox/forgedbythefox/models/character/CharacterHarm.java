package com.xenosnowfox.forgedbythefox.models.character;

import com.xenosnowfox.forgedbythefox.models.HarmLevel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.NonNull;

public class CharacterHarm {

    private final Map<HarmLevel, List<String>> harmMap = new HashMap<>();

    public CharacterHarm() {
        for (HarmLevel harmLevel : HarmLevel.values()) {
            harmMap.put(harmLevel, new ArrayList<>());
        }
    }

    public CharacterHarm(final CharacterHarm other) {
        this();
        if (other == null) {
            return;
        }
        for (HarmLevel harmLevel : HarmLevel.values()) {
            this.get(harmLevel).addAll(other.get(harmLevel));
        }
    }

    public Stream<String> stream(final HarmLevel withHarmLevel) {
        return harmMap.get(withHarmLevel).stream().filter(Objects::nonNull).filter(str -> !str.isBlank());
    }

    public List<String> get(final HarmLevel withHarmLevel) {
        return harmMap.computeIfAbsent(withHarmLevel, l -> new ArrayList<>());
    }

    public void append(@NonNull final HarmLevel withHarmLevel, @NonNull final String withDescription) {
        if (withDescription.isBlank()) {
            return;
        }
        final List<String> harm = this.harmMap.get(withHarmLevel);
        if (harm.size() >= withHarmLevel.maximum()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        harm.add(withDescription);
    }
}
