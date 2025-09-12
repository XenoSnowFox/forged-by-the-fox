package com.xenosnowfox.forgedbythefox.models.character;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder", toBuilder = true)
public record CharacterFriends(@NonNull String type, @NonNull Map<String, CharacterFriend> list) {
    public CharacterFriends(@NonNull final CharacterFriends other) {
        this(other.type, new HashMap<>(other.list));
    }

    public List<CharacterFriend> asSortedList() {
        return list.values().stream()
                .sorted(Comparator.comparing(CharacterFriend::name))
                .toList();
    }
}
