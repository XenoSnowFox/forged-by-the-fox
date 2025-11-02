package com.xenosnowfox.forgedbythefox.persistence.dynamodb;

import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.models.session.SessionIdentifier;
import com.xenosnowfox.forgedbythefox.persistence.SessionRepository;
import com.xenosnowfox.forgedbythefox.service.session.SessionSchema;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

public class DynamodbSessionRepository extends AbstractDynamodbRepository<Session> implements SessionRepository {
    public DynamodbSessionRepository(final DynamoDbEnhancedClient withDynamoDbEnhancedClient) {
        super(withDynamoDbEnhancedClient, SessionSchema.getTableSchema());
    }

    @Override
    public Session retrieve(final SessionIdentifier withIdentifier) {
        return super.retrieve(withIdentifier.toUrn(), "session");
    }
}
