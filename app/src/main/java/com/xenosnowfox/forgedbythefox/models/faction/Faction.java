package com.xenosnowfox.forgedbythefox.models.faction;

import com.xenosnowfox.forgedbythefox.models.campaign.CampaignIdentifier;
import com.xenosnowfox.forgedbythefox.util.RomanNumber;
import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder")
public record Faction(
        @NonNull FactionIdentifier identifier,
        @NonNull CampaignIdentifier campaignIdentifier,
        @NonNull String name,
        FactionAlignmentIdentifier alignment,
        int tier,
        int status) {
    public String tierRating() {
        return RomanNumber.toRoman(this.tier);
    }
}
