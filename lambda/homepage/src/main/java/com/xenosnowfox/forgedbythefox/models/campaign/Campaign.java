package com.xenosnowfox.forgedbythefox.models.campaign;

import com.xenosnowfox.forgedbythefox.models.account.AccountIdentifier;
import java.time.Instant;
import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder")
public record Campaign(
        @NonNull CampaignIdentifier identifier,
        @NonNull CampaignName name,
        @NonNull AccountIdentifier account,
        @NonNull Instant timestampCreated) {}
