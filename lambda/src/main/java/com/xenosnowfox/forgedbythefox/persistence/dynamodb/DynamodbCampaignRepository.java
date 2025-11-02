package com.xenosnowfox.forgedbythefox.persistence.dynamodb;

import com.xenosnowfox.forgedbythefox.models.campaign.Campaign;
import com.xenosnowfox.forgedbythefox.models.campaign.CampaignIdentifier;
import com.xenosnowfox.forgedbythefox.persistence.CampaignRepository;
import com.xenosnowfox.forgedbythefox.service.campaign.CampaignSchema;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

public class DynamodbCampaignRepository extends AbstractDynamodbRepository<Campaign> implements CampaignRepository {
    public DynamodbCampaignRepository(final DynamoDbEnhancedClient withDynamoDbEnhancedClient) {
        super(withDynamoDbEnhancedClient, CampaignSchema.getTableSchema());
    }

    @Override
    public Campaign retrieve(final CampaignIdentifier withIdentifier) {
        return super.retrieve(withIdentifier.toUrn(), "details");
    }
}
