package com.xenosnowfox.forgedbythefox.service.campaign;

import com.xenosnowfox.forgedbythefox.models.campaign.Campaign;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

public class CampaignService {

    private final DynamoDbTable<Campaign> table;

    public CampaignService() {
        this.table = DynamoDbEnhancedClient.create().table("forged-by-the-fox", CampaignSchema.getTableSchema());
    }

    public CampaignCreationExecutor create() {
        return new CampaignCreationExecutor(this.table);
    }

    public CampaignRetrievalExecutor fetch() {
        return new CampaignRetrievalExecutor(this.table);
    }
}
