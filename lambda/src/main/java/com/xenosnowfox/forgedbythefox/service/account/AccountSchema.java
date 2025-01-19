package com.xenosnowfox.forgedbythefox.service.account;

import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.account.AccountIdentifier;
import com.xenosnowfox.forgedbythefox.models.account.AccountName;
import java.time.Instant;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticImmutableTableSchema;

@UtilityClass
public class AccountSchema {

    @Getter
    private final TableSchema<Account> tableSchema = StaticImmutableTableSchema.builder(
                    Account.class, Account.Builder.class)
            .newItemBuilder(Account::builder, Account.Builder::build)
            .addAttribute(String.class, attributeBuilder -> attributeBuilder
                    .name("partition-key")
                    .tags(StaticAttributeTags.primaryPartitionKey())
                    .getter(instance -> instance.identifier().toUrn())
                    .setter((builder, value) -> builder.identifier(AccountIdentifier.fromUrn(value))))
            .addAttribute(String.class, attributeBuilder -> attributeBuilder
                    .name("sort-key")
                    .tags(StaticAttributeTags.primarySortKey())
                    .getter(instance -> "details")
                    .setter((builder, value) -> {}))
            .addAttribute(String.class, attributeBuilder -> attributeBuilder
                    .name("display-name")
                    .getter(instance -> instance.name().toString())
                    .setter((builder, value) -> builder.name(new AccountName(value))))
            .addAttribute(String.class, attributeBuilder -> attributeBuilder
                    .name("profile-picture-url")
                    .getter(Account::profilePictureUrl)
                    .setter(Account.Builder::profilePictureUrl))
            .addAttribute(Instant.class, attributeBuilder -> attributeBuilder
                    .name("timestamp-created")
                    .getter(Account::timestampCreated)
                    .setter(Account.Builder::timestampCreated))
            .build();
}
