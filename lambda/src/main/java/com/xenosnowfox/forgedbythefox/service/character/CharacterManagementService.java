package com.xenosnowfox.forgedbythefox.service.character;

import com.xenosnowfox.forgedbythefox.models.character.Character;
import com.xenosnowfox.forgedbythefox.models.character.CharacterExperience;
import com.xenosnowfox.forgedbythefox.models.character.CharacterIdentifier;
import com.xenosnowfox.forgedbythefox.persistence.CharacterRepository;
import com.xenosnowfox.forgedbythefox.persistence.dynamodb.DynamodbCharacterRepository;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import lombok.NonNull;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;

public class CharacterManagementService {

    private final DynamoDbClient client;
    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoDbTable<Character> table;
    private final DynamoDbIndex<Character> documentsByAccount;
    private final CharacterRepository characterRepository;

    public CharacterManagementService() {
        this.client = DynamoDbClient.create();
        this.enhancedClient = DynamoDbEnhancedClient.create();
        this.table = enhancedClient.table("forged-by-the-fox", CharacterSchema.getTableSchema());
        this.documentsByAccount = this.table.index("documents-by-account");

        this.characterRepository = new DynamodbCharacterRepository(this.enhancedClient);
    }

    public Character create(@NonNull final CreateCharacterRequest withRequest) {

        final AtomicInteger retries = new AtomicInteger(0);
        while (retries.incrementAndGet() <= 5) {
            try {
                Character item = Character.builder()
                        .identifier(CharacterIdentifier.random())
                        .timestampCreated(Instant.now())
                        .accountIdentifier(withRequest.accountIdentifier())
                        .playbook(withRequest.playbook())
                        .experience(CharacterExperience.builder()
                                .playbook(0)
                                .attributes(Collections.emptyMap())
                                .build())
                        .build();

                PutItemEnhancedRequest<Character> putRequest = PutItemEnhancedRequest.builder(Character.class)
                        .item(item)
                        .conditionExpression(Expression.and(List.of(
                                Expression.builder()
                                        .expression("#KP <> :KP")
                                        .putExpressionName("#KP", "partition-key")
                                        .putExpressionValue(
                                                ":KP",
                                                AttributeValue.fromS(
                                                        item.identifier().toUrn()))
                                        .build(),
                                Expression.builder()
                                        .expression("#KS <> :KS")
                                        .putExpressionName("#KS", "sort-key")
                                        .putExpressionValue(":KS", AttributeValue.fromS("CHARACTER"))
                                        .build())))
                        .build();
                table.putItem(putRequest);
                return item;
            } catch (ConditionalCheckFailedException ex) {
                // do nothing
                // TODO add a limit to retries.
            }
        }

        throw new IllegalStateException("Unable to create new character.");
    }

    public Character create(@NonNull final Consumer<CreateCharacterRequest.Builder> withMutator) {
        final CreateCharacterRequest.Builder builder = CreateCharacterRequest.builder();
        withMutator.accept(builder);
        return this.create(builder.build());
    }

    public Character retrieve(@NonNull final CharacterIdentifier withIdentifier) {
        final Character character = this.characterRepository.retrieve(withIdentifier);
        if (!character.identifier().equals(withIdentifier)) {
            throw new IllegalStateException(
                    "Repository returned a Character instance that does not contain the requested identifier.");
        }
        return character;
    }

    public Set<Character> query(@NonNull final QueryCharacterRequest withRequest) {
        final Set<Character> characterSet = new TreeSet<>();

        final Key key = Key.builder()
                .partitionValue(withRequest.accountIdentifier().toUrn())
                .sortValue("CHARACTER")
                .build();

        final QueryConditional queryConditional = QueryConditional.keyEqualTo(key);

        this.documentsByAccount
                .query(b -> {
                    b.queryConditional(queryConditional);

                    if (withRequest.playbook() != null) {
                        final Expression expression = Expression.builder()
                                .expression("playbook = :playbook")
                                .putExpressionValue(
                                        ":playbook",
                                        AttributeValue.fromS(
                                                withRequest.playbook().toString()))
                                .build();
                        b.filterExpression(expression);
                    }
                })
                .stream()
                .map(Page::items)
                .flatMap(Collection::stream)
                .forEach(characterSet::add);

        return characterSet;
    }

    public Set<Character> query(@NonNull final Consumer<QueryCharacterRequest.Builder> withMutator) {
        final QueryCharacterRequest.Builder builder = QueryCharacterRequest.builder();
        withMutator.accept(builder);
        return this.query(builder.build());
    }

    public CharacterQueryExecutor query() {
        return new CharacterQueryExecutor(this.documentsByAccount);
    }

    public CharacterMutationExecutor mutate() {
        return new CharacterMutationExecutor(this.client, this.table);
    }

    public CharacterMutationExecutor mutate(final Character withCharacter) {
        return new CharacterMutationExecutor(this.client, this.table).withIdentifier(withCharacter.identifier());
    }
}
