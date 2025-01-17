package com.xenosnowfox.forgedbythefox.service.character;

import com.xenosnowfox.forgedbythefox.models.HarmLevel;
import com.xenosnowfox.forgedbythefox.models.character.CharacterHarm;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticTableSchema;

@UtilityClass
public class CharacterHarmSchema {

    @Getter
    private final TableSchema<CharacterHarm> tableSchema = StaticTableSchema.builder(CharacterHarm.class)
            .newItemSupplier(CharacterHarm::new)
            .addAttribute(EnhancedType.setOf(String.class), attributeBuilder -> attributeBuilder
                    .name(HarmLevel.LESSER.name())
                    .getter(instance -> Optional.of(HarmLevel.LESSER)
                            .map(instance::stream)
                            .map(x -> x.collect(Collectors.toSet()))
                            .filter(x -> !x.isEmpty())
                            .orElse(null))
                    .setter((builder, value) -> value.forEach(i -> builder.append(HarmLevel.LESSER, i))))
            .addAttribute(EnhancedType.setOf(String.class), attributeBuilder -> attributeBuilder
                    .name(HarmLevel.MODERATE.name())
                    .getter(instance -> Optional.of(HarmLevel.MODERATE)
                            .map(instance::stream)
                            .map(x -> x.collect(Collectors.toSet()))
                            .filter(x -> !x.isEmpty())
                            .orElse(null))
                    .setter((builder, value) -> value.forEach(i -> builder.append(HarmLevel.MODERATE, i))))
            .addAttribute(EnhancedType.setOf(String.class), attributeBuilder -> attributeBuilder
                    .name(HarmLevel.SEVERE.name())
                    .getter(instance -> Optional.of(HarmLevel.SEVERE)
                            .map(instance::stream)
                            .map(x -> x.collect(Collectors.toSet()))
                            .filter(x -> !x.isEmpty())
                            .orElse(null))
                    .setter((builder, value) -> value.forEach(i -> builder.append(HarmLevel.SEVERE, i))))
            .addAttribute(EnhancedType.setOf(String.class), attributeBuilder -> attributeBuilder
                    .name(HarmLevel.FATAL.name())
                    .getter(instance -> Optional.of(HarmLevel.FATAL)
                            .map(instance::stream)
                            .map(x -> x.collect(Collectors.toSet()))
                            .filter(x -> !x.isEmpty())
                            .orElse(null))
                    .setter((builder, value) -> value.forEach(i -> builder.append(HarmLevel.FATAL, i))))
            .build();
}
