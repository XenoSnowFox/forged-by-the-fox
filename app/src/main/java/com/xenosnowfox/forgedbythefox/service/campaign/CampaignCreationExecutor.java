package com.xenosnowfox.forgedbythefox.service.campaign;

import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.account.AccountIdentifier;
import com.xenosnowfox.forgedbythefox.models.campaign.Campaign;
import com.xenosnowfox.forgedbythefox.models.campaign.CampaignIdentifier;
import com.xenosnowfox.forgedbythefox.models.campaign.CampaignName;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;

@RequiredArgsConstructor
public class CampaignCreationExecutor {

    private final DynamoDbTable<Campaign> dynamoDbTable;

    private final Campaign.Builder campaignBuilder = Campaign.builder();

    public void setAccount(@NonNull final AccountIdentifier withIdentifier) {
        this.campaignBuilder.account(withIdentifier);
    }

    public void setAccount(@NonNull final Account withAccount) {
        this.setAccount(withAccount.identifier());
    }

    public CampaignCreationExecutor withAccount(@NonNull final AccountIdentifier withIdentifier) {
        this.setAccount(withIdentifier);
        return this;
    }

    public CampaignCreationExecutor withAccount(@NonNull final Account withAccount) {
        this.setAccount(withAccount);
        return this;
    }

    public void setName(@NonNull final CampaignName withName) {
        this.campaignBuilder.name(withName);
    }

    public void setName(@NonNull final String withName) {
        this.setName(new CampaignName(withName));
    }

    public CampaignCreationExecutor withName(@NonNull final String withName) {
        this.setName(withName);
        return this;
    }

    public CampaignCreationExecutor withName(@NonNull final CampaignName withName) {
        this.setName(withName);
        return this;
    }

    public Campaign get() {

        final AtomicInteger retries = new AtomicInteger(0);
        while (retries.incrementAndGet() <= 5) {
            try {
                Campaign instance = this.campaignBuilder
                        .identifier(CampaignIdentifier.random())
                        .timestampCreated(Instant.now())
                        .build();

                PutItemEnhancedRequest<Campaign> putRequest = PutItemEnhancedRequest.builder(Campaign.class)
                        .item(instance)
                        .conditionExpression(Expression.and(List.of(
                                Expression.builder()
                                        .expression("#KP <> :KP")
                                        .putExpressionName("#KP", "partition-key")
                                        .putExpressionValue(
                                                ":KP",
                                                AttributeValue.fromS(
                                                        instance.identifier().toUrn()))
                                        .build(),
                                Expression.builder()
                                        .expression("#KS <> :KS")
                                        .putExpressionName("#KS", "sort-key")
                                        .putExpressionValue(":KS", AttributeValue.fromS("details"))
                                        .build())))
                        .build();
                dynamoDbTable.putItem(putRequest);
                return instance;
            } catch (ConditionalCheckFailedException ex) {
                // do nothing
                // TODO add a limit to retries.
            }
        }

        return null;
    }

    public Campaign orElse(final Campaign withAlternativeCampaign) {
        return Optional.ofNullable(this.get()).orElse(withAlternativeCampaign);
    }
}
