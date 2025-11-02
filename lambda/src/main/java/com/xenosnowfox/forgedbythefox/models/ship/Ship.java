package com.xenosnowfox.forgedbythefox.models.ship;

import com.xenosnowfox.forgedbythefox.models.Ability;
import com.xenosnowfox.forgedbythefox.models.Identifiable;
import com.xenosnowfox.forgedbythefox.models.Nameable;
import com.xenosnowfox.forgedbythefox.models.ShipModule;
import com.xenosnowfox.forgedbythefox.models.ShipModuleType;
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
        Set<Ability> abilities,
        Set<ShipModule> modules)
        implements Identifiable<ShipIdentifier>, Nameable {

    public Ship {
        if (name != null) {
            name = name.trim();
        }
        if (abilities == null) {
            abilities = new HashSet<>();
        }
        if (modules == null) {
            modules = new HashSet<>();
        }
    }

    public static String DEFAULT_NAME = "Unnamed Ship";

    public int upkeepCost() {
        final long systems = this.modules().stream()
                .filter(module -> module.type() != ShipModuleType.AUXILIARY)
                .mapToInt(e -> 1)
                .sum();
        final long crewQuality = 1;
        return (int) Math.floorDiv(systems + crewQuality, 4);
    }
}
