package com.xenosnowfox.forgedbythefox.models.character;

import com.xenosnowfox.forgedbythefox.models.FriendDisposition;
import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder", toBuilder = true)
public record CharacterFriend(
        @NonNull String name, @NonNull String description, @NonNull FriendDisposition disposition) {

    public boolean isCloseFriend() {
        return this.disposition == FriendDisposition.CLOSE_FRIEND;
    }

    public boolean isFriend() {
        return this.disposition == FriendDisposition.FRIEND;
    }

    public boolean isRival() {
        return this.disposition == FriendDisposition.RIVAL;
    }
}
