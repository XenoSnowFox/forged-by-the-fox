package com.xenosnowfox.forgedbythefox.schema;

import com.xenosnowfox.forgedbythefox.models.Identifer;
import com.xenosnowfox.forgedbythefox.models.Identifiable;

public interface IdentifiableSchema<T, IDENTIFER extends Identifer<T>, MODEL extends Identifiable<IDENTIFER>>
        extends Schema<MODEL> {
    default T identifier() {
        return this.model().identifier().value();
    }
}
