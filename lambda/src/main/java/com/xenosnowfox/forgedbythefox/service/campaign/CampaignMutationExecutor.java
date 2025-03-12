package com.xenosnowfox.forgedbythefox.service.campaign;

import com.xenosnowfox.forgedbythefox.models.campaign.Campaign;
import com.xenosnowfox.forgedbythefox.models.campaign.CampaignIdentifier;
import com.xenosnowfox.forgedbythefox.models.clock.Clock;
import com.xenosnowfox.forgedbythefox.models.dynamodb.DynamoDbUpdateExpressionMutator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;

@Accessors(fluent = true, chain = true)
@RequiredArgsConstructor
public class CampaignMutationExecutor {

    private final DynamoDbClient dynamoDbClient;

    private final DynamoDbTable<Campaign> dynamoDbTable;

    private final DynamoDbUpdateExpressionMutator mutator = new DynamoDbUpdateExpressionMutator();

    @Setter
    private CampaignIdentifier withIdentifier;

    public CampaignMutationExecutor addClock(@NonNull final Clock withClock) {
        final List<AttributeValue> l = Stream.of(withClock)
                .map(clock -> ClockSchema.getTableSchema().itemToMap(clock, true))
                .map(AttributeValue::fromM)
                .toList();

        this.mutator.listAppend("clocks", AttributeValue.fromL(l));
        return this;
    }

    public Campaign orNull() {
        return this.get();
    }

    public Campaign get() {

        final Key key = Key.builder()
                .partitionValue(withIdentifier.toUrn())
                .sortValue("details")
                .build();
        System.out.println(this.mutator.toString());
        final UpdateItemRequest updateRequest = UpdateItemRequest.builder()
                .tableName(dynamoDbTable.tableName())
                .key(key.primaryKeyMap(dynamoDbTable.tableSchema()))
                .applyMutation(this.mutator)
                .returnValues(ReturnValue.ALL_NEW)
                .build();
        System.out.println(updateRequest);

        final UpdateItemResponse updateResponse = this.dynamoDbClient.updateItem(updateRequest);

        return Optional.of(updateResponse)
                .map(UpdateItemResponse::attributes)
                .map(this.dynamoDbTable.tableSchema()::mapToItem)
                .orElse(null);
    }
}
