package com.xenosnowfox.forgedbythefox.models.campaign;

import com.xenosnowfox.forgedbythefox.models.account.AccountIdentifier;
import com.xenosnowfox.forgedbythefox.models.faction.FactionAlignment;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder")
public record Campaign(
        @NonNull CampaignIdentifier identifier,
        @NonNull CampaignName name,
        @NonNull AccountIdentifier account,
        @NonNull Instant timestampCreated,
        List<FactionAlignment> factionAlignments) {

    public Campaign {
        factionAlignments =
                Optional.ofNullable(factionAlignments).map(ArrayList::new).orElse(new ArrayList<>());
    }
}
