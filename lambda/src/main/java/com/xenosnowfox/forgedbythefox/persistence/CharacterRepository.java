package com.xenosnowfox.forgedbythefox.persistence;

import com.xenosnowfox.forgedbythefox.models.character.Character;
import com.xenosnowfox.forgedbythefox.models.character.CharacterIdentifier;

public interface CharacterRepository {
    Character retrieve(CharacterIdentifier withIdentifier);
}
