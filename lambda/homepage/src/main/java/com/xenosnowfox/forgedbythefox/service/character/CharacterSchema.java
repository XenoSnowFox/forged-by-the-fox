package com.xenosnowfox.forgedbythefox.service.character;

import com.xenosnowfox.forgedbythefox.models.Ability;
import com.xenosnowfox.forgedbythefox.models.Action;
import com.xenosnowfox.forgedbythefox.models.Playbook;
import com.xenosnowfox.forgedbythefox.models.account.AccountIdentifier;
import com.xenosnowfox.forgedbythefox.models.campaign.CampaignIdentifier;
import com.xenosnowfox.forgedbythefox.models.character.Character;
import com.xenosnowfox.forgedbythefox.models.character.CharacterExperience;
import com.xenosnowfox.forgedbythefox.models.character.CharacterIdentifier;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticImmutableTableSchema;

@UtilityClass
public class CharacterSchema {

    @Getter
    private final TableSchema<Character> tableSchema = StaticImmutableTableSchema.builder(
                    Character.class, Character.Builder.class)
            .newItemBuilder(Character::builder, Character.Builder::build)
            // Partition Key
            .addAttribute(String.class, attributeBuilder -> attributeBuilder
                    .name("partition-key")
                    .tags(StaticAttributeTags.primaryPartitionKey())
                    .getter(record -> record.identifier().toUrn())
                    .setter((builder, attributeValue) ->
                            builder.identifier(CharacterIdentifier.fromUrn(attributeValue))))
            // Sort Key
            .addAttribute(String.class, attributeBuilder -> attributeBuilder
                    .name("sort-key")
                    .tags(
                            StaticAttributeTags.primarySortKey(),
                            StaticAttributeTags.secondarySortKey(List.of("documents-by-account")))
                    .getter(unused -> "CHARACTER")
                    .setter((builder, unused) -> {}))
            // Account Identifier
            .addAttribute(String.class, attributeBuilder -> attributeBuilder
                    .name("account-identifier")
                    .tags(StaticAttributeTags.secondaryPartitionKey(List.of("documents-by-account")))
                    .getter(record -> record.accountIdentifier().toUrn())
                    .setter((builder, value) -> builder.accountIdentifier(AccountIdentifier.fromUrn(value))))
            .addAttribute(String.class, attributeBuilder -> attributeBuilder
                    .name("campaign-identifier")
                    .getter(instance -> Optional.ofNullable(instance.campaignIdentifier())
                            .map(CampaignIdentifier::toUrn)
                            .orElse(null))
                    .setter((builder, value) -> builder.campaignIdentifier(CampaignIdentifier.fromUrn(value))))
            // Name
            .addAttribute(String.class, attributeBuilder -> attributeBuilder
                    .name("name")
                    .getter(Character::name)
                    .setter(Character.Builder::name))
            // Alias
            .addAttribute(String.class, attributeBuilder -> attributeBuilder
                    .name("alias")
                    .getter(Character::alias)
                    .setter(Character.Builder::alias))
            // Timestamp created
            .addAttribute(Instant.class, attributeBuilder -> attributeBuilder
                    .name("timestamp-created")
                    .getter(Character::timestampCreated)
                    .setter(Character.Builder::timestampCreated))
            // Playbook
            .addAttribute(String.class, builder -> builder.name("playbook")
                    .getter(record -> record.playbook().toString())
                    .setter((b, i) -> b.playbook(Playbook.valueOf(i))))
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
            // Experience
            .addAttribute(
                    EnhancedType.documentOf(CharacterExperience.class, CharacterExperienceSchema.getTableSchema()),
                    builder -> builder.name("experience")
                            .getter(Character::experience)
                            .setter(Character.Builder::experience))
            .addAttribute(EnhancedType.mapOf(String.class, Integer.class), attributeBuilder -> attributeBuilder
                    .name("action-dots")
                    .getter(record -> record.actionDots().entrySet().stream()
                            .collect(Collectors.toMap(x -> x.getKey().toString(), Map.Entry::getValue)))
                    .setter((b, m) -> b.actionDots(m.entrySet().stream()
                            .collect(Collectors.toMap(x -> Action.valueOf(x.getKey()), Map.Entry::getValue)))))
            .build();
}
