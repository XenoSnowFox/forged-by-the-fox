package com.xenosnowfox.forgedbythefox.service.faction;

import com.xenosnowfox.forgedbythefox.models.campaign.CampaignIdentifier;
import com.xenosnowfox.forgedbythefox.models.faction.Faction;
import com.xenosnowfox.forgedbythefox.models.faction.FactionAlignmentIdentifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@Accessors(fluent = true, chain = true)
@RequiredArgsConstructor
public class FactionQueryExecutor {

    private final DynamoDbTable<Faction> dynamoDbTable;

    @Setter
    private CampaignIdentifier withCampaign;

    @Setter
    private FactionAlignmentIdentifier withAlignment;

    public Set<Faction> orNothing() {
        return this.get();
    }

    public Set<Faction> get() {
        final Set<Faction> resultSet = new HashSet<>();

        final Key key = Key.builder()
                .partitionValue(withCampaign.toUrn())
                .sortValue("FACTION:")
                .build();

        final QueryConditional queryConditional = QueryConditional.sortBeginsWith(key);

        this.dynamoDbTable
                .query(b -> {
                    b.queryConditional(queryConditional);

                    if (withAlignment != null) {
                        final Expression expression = Expression.builder()
                                .expression("#k1 = :k1")
                                .putExpressionName("#k1", "alignment")
                                .putExpressionValue(":k1", AttributeValue.fromS(withAlignment.toUrn()))
                                .build();
                        b.filterExpression(expression);
                    }
                })
                .stream()
                .map(Page::items)
                .flatMap(Collection::stream)
                .forEach(resultSet::add);

        return resultSet;
    }
}
