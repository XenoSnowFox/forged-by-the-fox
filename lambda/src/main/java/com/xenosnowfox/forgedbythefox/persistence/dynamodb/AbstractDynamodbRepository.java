package com.xenosnowfox.forgedbythefox.persistence.dynamodb;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public abstract class AbstractDynamodbRepository<IDENTIFIER, MODEL> {

    @NonNull @Getter(AccessLevel.PROTECTED)
    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @NonNull @Getter(AccessLevel.PROTECTED)
    private final DynamoDbTable<MODEL> dynamoDbTable;

    public AbstractDynamodbRepository(
            final DynamoDbEnhancedClient withDynamoDbEnhancedClient, final TableSchema<MODEL> withTableSchema) {
        this.dynamoDbEnhancedClient = Objects.requireNonNull(withDynamoDbEnhancedClient);
        this.dynamoDbTable = this.dynamoDbEnhancedClient.table("forged-by-the-fox", withTableSchema);
    }

    protected MODEL retrieve(final String withPartitionKey, final String withSortKey) {
        final Key key = Key.builder()
                .partitionValue(withPartitionKey)
                .sortValue(withSortKey)
                .build();
        return this.getDynamoDbTable().getItem(key);
    }
}
