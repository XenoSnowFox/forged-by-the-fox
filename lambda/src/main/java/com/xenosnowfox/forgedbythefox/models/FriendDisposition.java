package com.xenosnowfox.forgedbythefox.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum FriendDisposition {
    CLOSE_FRIEND("Close Friend"),
    FRIEND("Friend"),
    RIVAL("Rival");

    final String label;
}
