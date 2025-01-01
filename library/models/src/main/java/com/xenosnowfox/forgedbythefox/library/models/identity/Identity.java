package com.xenosnowfox.forgedbythefox.library.models.identity;

import com.xenosnowfox.forgedbythefox.library.models.account.AccountIdentifier;
import java.time.Instant;
import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder")
public record Identity(
        @NonNull IdentityIdentifier identifier,
        @NonNull String givenName,
        @NonNull String familyName,
        @NonNull String profilePictureUrl,
        @NonNull AccountIdentifier accountIdentifier,
        @NonNull Instant timestampCreated) {}