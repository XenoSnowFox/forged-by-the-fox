package com.xenosnowfox.forgedbythefox.service.campaign;

import com.xenosnowfox.forgedbythefox.models.campaign.Campaign;
import com.xenosnowfox.forgedbythefox.models.campaign.CampaignIdentifier;
import java.util.Optional;

import com.xenosnowfox.forgedbythefox.persistence.CampaignRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

@Accessors(fluent = true, chain = true)
@RequiredArgsConstructor
public class CampaignRetrievalExecutor {

    private final CampaignRepository campaignRepository;

    @Setter
    private CampaignIdentifier withIdentifier;

    public Campaign get() {
        final Key key = Key.builder()
                .partitionValue(withIdentifier.toUrn())
                .sortValue("details")
                .build();
        return this.campaignRepository.retrieve(this.withIdentifier);
    }

    public Campaign orElse(final Campaign withAlternativeCampaign) {
        return Optional.ofNullable(this.get()).orElse(withAlternativeCampaign);
    }
}
