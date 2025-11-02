package com.xenosnowfox.forgedbythefox.models.ship;

import com.xenosnowfox.forgedbythefox.models.ShipSheet;
import com.xenosnowfox.forgedbythefox.models.account.AccountIdentifier;
import com.xenosnowfox.forgedbythefox.models.campaign.CampaignIdentifier;
import java.time.Instant;
import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder", toBuilder = true)
public record Ship(
        @NonNull ShipIdentifier identifier,
        @NonNull AccountIdentifier accountIdentifier,
        CampaignIdentifier campaignIdentifier,
        @NonNull Instant timestampCreated,
        String name,
        ShipSheet shipSheet) {

    public Ship {
        if (name != null) {
            name = name.trim();
        }
    }

    public static String DEFAULT_NAME = "Unnamed Ship";
}
