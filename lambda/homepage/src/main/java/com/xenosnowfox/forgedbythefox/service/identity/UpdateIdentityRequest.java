package com.xenosnowfox.forgedbythefox.service.identity;

import com.xenosnowfox.forgedbythefox.models.identity.IdentityIdentifier;
import java.util.Optional;
import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder")
public record UpdateIdentityRequest(
        @NonNull IdentityIdentifier identifier, String givenName, String familyName, String profilePictureUrl) {

    public Optional<String> optGivenName() {
        return Optional.ofNullable(givenName);
    }

    public Optional<String> optFamilyName() {
        return Optional.ofNullable(familyName);
    }

    public Optional<String> optProfilePictureUrl() {
        return Optional.ofNullable(profilePictureUrl);
    }
}
