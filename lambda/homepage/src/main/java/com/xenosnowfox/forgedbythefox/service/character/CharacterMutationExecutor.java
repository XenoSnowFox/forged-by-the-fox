package com.xenosnowfox.forgedbythefox.service.character;

import com.xenosnowfox.forgedbythefox.models.Ability;
import com.xenosnowfox.forgedbythefox.models.Trauma;
import com.xenosnowfox.forgedbythefox.models.character.Character;
import com.xenosnowfox.forgedbythefox.models.character.CharacterHarm;
import com.xenosnowfox.forgedbythefox.models.character.CharacterIdentifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
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
public class CharacterMutationExecutor {

    private final DynamoDbClient dynamoDbClient;

    private final DynamoDbTable<Character> dynamoDbTable;

    @Setter
    private CharacterIdentifier withIdentifier;

    @Setter
    private String withName;

    @Setter
    private String withAlias;

    @Setter
    private Integer withStress;

    @Setter
    private Set<Trauma> withTrauma;

    @Setter
    private Set<Ability> withAbilities;

    @Setter
    private CharacterHarm withHarm;

    public Character orNull() {
        return this.get();
    }

    public Character get() {

        final Key key = Key.builder()
                .partitionValue(withIdentifier.toUrn())
                .sortValue("CHARACTER")
                .build();

        final Set<String> updateExpressionParts = new HashSet<>();
        final Map<String, String> attributeKeyMap = new HashMap<>();
        final Map<String, AttributeValue> attributeValueMap = new HashMap<>();

        final BiConsumer<String, AttributeValue> append = (k, v) -> {
            updateExpressionParts.add("#k" + attributeKeyMap.size() + " = :v" + attributeValueMap.size());
            attributeKeyMap.put("#k" + attributeKeyMap.size(), k);
            attributeValueMap.put(":v" + attributeValueMap.size(), v);
        };

        if (this.withName != null && !this.withName.isBlank()) {
            append.accept("name", AttributeValue.fromS(withName.trim()));
        }

        if (this.withAlias != null && !this.withAlias.isBlank()) {
            append.accept("alias", AttributeValue.fromS(withAlias.trim()));
        }

        if (this.withAbilities != null) {
            append.accept(
                    "abilities",
                    AttributeValue.fromSs(
                            this.withAbilities.stream().map(Enum::name).toList()));
        }

        if (this.withHarm != null) {
            append.accept(
                    "harm",
                    AttributeValue.fromM(CharacterHarmSchema.getTableSchema().itemToMap(this.withHarm, true)));
        }

        if (this.withStress != null) {
            append.accept("stress", AttributeValue.fromN(String.valueOf(this.withStress)));
        }

        if (this.withTrauma != null) {
            append.accept(
                    "trauma",
                    this.withTrauma.isEmpty()
                            ? AttributeValue.fromNul(true)
                            : AttributeValue.fromSs(
                                    this.withTrauma.stream().map(Enum::name).toList()));
        }

        if (updateExpressionParts.isEmpty()) {
            return dynamoDbTable.getItem(key);
        }

        final UpdateItemRequest updateRequest = UpdateItemRequest.builder()
                .tableName(dynamoDbTable.tableName())
                .key(key.primaryKeyMap(dynamoDbTable.tableSchema()))
                .updateExpression("SET " + String.join(", ", updateExpressionParts))
                .expressionAttributeNames(attributeKeyMap)
                .expressionAttributeValues(attributeValueMap)
                .returnValues(ReturnValue.ALL_NEW)
                .build();

        final UpdateItemResponse updateResponse = this.dynamoDbClient.updateItem(updateRequest);

        return Optional.of(updateResponse)
                .map(UpdateItemResponse::attributes)
                .map(this.dynamoDbTable.tableSchema()::mapToItem)
                .orElse(null);
    }
}
