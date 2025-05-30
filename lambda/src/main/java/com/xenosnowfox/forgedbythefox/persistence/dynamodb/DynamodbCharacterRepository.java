package com.xenosnowfox.forgedbythefox.persistence.dynamodb;

import com.xenosnowfox.forgedbythefox.models.character.Character;
import com.xenosnowfox.forgedbythefox.models.character.CharacterIdentifier;
import com.xenosnowfox.forgedbythefox.persistence.CharacterRepository;
import com.xenosnowfox.forgedbythefox.service.character.CharacterSchema;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

public class DynamodbCharacterRepository extends AbstractDynamodbRepository<CharacterIdentifier, Character>
        implements CharacterRepository {
    public DynamodbCharacterRepository(final DynamoDbEnhancedClient withDynamoDbEnhancedClient) {
        super(withDynamoDbEnhancedClient, CharacterSchema.getTableSchema());
    }

    @Override
    public Character retrieve(final CharacterIdentifier withIdentifier) {
        return super.retrieve(withIdentifier.toUrn(), "CHARACTER");
    }
}
