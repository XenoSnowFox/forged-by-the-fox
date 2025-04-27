package com.xenosnowfox.forgedbythefox.service.campaign;

import com.xenosnowfox.forgedbythefox.models.campaign.Campaign;
import com.xenosnowfox.forgedbythefox.persistence.CampaignRepository;
import com.xenosnowfox.forgedbythefox.persistence.dynamodb.DynamodbCampaignRepository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class CampaignService {

    private final DynamoDbClient client;
    private final DynamoDbTable<Campaign> table;
    private final CampaignRepository campaignRepository;

    public CampaignService() {
        this.client = DynamoDbClient.create();
        this.table = DynamoDbEnhancedClient.create().table("forged-by-the-fox", CampaignSchema.getTableSchema());
        this.campaignRepository = new DynamodbCampaignRepository(DynamoDbEnhancedClient.create());
    }

    public CampaignCreationExecutor create() {
        return new CampaignCreationExecutor(this.table);
    }

    public CampaignMutationExecutor mutate() {
        return new CampaignMutationExecutor(this.client, this.table);
    }

    public CampaignRetrievalExecutor fetch() {
        return new CampaignRetrievalExecutor(this.campaignRepository);
    }
}
