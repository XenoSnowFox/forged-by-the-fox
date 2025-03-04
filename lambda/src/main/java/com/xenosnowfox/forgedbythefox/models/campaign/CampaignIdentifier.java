package com.xenosnowfox.forgedbythefox.models.campaign;

import com.xenosnowfox.forgedbythefox.util.NanoId;
import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder")
public record CampaignIdentifier(@NonNull String value) {

    public static CampaignIdentifier fromUrn(final String withUrn) {
        final String[] parts = withUrn.split("CAMPAIGN:", 2);
        if (parts.length != 2 || !parts[0].isBlank() || parts[1].isBlank()) {
            throw new IllegalArgumentException();
        }

        return new CampaignIdentifier(parts[1].trim());
    }

    public CampaignIdentifier {
        value = value.trim();
        if (value.isBlank()) {
            throw new IllegalArgumentException("Identifier value must not be blank or empty.");
        }
    }

    @Override
    public String toString() {
        return value;
    }

    public String toUrn() {
        return "CAMPAIGN:" + this.value;
    }

    public static CampaignIdentifier random() {
        return new CampaignIdentifier(NanoId.random());
    }
}
