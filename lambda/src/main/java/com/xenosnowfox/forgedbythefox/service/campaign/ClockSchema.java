package com.xenosnowfox.forgedbythefox.service.campaign;

import com.xenosnowfox.forgedbythefox.models.clock.Clock;
import com.xenosnowfox.forgedbythefox.models.clock.ClockIdentifier;
import com.xenosnowfox.forgedbythefox.models.clock.ClockName;
import java.time.Instant;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticImmutableTableSchema;

@UtilityClass
public class ClockSchema {

    @Getter
    private final TableSchema<Clock> tableSchema = StaticImmutableTableSchema.builder(Clock.class, Clock.Builder.class)
            .newItemBuilder(Clock::builder, Clock.Builder::build)
            .addAttribute(String.class, attributeBuilder -> attributeBuilder
                    .name("identifier")
                    .getter(instance -> instance.identifier().toUrn())
                    .setter((builder, value) -> builder.identifier(ClockIdentifier.fromUrn(value))))
            .addAttribute(String.class, attributeBuilder -> attributeBuilder
                    .name("name")
                    .getter(instance -> instance.name().toString())
                    .setter((builder, value) -> builder.name(new ClockName(value))))
            .addAttribute(Instant.class, attributeBuilder -> attributeBuilder
                    .name("timestamp-created")
                    .getter(Clock::timestampCreated)
                    .setter(Clock.Builder::timestampCreated))
            .addAttribute(Integer.class, attributeBuilder -> attributeBuilder
                    .name("marked")
                    .getter(Clock::markedSegments)
                    .setter(Clock.Builder::markedSegments))
            .addAttribute(Integer.class, attributeBuilder -> attributeBuilder
                    .name("total")
                    .getter(Clock::totalSegments)
                    .setter(Clock.Builder::totalSegments))
            .build();
}
