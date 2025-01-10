package com.xenosnowfox.forgedbythefox.service.character;

import com.xenosnowfox.forgedbythefox.models.account.AccountIdentifier;
import com.xenosnowfox.forgedbythefox.models.campaign.CampaignIdentifier;
import com.xenosnowfox.forgedbythefox.models.character.Character;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@Accessors(fluent = true, chain = true)
@RequiredArgsConstructor
public class CharacterQueryExecutor {

    private final DynamoDbIndex<Character> dynamoDbIndex;

    @Setter
    private AccountIdentifier withAccount;

    @Setter
    private CampaignIdentifier withCampaign;

    public Set<Character> orNothing() {
        return this.get();
    }

    public Set<Character> get() {
        final Set<Character> characterSet = new HashSet<>();

        final Key key = Key.builder()
                .partitionValue(withAccount.toUrn())
                .sortValue("CHARACTER")
                .build();

        final QueryConditional queryConditional = QueryConditional.keyEqualTo(key);

        this.dynamoDbIndex
                .query(b -> {
                    b.queryConditional(queryConditional);

                    if (withCampaign != null) {
                        final Expression expression = Expression.builder()
                                .expression("#k1 = :k1")
                                .putExpressionName("#k1", "campaign-identifier")
                                .putExpressionValue(":k1", AttributeValue.fromS(withCampaign.toUrn()))
                                .build();
                        b.filterExpression(expression);
                    }
                })
                .stream()
                .map(Page::items)
                .flatMap(Collection::stream)
                .forEach(characterSet::add);

        return characterSet;
    }
}
