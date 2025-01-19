package com.xenosnowfox.forgedbythefox.service.faction;

import com.xenosnowfox.forgedbythefox.models.campaign.CampaignIdentifier;
import com.xenosnowfox.forgedbythefox.models.faction.Faction;
import com.xenosnowfox.forgedbythefox.models.faction.FactionAlignmentIdentifier;
import com.xenosnowfox.forgedbythefox.models.faction.FactionIdentifier;
import java.util.Optional;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticImmutableTableSchema;

@UtilityClass
public class FactionSchema {

    @Getter
    private final TableSchema<Faction> tableSchema = StaticImmutableTableSchema.builder(
                    Faction.class, Faction.Builder.class)
            .newItemBuilder(Faction::builder, Faction.Builder::build)
            .addAttribute(String.class, attributeBuilder -> attributeBuilder
                    .name("partition-key")
                    .tags(StaticAttributeTags.primaryPartitionKey())
                    .getter(instance -> instance.campaignIdentifier().toUrn())
                    .setter((builder, value) -> builder.campaignIdentifier(CampaignIdentifier.fromUrn(value))))
            .addAttribute(String.class, attributeBuilder -> attributeBuilder
                    .name("sort-key")
                    .tags(StaticAttributeTags.primarySortKey())
                    .getter(instance -> instance.identifier().toUrn())
                    .setter((builder, value) -> builder.identifier(FactionIdentifier.fromUrn(value))))
            .addAttribute(
                    String.class,
                    attributeBuilder ->
                            attributeBuilder.name("name").getter(Faction::name).setter(Faction.Builder::name))
            .addAttribute(
                    Integer.class,
                    attributeBuilder ->
                            attributeBuilder.name("tier").getter(Faction::tier).setter(Faction.Builder::tier))
            .addAttribute(Integer.class, attributeBuilder -> attributeBuilder
                    .name("status")
                    .getter(Faction::status)
                    .setter(Faction.Builder::status))
            .addAttribute(String.class, attributeBuilder -> attributeBuilder
                    .name("alignment")
                    .getter(instance -> Optional.of(instance)
                            .map(Faction::alignment)
                            .map(FactionAlignmentIdentifier::toUrn)
                            .orElse(null))
                    .setter((builder, value) -> builder.alignment(FactionAlignmentIdentifier.fromUrn(value))))
            .build();
}
