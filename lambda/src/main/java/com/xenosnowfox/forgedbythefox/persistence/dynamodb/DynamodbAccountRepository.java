package com.xenosnowfox.forgedbythefox.persistence.dynamodb;

import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.account.AccountIdentifier;
import com.xenosnowfox.forgedbythefox.persistence.AccountRepository;
import com.xenosnowfox.forgedbythefox.service.account.AccountSchema;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

public class DynamodbAccountRepository extends AbstractDynamodbRepository<Account> implements AccountRepository {

    public DynamodbAccountRepository(final DynamoDbEnhancedClient withDynamoDbEnhancedClient) {
        super(withDynamoDbEnhancedClient, AccountSchema.getTableSchema());
    }

    @Override
    public Account retrieve(final AccountIdentifier withIdentifier) {
        return super.retrieve("ACCOUNT:" + withIdentifier.value(), "details");
    }
}
