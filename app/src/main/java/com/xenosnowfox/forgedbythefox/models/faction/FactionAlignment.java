package com.xenosnowfox.forgedbythefox.models.faction;

import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder")
public record FactionAlignment(@NonNull FactionAlignmentIdentifier identifier, @NonNull String name) {}
