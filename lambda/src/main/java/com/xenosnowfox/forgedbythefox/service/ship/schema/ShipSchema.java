package com.xenosnowfox.forgedbythefox.service.ship.schema;

import com.xenosnowfox.forgedbythefox.models.Playbook;
import com.xenosnowfox.forgedbythefox.models.ShipSheet;
import com.xenosnowfox.forgedbythefox.models.account.AccountIdentifier;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.xenosnowfox.forgedbythefox.models.campaign.CampaignIdentifier;
import com.xenosnowfox.forgedbythefox.models.ship.Ship;
import com.xenosnowfox.forgedbythefox.models.ship.ShipIdentifier;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticImmutableTableSchema;

@UtilityClass
public class ShipSchema {

    @Getter
    private final TableSchema<Ship> tableSchema = StaticImmutableTableSchema.builder(
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
            .addAttribute(String.class, attributeBuilder -> attributeBuilder
                    .name("name")
                    .getter(Ship::name)
                    .setter(Ship.Builder::name))
            // Ship Sheet
            .addAttribute(String.class, builder -> builder.name("ship-sheet")
                    .getter(record -> record.shipSheet().toString())
                    .setter((b, i) -> b.shipSheet(ShipSheet.valueOf(i))))
            .build();
}
