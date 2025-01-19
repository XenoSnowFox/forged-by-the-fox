package com.xenosnowfox.forgedbythefox.service.character;

import com.xenosnowfox.forgedbythefox.models.Attribute;
import com.xenosnowfox.forgedbythefox.models.character.CharacterExperience;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticImmutableTableSchema;

@UtilityClass
public class CharacterExperienceSchema {

    @Getter
    private final TableSchema<CharacterExperience> tableSchema = StaticImmutableTableSchema.builder(
                    CharacterExperience.class, CharacterExperience.Builder.class)
            .newItemBuilder(CharacterExperience::builder, CharacterExperience.Builder::build)
            .addAttribute(int.class, attributeBuilder -> attributeBuilder
                    .name("playbook")
                    .getter(CharacterExperience::playbook)
                    .setter(CharacterExperience.Builder::playbook))
            .addAttribute(EnhancedType.mapOf(String.class, Integer.class), attributeBuilder -> attributeBuilder
                    .name("attributes")
                    .getter(record -> record.attributes().entrySet().stream()
                            .collect(Collectors.toMap(x -> x.getKey().toString(), Map.Entry::getValue)))
                    .setter((b, m) -> b.attributes(m.entrySet().stream()
                            .collect(Collectors.toMap(x -> Attribute.valueOf(x.getKey()), Map.Entry::getValue)))))
            .build();
}
