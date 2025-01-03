package com.xenosnowfox.forgedbythefox.service.session;

import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.models.session.SessionIdentifier;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import lombok.NonNull;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;

public class SessionManagementService {

    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoDbTable<Session> table;

    public SessionManagementService() {
        this.enhancedClient = DynamoDbEnhancedClient.create();
        this.table = enhancedClient.table("forged-by-the-fox", SessionSchema.getTableSchema());
    }

    public Optional<Session> create(@NonNull final CreateSessionRequest withRequest) {

        final AtomicInteger retries = new AtomicInteger(0);
        while (retries.incrementAndGet() <= 5) {
            try {
                Session item = Session.builder()
                        .identifier(SessionIdentifier.random())
                        .timestampCreated(Instant.now())
                        .timestampExpires(Instant.now().plus(Session.TIME_TO_LIVE))
                        .accountIdentifier(withRequest.accountIdentifier())
                        .build();

                PutItemEnhancedRequest<Session> putRequest = PutItemEnhancedRequest.builder(Session.class)
                        .item(item)
                        .conditionExpression(Expression.and(List.of(
                                Expression.builder()
                                        .expression("#KP <> :KP")
                                        .putExpressionName("#KP", "partition-key")
                                        .putExpressionValue(
                                                ":KP",
                                                AttributeValue.fromS("session:"
                                                        + item.identifier().toString()))
                                        .build(),
                                Expression.builder()
                                        .expression("#KS <> :KS")
                                        .putExpressionName("#KS", "sort-key")
                                        .putExpressionValue(":KS", AttributeValue.fromS("session"))
                                        .build())))
                        .build();
                table.putItem(putRequest);
                return Optional.of(item);
            } catch (ConditionalCheckFailedException ex) {
                // do nothing
                // TODO add a limit to retries.
            }
        }

        throw new IllegalStateException("Unable to create new session.");
    }

    public Optional<Session> create(@NonNull final Consumer<CreateSessionRequest.Builder> withMutator) {
        final CreateSessionRequest.Builder builder = CreateSessionRequest.builder();
        withMutator.accept(builder);
        return this.create(builder.build());
    }

    public Session retrieve(@NonNull final SessionIdentifier withIdentifier) {
        final Key key = Key.builder()
                .partitionValue("SESSION:" + withIdentifier.value())
                .sortValue("session")
                .build();
        return table.getItem(key);
    }
}
