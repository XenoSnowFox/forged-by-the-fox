package com.xenosnowfox.forgedbythefox.service.faction;

import com.xenosnowfox.forgedbythefox.models.faction.Faction;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

public class FactionManagementService {

	private final DynamoDbTable<Faction> table;

    public FactionManagementService() {
	    DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.create();
        this.table = enhancedClient.table("forged-by-the-fox", FactionSchema.getTableSchema());
    }

    public FactionQueryExecutor query() {
        return new FactionQueryExecutor(this.table);
    }
}
