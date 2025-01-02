package com.xenosnowfox.forgedbythefox.library.models.character;

import com.xenosnowfox.forgedbythefox.library.models.Playbook;
import com.xenosnowfox.forgedbythefox.library.models.account.AccountIdentifier;
import java.time.Duration;
import java.time.Instant;
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
        @NonNull Playbook playbook) {

    public static Duration TIME_TO_LIVE = Duration.ofDays(31);
}
