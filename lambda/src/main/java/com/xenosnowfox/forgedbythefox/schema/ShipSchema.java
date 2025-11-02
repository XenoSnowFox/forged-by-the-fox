package com.xenosnowfox.forgedbythefox.schema;

import com.xenosnowfox.forgedbythefox.models.Ability;
import com.xenosnowfox.forgedbythefox.models.ShipModule;
import com.xenosnowfox.forgedbythefox.models.ShipModuleType;
import com.xenosnowfox.forgedbythefox.models.ShipSheet;
import com.xenosnowfox.forgedbythefox.models.ship.Ship;
import com.xenosnowfox.forgedbythefox.models.ship.ShipIdentifier;
import java.util.Collection;
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

    public Collection<Ability> abilities() {
        return this.model().abilities();
    }

    public Collection<ShipModule> modules() {
        return this.model().modules();
    }

    public Collection<ShipModule> auxiliaryModules() {
        return this.model().modules().stream()
                .filter(shipModule -> shipModule.type() == ShipModuleType.AUXILIARY)
                .toList();
    }

    public Collection<ShipModule> hullModules() {
        return this.model().modules().stream()
                .filter(shipModule -> shipModule.type() == ShipModuleType.HULL)
                .toList();
    }

    public Collection<ShipModule> engineModules() {
        return this.model().modules().stream()
                .filter(shipModule -> shipModule.type() == ShipModuleType.ENGINE)
                .toList();
    }

    public Collection<ShipModule> commsModules() {
        return this.model().modules().stream()
                .filter(shipModule -> shipModule.type() == ShipModuleType.COMMS)
                .toList();
    }

    public Collection<ShipModule> weaponModules() {
        return this.model().modules().stream()
                .filter(shipModule -> shipModule.type() == ShipModuleType.WEAPON)
                .toList();
    }
}
