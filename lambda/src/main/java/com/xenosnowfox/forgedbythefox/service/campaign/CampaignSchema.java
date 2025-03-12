package com.xenosnowfox.forgedbythefox.service.campaign;

import com.xenosnowfox.forgedbythefox.models.account.AccountIdentifier;
import com.xenosnowfox.forgedbythefox.models.campaign.Campaign;
import com.xenosnowfox.forgedbythefox.models.campaign.CampaignIdentifier;
import com.xenosnowfox.forgedbythefox.models.campaign.CampaignName;
import com.xenosnowfox.forgedbythefox.models.clock.Clock;
import com.xenosnowfox.forgedbythefox.models.faction.FactionAlignment;
import com.xenosnowfox.forgedbythefox.service.faction.FactionAlignmentSchema;
import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticImmutableTableSchema;

@UtilityClass
public class CampaignSchema {

    @Getter
    private final TableSchema<Campaign> tableSchema = StaticImmutableTableSchema.builder(
                    Campaign.class, Campaign.Builder.class)
            .newItemBuilder(Campaign::builder, Campaign.Builder::build)
            .addAttribute(String.class, attributeBuilder -> attributeBuilder
                    .name("partition-key")
                    .tags(StaticAttributeTags.primaryPartitionKey())
                    .getter(instance -> instance.identifier().toUrn())
                    .setter((builder, value) -> builder.identifier(CampaignIdentifier.fromUrn(value))))
            .addAttribute(String.class, attributeBuilder -> attributeBuilder
                    .name("sort-key")
                    .tags(
                            StaticAttributeTags.primarySortKey(),
                            StaticAttributeTags.secondarySortKey(List.of("documents-by-account")))
                    .getter(instance -> "details")
                    .setter((builder, unused) -> {}))
            .addAttribute(String.class, attributeBuilder -> attributeBuilder
                    .name("name")
                    .getter(instance -> instance.name().toString())
                    .setter((builder, value) -> builder.name(new CampaignName(value))))
            .addAttribute(Instant.class, attributeBuilder -> attributeBuilder
                    .name("timestamp-created")
                    .getter(Campaign::timestampCreated)
                    .setter(Campaign.Builder::timestampCreated)) // Account Identifier
            .addAttribute(String.class, attributeBuilder -> attributeBuilder
                    .name("account-identifier")
                    .tags(StaticAttributeTags.secondaryPartitionKey(List.of("documents-by-account")))
                    .getter(instance -> instance.account().toUrn())
                    .setter((builder, value) -> builder.account(AccountIdentifier.fromUrn(value))))
            .addAttribute(
                    EnhancedType.listOf(
                            EnhancedType.documentOf(FactionAlignment.class, FactionAlignmentSchema.getTableSchema())),
                    attributeBuilder -> attributeBuilder
                            .name("faction-alignments")
                            .getter(Campaign::factionAlignments)
                            .setter(Campaign.Builder::factionAlignments))
            .addAttribute(
                    EnhancedType.listOf(EnhancedType.documentOf(Clock.class, ClockSchema.getTableSchema())),
                    attributeBuilder -> attributeBuilder
                            .name("clocks")
                            .getter(Campaign::clocks)
                            .setter(Campaign.Builder::clocks))
            .build();
}
