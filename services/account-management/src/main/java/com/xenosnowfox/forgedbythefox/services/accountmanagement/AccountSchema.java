package com.xenosnowfox.forgedbythefox.services.accountmanagement;

import com.xenosnowfox.forgedbythefox.library.models.account.Account;
import com.xenosnowfox.forgedbythefox.library.models.account.AccountIdentifier;
import com.xenosnowfox.forgedbythefox.library.models.account.AccountName;
import java.time.Instant;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.ImmutableAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticImmutableTableSchema;

@UtilityClass
public class AccountSchema {

    @Getter
    private final TableSchema<Account> tableSchema = StaticImmutableTableSchema.builder(
                    Account.class, Account.Builder.class)
            .newItemBuilder(Account::builder, Account.Builder::build)
            .addAttribute(String.class, AccountSchema::partitionMutator)
            .addAttribute(String.class, AccountSchema::sortKeyMutator)
            .addAttribute(String.class, AccountSchema::accountNameMutator)
            .addAttribute(String.class, AccountSchema::profilePictureUrlMutator)
            .addAttribute(Instant.class, AccountSchema::timestampCreatedMutator)
            .build();

    void partitionMutator(ImmutableAttribute.Builder<Account, Account.Builder, String> attributeBuilder) {
        attributeBuilder
                .name("partition-key")
                .tags(StaticAttributeTags.primaryPartitionKey())
                .getter(identity -> "account:" + identity.identifier().toString())
                .setter((builder, identity) -> builder.identifier(AccountIdentifier.builder()
                        .value(identity.split("account:")[1])
                        .build()));
    }

    void sortKeyMutator(ImmutableAttribute.Builder<Account, Account.Builder, String> attributeBuilder) {
        attributeBuilder
                .name("sort-key")
                .tags(StaticAttributeTags.primarySortKey())
                .getter(unused -> "details")
                .setter((builder, unused) -> {});
    }

    void accountNameMutator(ImmutableAttribute.Builder<Account, Account.Builder, String> attributeBuilder) {
        attributeBuilder
                .name("display-name")
                .getter(account -> account.name().toString())
                .setter((builder, value) -> builder.name(new AccountName(value)));
    }

    void profilePictureUrlMutator(ImmutableAttribute.Builder<Account, Account.Builder, String> attributeBuilder) {
        attributeBuilder
                .name("profile-picture-url")
                .getter(Account::profilePictureUrl)
                .setter(Account.Builder::profilePictureUrl);
    }

    void timestampCreatedMutator(ImmutableAttribute.Builder<Account, Account.Builder, Instant> attributeBuilder) {
        attributeBuilder
                .name("timestamp-created")
                .getter(Account::timestampCreated)
                .setter(Account.Builder::timestampCreated);
    }
}
