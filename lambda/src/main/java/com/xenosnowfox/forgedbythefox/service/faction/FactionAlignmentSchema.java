package com.xenosnowfox.forgedbythefox.service.faction;

import com.xenosnowfox.forgedbythefox.models.faction.FactionAlignment;
import com.xenosnowfox.forgedbythefox.models.faction.FactionAlignmentIdentifier;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticImmutableTableSchema;

@UtilityClass
public class FactionAlignmentSchema {

    @Getter
    private final TableSchema<FactionAlignment> tableSchema = StaticImmutableTableSchema.builder(
                    FactionAlignment.class, FactionAlignment.Builder.class)
            .newItemBuilder(FactionAlignment::builder, FactionAlignment.Builder::build)
            // Identifier
            .addAttribute(String.class, attributeBuilder -> attributeBuilder
                    .name("identifier")
                    .getter(record -> record.identifier().value())
                    .setter((builder, attributeValue) ->
                            builder.identifier(new FactionAlignmentIdentifier(attributeValue))))
            // Name
            .addAttribute(String.class, attributeBuilder -> attributeBuilder
                    .name("name")
                    .getter(FactionAlignment::name)
                    .setter(FactionAlignment.Builder::name))
            .build();
}
