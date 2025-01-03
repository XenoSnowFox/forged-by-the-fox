package com.xenosnowfox.forgedbythefox.service.character;

import com.xenosnowfox.forgedbythefox.models.Playbook;
import com.xenosnowfox.forgedbythefox.models.account.AccountIdentifier;
import com.xenosnowfox.forgedbythefox.models.character.Character;
import com.xenosnowfox.forgedbythefox.models.character.CharacterIdentifier;
import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.experimental.UtilityClass;
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
            // Timestamp created
            .addAttribute(Instant.class, attributeBuilder -> attributeBuilder
                    .name("timestamp-created")
                    .getter(Character::timestampCreated)
                    .setter(Character.Builder::timestampCreated))
            // Playbook
            .addAttribute(String.class, builder -> builder.name("playbook")
                    .getter(record -> record.playbook().toString())
                    .setter((b, i) -> b.playbook(Playbook.valueOf(i))))
            .build();
}
