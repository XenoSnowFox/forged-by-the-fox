package com.xenosnowfox.forgedbythefox.service.identity;

import com.xenosnowfox.forgedbythefox.models.account.AccountIdentifier;
import com.xenosnowfox.forgedbythefox.models.identity.IdentityIdentifier;
import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder")
public record CreateIdentityRequest(
        @NonNull IdentityIdentifier identifier,
        @NonNull String givenName,
        @NonNull String familyName,
        @NonNull String profilePictureUrl,
        @NonNull AccountIdentifier accountIdentifier) {}
