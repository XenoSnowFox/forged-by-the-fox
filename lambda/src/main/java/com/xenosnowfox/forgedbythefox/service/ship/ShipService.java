package com.xenosnowfox.forgedbythefox.service.ship;

import com.xenosnowfox.forgedbythefox.models.Ability;
import com.xenosnowfox.forgedbythefox.models.ShipModule;
import com.xenosnowfox.forgedbythefox.models.ShipSheet;
import com.xenosnowfox.forgedbythefox.models.account.AccountIdentifier;
import com.xenosnowfox.forgedbythefox.models.campaign.CampaignIdentifier;
import com.xenosnowfox.forgedbythefox.models.ship.Ship;
import com.xenosnowfox.forgedbythefox.models.ship.ShipIdentifier;
import com.xenosnowfox.forgedbythefox.persistence.dynamodb.DynamodbRepository;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.NonNull;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticImmutableTableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class ShipService {

    private final DynamoDbClient client;
    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoDbTable<Ship> table;
    private final DynamoDbIndex<Ship> documentsByAccount;
    private final DynamodbRepository<Ship> shipRepository;

    public ShipService() {
        this.client = DynamoDbClient.create();
        this.enhancedClient = DynamoDbEnhancedClient.create();
        this.table = enhancedClient.table("forged-by-the-fox", ShipService.TABLE_SCHEMA);
        this.documentsByAccount = this.table.index("documents-by-account");

        this.shipRepository = new DynamodbRepository<Ship>(this.enhancedClient, ShipService.TABLE_SCHEMA);
    }

    public Ship retrieve(@NonNull final ShipIdentifier withIdentifier) {
        final Ship ship = this.shipRepository.retrieve(withIdentifier.toUrn(), "details");
        if (!ship.identifier().equals(withIdentifier)) {
            throw new IllegalStateException(
                    "Repository returned a Character instance that does not contain the requested identifier.");
        }
        return ship;
    }

    public Set<Ship> query() {
        final Set<Ship> resultSet = new HashSet<>();

        this.documentsByAccount
                .scan(b -> {
                    final Expression expression = Expression.builder()
                            .expression("begins_with(#key, :value)")
                            .putExpressionName("#key", "partition-key")
                            .putExpressionValue(":value", AttributeValue.fromS("SHIP:"))
                            .build();
                    b.filterExpression(expression);
                })
                .stream()
                .map(Page::items)
                .flatMap(Collection::stream)
                .forEach(resultSet::add);

        return resultSet;
    }

    public Stream<Ship> stream() {
        return this.query().stream();
    }

    private static final TableSchema<Ship> TABLE_SCHEMA = StaticImmutableTableSchema.builder(
                    Ship.class, Ship.Builder.class)
            .newItemBuilder(Ship::builder, Ship.Builder::build)
            .addAttribute(String.class, attributeBuilder -> attributeBuilder
                    .name("partition-key")
                    .tags(StaticAttributeTags.primaryPartitionKey())
                    .getter(instance -> instance.identifier().toUrn())
                    .setter((builder, value) -> builder.identifier(ShipIdentifier.fromUrn(value))))
            .addAttribute(String.class, attributeBuilder -> attributeBuilder
                    .name("sort-key")
                    .tags(StaticAttributeTags.primarySortKey())
                    .getter(instance -> "details")
                    .setter((builder, value) -> {}))
            // Account Identifier
            .addAttribute(String.class, attributeBuilder -> attributeBuilder
                    .name("account-identifier")
                    .tags(StaticAttributeTags.secondaryPartitionKey(List.of("documents-by-account")))
                    .getter(record -> record.accountIdentifier().toUrn())
                    .setter((builder, value) -> builder.accountIdentifier(AccountIdentifier.fromUrn(value))))
            // Campaign Identifier
            .addAttribute(String.class, attributeBuilder -> attributeBuilder
                    .name("campaign-identifier")
                    .getter(instance -> Optional.ofNullable(instance.campaignIdentifier())
                            .map(CampaignIdentifier::toUrn)
                            .orElse(null))
                    .setter((builder, value) -> builder.campaignIdentifier(CampaignIdentifier.fromUrn(value))))
            // Timestamp created
            .addAttribute(Instant.class, attributeBuilder -> attributeBuilder
                    .name("timestamp-created")
                    .getter(Ship::timestampCreated)
                    .setter(Ship.Builder::timestampCreated))
            // Name
            .addAttribute(
                    String.class,
                    attributeBuilder ->
                            attributeBuilder.name("name").getter(Ship::name).setter(Ship.Builder::name))
            // Ship Sheet
            .addAttribute(String.class, builder -> builder.name("ship-sheet")
                    .getter(record -> record.shipSheet().toString())
                    .setter((b, i) -> b.shipSheet(ShipSheet.valueOf(i))))
            // Abilities
            .addAttribute(EnhancedType.setOf(String.class), builder -> builder.name("abilities")
                    .getter(character -> Optional.of(character.abilities().stream()
                                    .map(Enum::name)
                                    .collect(Collectors.toSet()))
                            .filter(x -> !x.isEmpty())
                            .orElse(null))
                    .setter((b, data) -> b.abilities(
                            data == null
                                    ? new HashSet<>()
                                    : data.stream().map(Ability::valueOf).collect(Collectors.toSet()))))
            // Modules
            .addAttribute(EnhancedType.setOf(String.class), builder -> builder.name("modules")
                    .getter(character -> Optional.of(
                                    character.modules().stream().map(Enum::name).collect(Collectors.toSet()))
                            .filter(x -> !x.isEmpty())
                            .orElse(null))
                    .setter((b, data) -> b.modules(
                            data == null
                                    ? new HashSet<>()
                                    : data.stream().map(ShipModule::valueOf).collect(Collectors.toSet()))))
            .build();
}
