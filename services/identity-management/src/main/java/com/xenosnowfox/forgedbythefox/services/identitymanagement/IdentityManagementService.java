package com.xenosnowfox.forgedbythefox.services.identitymanagement;

import com.xenosnowfox.forgedbythefox.library.models.identity.Identity;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.Builder;
import lombok.NonNull;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;

@Builder(builderClassName = "Builder")
public record IdentityManagementService() {

    public Optional<Identity> create(@NonNull final CreateIdentityRequest withRequest) {

        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.create();
        DynamoDbTable<Identity> table = enhancedClient.table("forged-by-the-fox", IdentitySchema.getTableSchema());

        final AtomicInteger retries = new AtomicInteger(0);
        while (retries.incrementAndGet() <= 5) {
            try {
                Identity item = Identity.builder()
                        .identifier(withRequest.identifier())
                        .timestampCreated(Instant.now())
                        .givenName(withRequest.givenName())
                        .familyName(withRequest.familyName())
                        .profilePictureUrl(withRequest.profilePictureUrl())
                        .accountIdentifier(withRequest.accountIdentifier())
                        .build();

                PutItemEnhancedRequest<Identity> putRequest = PutItemEnhancedRequest.builder(Identity.class)
                        .item(item)
                        .conditionExpression(Expression.and(List.of(
                                Expression.builder()
                                        .expression("#KP <> :KP")
                                        .putExpressionName("#KP", "partition-key")
                                        .putExpressionValue(
                                                ":KP",
                                                AttributeValue.fromS(
                                                        item.identifier().toString()))
                                        .build(),
                                Expression.builder()
                                        .expression("#KS <> :KS")
                                        .putExpressionName("#KS", "sort-key")
                                        .putExpressionValue(":KS", AttributeValue.fromS("identity"))
                                        .build())))
                        .build();
                table.putItem(putRequest);
                return Optional.of(item);
            } catch (ConditionalCheckFailedException ex) {
                // do nothing
                // TODO add a limit to retries.
            }
        }

        throw new IllegalStateException("Unable to create new identity.");
    }

    public Optional<Identity> create(@NonNull final Consumer<CreateIdentityRequest.Builder> withMutator) {
        final CreateIdentityRequest.Builder builder = CreateIdentityRequest.builder();
        withMutator.accept(builder);
        return this.create(builder.build());
    }

    public Optional<Identity> update(@NonNull final UpdateIdentityRequest withRequest) {

        final Set<String> conditionExpression = new HashSet<>();

        final Set<String> updateExpressionParts = new HashSet<>();

        final Map<String, String> expressionAttributeNames = new HashMap<>();

        final Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();

        final Function<String, Consumer<AttributeValue>> appendToUpdateExpression = dbFieldName -> attributeValue -> {
            final String key = "#k" + expressionAttributeNames.size();
            final String value = ":v" + expressionAttributeValues.size();
            updateExpressionParts.add(key + " = " + value);
            expressionAttributeNames.put(key, dbFieldName);
            expressionAttributeValues.put(value, attributeValue);
        };

        conditionExpression.add("#CPK = :CKP");
        expressionAttributeNames.put("#CPK", "partition-key");
        expressionAttributeValues.put(
                ":CKP", AttributeValue.fromS(withRequest.identifier().toString()));

        conditionExpression.add("#CSK = :CSK");
        expressionAttributeNames.put("#CSK", "sort-key");
        expressionAttributeValues.put(":CSK", AttributeValue.fromS("identity"));

        withRequest.optGivenName().map(AttributeValue::fromS).ifPresent(appendToUpdateExpression.apply("given-name"));

        withRequest.optFamilyName().map(AttributeValue::fromS).ifPresent(appendToUpdateExpression.apply("family-name"));

        withRequest
                .optProfilePictureUrl()
                .map(AttributeValue::fromS)
                .ifPresent(appendToUpdateExpression.apply("profile-picture-url"));

        Map<String, AttributeValue> dbKey = Map.of(
                "partition-key",
                AttributeValue.fromS(withRequest.identifier().toString()),
                "sort-key",
                AttributeValue.fromS("identity"));

        // check if not performing any updates and attempt a query
        if (updateExpressionParts.isEmpty()) {
            try (DynamoDbClient ddb = DynamoDbClient.create()) {
                final GetItemResponse response = ddb.getItem(b -> b.key(dbKey).tableName("forged-by-the-fox"));
                final Identity identity = IdentitySchema.getTableSchema().mapToItem(response.item());
                return Optional.of(identity);
            } catch (ResourceNotFoundException e) {
                return Optional.empty();
            } catch (DynamoDbException e) {
                throw new RuntimeException(e);
            }
        }

        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName("forged-by-the-fox")
                .key(dbKey)
                .updateExpression("SET " + String.join(", ", updateExpressionParts))
                .expressionAttributeNames(expressionAttributeNames)
                .expressionAttributeValues(expressionAttributeValues)
                .conditionExpression(String.join(" AND ", conditionExpression))
                .returnValues(ReturnValue.ALL_NEW)
                .build();

        try (DynamoDbClient ddb = DynamoDbClient.create()) {
            final UpdateItemResponse response = ddb.updateItem(request);

            final Identity identity = IdentitySchema.getTableSchema().mapToItem(response.attributes());

            return Optional.of(identity);
        } catch (ResourceNotFoundException | ConditionalCheckFailedException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();

            return Optional.empty();
        } catch (DynamoDbException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Identity> update(@NonNull final Consumer<UpdateIdentityRequest.Builder> withMutator) {
        final UpdateIdentityRequest.Builder builder = UpdateIdentityRequest.builder();
        withMutator.accept(builder);
        return this.update(builder.build());
    }
}
