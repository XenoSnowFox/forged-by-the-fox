package com.xenosnowfox.forgedbythefox.models.account;

import java.time.Instant;
import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder")
public record Account(
        @NonNull AccountIdentifier identifier,
        @NonNull AccountName name,
        @NonNull String profilePictureUrl,
        @NonNull Instant timestampCreated) {}
