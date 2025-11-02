package com.xenosnowfox.forgedbythefox.models.ship;

import com.xenosnowfox.forgedbythefox.models.Ability;
import com.xenosnowfox.forgedbythefox.models.Identifiable;
import com.xenosnowfox.forgedbythefox.models.Nameable;
import com.xenosnowfox.forgedbythefox.models.ShipSheet;
import com.xenosnowfox.forgedbythefox.models.account.AccountIdentifier;
import com.xenosnowfox.forgedbythefox.models.campaign.CampaignIdentifier;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder", toBuilder = true)
public record Ship(
        @NonNull ShipIdentifier identifier,
        @NonNull AccountIdentifier accountIdentifier,
        CampaignIdentifier campaignIdentifier,
        @NonNull Instant timestampCreated,
        String name,
        ShipSheet shipSheet,
        Set<Ability> abilities)
        implements Identifiable<ShipIdentifier>, Nameable {

    public Ship {
        if (name != null) {
            name = name.trim();
        }
        if (abilities == null) {
            abilities = new HashSet<>();
        }
    }

    public static String DEFAULT_NAME = "Unnamed Ship";
}
