package com.xenosnowfox.forgedbythefox.services.identitymanagement;

import com.xenosnowfox.forgedbythefox.library.models.account.AccountIdentifier;
import com.xenosnowfox.forgedbythefox.library.models.identity.Identity;
import com.xenosnowfox.forgedbythefox.library.models.identity.IdentityIdentifier;
import com.xenosnowfox.forgedbythefox.library.models.identity.IdentityProvider;
import java.time.Instant;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.ImmutableAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticImmutableTableSchema;

@UtilityClass
public class IdentitySchema {

    @Getter
    private final TableSchema<Identity> tableSchema = StaticImmutableTableSchema.builder(
                    Identity.class, Identity.Builder.class)
            .newItemBuilder(Identity::builder, Identity.Builder::build)
            .addAttribute(String.class, IdentitySchema::partitionMutator)
            .addAttribute(String.class, IdentitySchema::sortKeyMutator)
            .addAttribute(String.class, IdentitySchema::givenNameMutator)
            .addAttribute(String.class, IdentitySchema::familyNameMutator)
            .addAttribute(String.class, IdentitySchema::profilePictureUrlMutator)
            .addAttribute(String.class, IdentitySchema::accountIdentifierMutator)
            .addAttribute(Instant.class, IdentitySchema::timestampCreatedMutator)
            .build();

    void partitionMutator(ImmutableAttribute.Builder<Identity, Identity.Builder, String> attributeBuilder) {
        attributeBuilder
                .name("partition-key")
                .tags(StaticAttributeTags.primaryPartitionKey())
                .getter(identity -> identity.identifier().toString())
                .setter((builder, identity) -> builder.identifier(IdentityIdentifier.builder()
                        .provider(IdentityProvider.GOOGLE)
                        .value(identity.split(":")[1])
                        .build()));
    }

    void sortKeyMutator(ImmutableAttribute.Builder<Identity, Identity.Builder, String> attributeBuilder) {
        attributeBuilder
                .name("sort-key")
                .tags(StaticAttributeTags.primarySortKey())
                .getter(unused -> "identity")
                .setter((builder, unused) -> {});
    }

    void givenNameMutator(ImmutableAttribute.Builder<Identity, Identity.Builder, String> attributeBuilder) {
        attributeBuilder.name("given-name").getter(Identity::givenName).setter(Identity.Builder::givenName);
    }

    void familyNameMutator(ImmutableAttribute.Builder<Identity, Identity.Builder, String> attributeBuilder) {
        attributeBuilder.name("family-name").getter(Identity::familyName).setter(Identity.Builder::familyName);
    }

    void profilePictureUrlMutator(ImmutableAttribute.Builder<Identity, Identity.Builder, String> attributeBuilder) {
        attributeBuilder
                .name("profile-picture-url")
                .getter(Identity::profilePictureUrl)
                .setter(Identity.Builder::profilePictureUrl);
    }

    void accountIdentifierMutator(ImmutableAttribute.Builder<Identity, Identity.Builder, String> attributeBuilder) {
        attributeBuilder
                .name("account-identifier")
                .getter(identity -> identity.accountIdentifier().toString())
                .setter((builder, value) -> builder.accountIdentifier(new AccountIdentifier(value)));
    }

    void timestampCreatedMutator(ImmutableAttribute.Builder<Identity, Identity.Builder, Instant> attributeBuilder) {
        attributeBuilder
                .name("timestamp-created")
                .getter(Identity::timestampCreated)
                .setter(Identity.Builder::timestampCreated);
    }
}
