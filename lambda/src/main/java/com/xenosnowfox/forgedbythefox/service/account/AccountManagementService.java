package com.xenosnowfox.forgedbythefox.service.account;

import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.account.AccountIdentifier;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import com.xenosnowfox.forgedbythefox.persistence.AccountRepository;
import com.xenosnowfox.forgedbythefox.persistence.dynamodb.DynamodbAccountRepository;
import lombok.NonNull;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;

public class AccountManagementService {

    private final DynamoDbEnhancedClient enhancedClient;

    private final DynamoDbTable<Account> table;

    private final AccountRepository accountRepository;

    public AccountManagementService() {

        this.enhancedClient = DynamoDbEnhancedClient.create();
        this.table = enhancedClient.table("forged-by-the-fox", AccountSchema.getTableSchema());

        this.accountRepository = new DynamodbAccountRepository(this.enhancedClient);
    }

    public Optional<Account> create(@NonNull final CreateAccountRequest withRequest) {

        final AtomicInteger retries = new AtomicInteger(0);
        while (retries.incrementAndGet() <= 5) {
            try {
                Account account = Account.builder()
                        .identifier(AccountIdentifier.random())
                        .timestampCreated(Instant.now())
                        .name(withRequest.name())
                        .profilePictureUrl(withRequest.profilePictureUrl())
                        .build();

                PutItemEnhancedRequest<Account> putRequest = PutItemEnhancedRequest.builder(Account.class)
                        .item(account)
                        .conditionExpression(Expression.and(List.of(
                                Expression.builder()
                                        .expression("#KP <> :KP")
                                        .putExpressionName("#KP", "partition-key")
                                        .putExpressionValue(
                                                ":KP", AttributeValue.fromS("ACCOUNT:" + account.identifier()))
                                        .build(),
                                Expression.builder()
                                        .expression("#KS <> :KS")
                                        .putExpressionName("#KS", "sort-key")
                                        .putExpressionValue(":KS", AttributeValue.fromS("details"))
                                        .build())))
                        .build();
                table.putItem(putRequest);
                return Optional.of(account);
            } catch (ConditionalCheckFailedException ex) {
                // do nothing
                // TODO add a limit to retries.
            }
        }

        throw new IllegalStateException("Unable to create new account.");
    }

    public Optional<Account> create(@NonNull final Consumer<CreateAccountRequest.Builder> withMutator) {
        final CreateAccountRequest.Builder builder = CreateAccountRequest.builder();
        withMutator.accept(builder);
        return this.create(builder.build());
    }

    public Account retrieve(@NonNull final AccountIdentifier withIdentifier) {
        return this.accountRepository.retrieve(withIdentifier);
    }
}
