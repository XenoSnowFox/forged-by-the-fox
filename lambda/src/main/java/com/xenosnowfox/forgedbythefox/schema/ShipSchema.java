package com.xenosnowfox.forgedbythefox.schema;

import com.xenosnowfox.forgedbythefox.models.ShipSheet;
import com.xenosnowfox.forgedbythefox.models.ship.Ship;
import com.xenosnowfox.forgedbythefox.models.ship.ShipIdentifier;
import lombok.Builder;
import lombok.NonNull;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Builder(builderClassName = "Builder")
public record ShipSchema(@NonNull Ship model)
        implements IdentifiableSchema<String, ShipIdentifier, Ship>, NamedSchema<Ship> {
    public ShipSheet playbook() {
        return this.model().shipSheet();
    }
}
