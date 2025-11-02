package com.xenosnowfox.forgedbythefox.schema;

import com.xenosnowfox.forgedbythefox.models.Nameable;

public interface NamedSchema<MODEL extends Nameable> extends Schema<MODEL> {
    default String name() {
        return this.model().name();
    }
}
