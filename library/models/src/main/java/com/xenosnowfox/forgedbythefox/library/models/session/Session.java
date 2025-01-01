package com.xenosnowfox.forgedbythefox.library.models.session;

import com.xenosnowfox.forgedbythefox.library.models.account.AccountIdentifier;
import java.time.Duration;
import java.time.Instant;
import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder", toBuilder = true)
public record Session(
        @NonNull SessionIdentifier identifier,
        @NonNull AccountIdentifier accountIdentifier,
        @NonNull Instant timestampCreated,
        @NonNull Instant timestampExpires) {

    public static Duration TIME_TO_LIVE = Duration.ofDays(31);
}
