package com.xenosnowfox.forgedbythefox.service.character;

import com.xenosnowfox.forgedbythefox.models.Ability;
import com.xenosnowfox.forgedbythefox.models.Attribute;
import com.xenosnowfox.forgedbythefox.models.Item;
import com.xenosnowfox.forgedbythefox.models.Trauma;
import com.xenosnowfox.forgedbythefox.models.character.Character;
import com.xenosnowfox.forgedbythefox.models.character.CharacterHarm;
import com.xenosnowfox.forgedbythefox.models.character.CharacterIdentifier;
import com.xenosnowfox.forgedbythefox.models.dynamodb.DynamoDbUpdateExpressionMutator;
import java.util.Optional;
import java.util.Set;
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
public class CharacterMutationExecutor {

    private final DynamoDbClient dynamoDbClient;

    private final DynamoDbTable<Character> dynamoDbTable;

    private final DynamoDbUpdateExpressionMutator mutator = new DynamoDbUpdateExpressionMutator();

    @Setter
    private CharacterIdentifier withIdentifier;

    public CharacterMutationExecutor withName(final String withName) {
        if (withName != null) {
            this.mutator.set("name", AttributeValue.fromS(withName));
        }
        return this;
    }

    public CharacterMutationExecutor withAlias(final String withAlias) {
        if (withAlias != null) {
            this.mutator.set("alias", AttributeValue.fromS(withAlias));
        }
        return this;
    }

    public CharacterMutationExecutor withStress(final Integer withStress) {
        if (withStress != null) {
            this.mutator.set("stress", AttributeValue.fromS(String.valueOf(withStress)));
        }
        return this;
    }

    public CharacterMutationExecutor withPlaybookExperience(final Integer withExperience) {
        if (withExperience != null) {
            this.mutator.set("experience.playbook", AttributeValue.fromN(String.valueOf(withExperience)));
        }
        return this;
    }

    public CharacterMutationExecutor withAttributeExperience(
            @NonNull final Attribute withAttribute, final Integer withExperience) {
        if (withExperience != null) {
            this.mutator.set(
                    "experience.attributes." + withAttribute.name(),
                    AttributeValue.fromN(String.valueOf(withExperience)));
        }
        return this;
    }

    public CharacterMutationExecutor withHarm(final CharacterHarm withHarm) {
        if (withHarm != null) {
            this.mutator.set(
                    "harm",
                    AttributeValue.fromM(CharacterHarmSchema.getTableSchema().itemToMap(withHarm, true)));
        }
        return this;
    }

    public CharacterMutationExecutor withAbilities(final Set<Ability> withAbilities) {
        if (withAbilities != null) {
            this.mutator.set(
                    "abilities",
                    AttributeValue.fromSs(withAbilities.stream().map(Enum::name).toList()));
        }
        return this;
    }

    public CharacterMutationExecutor withTrauma(final Set<Trauma> withTrauma) {
        if (withTrauma != null) {
            this.mutator.set(
                    "trauma",
                    withTrauma.isEmpty()
                            ? AttributeValue.fromNul(true)
                            : AttributeValue.fromSs(
                                    withTrauma.stream().map(Enum::name).toList()));
        }
        return this;
    }

    public CharacterMutationExecutor withItems(final Set<Item> withItems) {
        if (withItems != null) {
            this.mutator.set(
                    "items",
                    withItems.isEmpty()
                            ? AttributeValue.fromNul(true)
                            : AttributeValue.fromSs(
                                    withItems.stream().map(Enum::name).toList()));
        }
        return this;
    }

    public Character orNull() {
        return this.get();
    }

    public Character get() {

        final Key key = Key.builder()
                .partitionValue(withIdentifier.toUrn())
                .sortValue("CHARACTER")
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
