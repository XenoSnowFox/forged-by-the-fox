package com.xenosnowfox.forgedbythefox.services.sessionmanagement;

import com.xenosnowfox.forgedbythefox.library.models.account.AccountIdentifier;
import com.xenosnowfox.forgedbythefox.library.models.session.Session;
import com.xenosnowfox.forgedbythefox.library.models.session.SessionIdentifier;
import java.time.Instant;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.ImmutableAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticImmutableTableSchema;

@UtilityClass
public class SessionSchema {

    @Getter
    private final TableSchema<Session> tableSchema = StaticImmutableTableSchema.builder(
                    Session.class, Session.Builder.class)
            .newItemBuilder(Session::builder, Session.Builder::build)
            .addAttribute(String.class, SessionSchema::partitionMutator)
            .addAttribute(String.class, SessionSchema::sortKeyMutator)
            .addAttribute(String.class, SessionSchema::accountIdentifierMutator)
            .addAttribute(Instant.class, SessionSchema::timestampCreatedMutator)
            .addAttribute(Instant.class, SessionSchema::timestampExpiresMutator)
            .build();

    void partitionMutator(ImmutableAttribute.Builder<Session, Session.Builder, String> attributeBuilder) {
        attributeBuilder
                .name("partition-key")
                .tags(StaticAttributeTags.primaryPartitionKey())
                .getter(session -> "SESSION:" + session.identifier().toString())
                .setter((builder, value) -> builder.identifier(new SessionIdentifier(value.split(":")[1])));
    }

    void sortKeyMutator(ImmutableAttribute.Builder<Session, Session.Builder, String> attributeBuilder) {
        attributeBuilder
                .name("sort-key")
                .tags(StaticAttributeTags.primarySortKey())
                .getter(unused -> "session")
                .setter((builder, unused) -> {});
    }

    void accountIdentifierMutator(ImmutableAttribute.Builder<Session, Session.Builder, String> attributeBuilder) {
        attributeBuilder
                .name("account-identifier")
                .getter(identity -> identity.accountIdentifier().toString())
                .setter((builder, value) -> builder.accountIdentifier(new AccountIdentifier(value)));
    }

    void timestampCreatedMutator(ImmutableAttribute.Builder<Session, Session.Builder, Instant> attributeBuilder) {
        attributeBuilder
                .name("timestamp-created")
                .getter(Session::timestampCreated)
                .setter(Session.Builder::timestampCreated);
    }

    void timestampExpiresMutator(ImmutableAttribute.Builder<Session, Session.Builder, Instant> attributeBuilder) {
        attributeBuilder.name("ttl").getter(Session::timestampExpires).setter(Session.Builder::timestampExpires);
    }
}
