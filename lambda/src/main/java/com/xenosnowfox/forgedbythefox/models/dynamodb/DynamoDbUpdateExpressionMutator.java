package com.xenosnowfox.forgedbythefox.models.dynamodb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.ToString;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

@ToString
public class DynamoDbUpdateExpressionMutator implements Consumer<UpdateItemRequest.Builder> {

    private Map<UpdateExpressionClause, List<String>> updateExpression = new HashMap<>();

    private Map<String, String> attributeNames = new HashMap<>();

    private Map<String, AttributeValue> attributeValues = new HashMap<>();

    public String getUpdateExpressionString() {
        return updateExpression.entrySet().stream()
                .map(entry -> Map.entry(
                        entry.getKey().name(),
                        entry.getValue().stream()
                                .filter(Objects::nonNull)
                                .filter(str -> !str.isBlank())
                                .collect(Collectors.joining(", "))
                                .trim()))
                .filter(entry -> !entry.getValue().isBlank())
                .map(entry -> entry.getKey() + " " + entry.getValue())
                .collect(Collectors.joining(" "))
                .trim();
    }

    private String parseKey(final String withKey) {
        return Arrays.stream(withKey.split("\\."))
                .map(part -> this.attributeNames.entrySet().stream()
                        .filter(entry -> entry.getValue().equals(part))
                        .findFirst()
                        .map(Map.Entry::getKey)
                        .orElseGet(() -> {
                            final String k = "#_" + this.attributeNames.size();
                            this.attributeNames.put(k, part);
                            return k;
                        }))
                .collect(Collectors.joining("."));
    }

    private String parseAttributeValue(final AttributeValue withAttributeValue) {
        final String k = ":_" + this.attributeValues.size();
        this.attributeValues.put(k, withAttributeValue);
        return k;
    }

    public DynamoDbUpdateExpressionMutator set(final String withKey, final AttributeValue withAttributeValue) {
        this.updateExpression
                .computeIfAbsent(UpdateExpressionClause.SET, k -> new ArrayList<>())
                .add(this.parseKey(withKey) + " = " + this.parseAttributeValue(withAttributeValue));
        return this;
    }

    public DynamoDbUpdateExpressionMutator listAppend(final String withKey, final AttributeValue withAttributeValue) {
        final String key = this.parseKey(withKey);
        this.updateExpression
                .computeIfAbsent(UpdateExpressionClause.SET, k -> new ArrayList<>())
                .add(key + " = list_append(" + key + ", " + this.parseAttributeValue(withAttributeValue) + ")");
        return this;
    }

    @Override
    public void accept(@NonNull final UpdateItemRequest.Builder builder) {
        // compile the update expression
        final String ue = this.getUpdateExpressionString();
        if (ue == null || ue.isBlank()) {
            return;
        }

        builder.updateExpression(ue)
                .expressionAttributeNames(attributeNames)
                .expressionAttributeValues(attributeValues);
    }
}
