package com.xenosnowfox.forgedbythefox.services.identitymanagement;

import com.xenosnowfox.forgedbythefox.library.models.account.AccountIdentifier;
import com.xenosnowfox.forgedbythefox.library.models.identity.IdentityIdentifier;
import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder")
public record CreateIdentityRequest(
        @NonNull IdentityIdentifier identifier,
        @NonNull String givenName,
        @NonNull String familyName,
        @NonNull String profilePictureUrl,
        @NonNull AccountIdentifier accountIdentifier) {}
