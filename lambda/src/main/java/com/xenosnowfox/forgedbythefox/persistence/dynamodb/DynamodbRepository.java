package com.xenosnowfox.forgedbythefox.persistence.dynamodb;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class DynamodbRepository<MODEL> extends AbstractDynamodbRepository<MODEL> {

    public DynamodbRepository(
            final DynamoDbEnhancedClient withDynamoDbEnhancedClient, final TableSchema<MODEL> withTableSchema) {
        super(withDynamoDbEnhancedClient, withTableSchema);
    }

    public MODEL retrieve(final String withPartitionKey, final String withSortKey) {
        return super.retrieve(withPartitionKey, withSortKey);
    }
}
