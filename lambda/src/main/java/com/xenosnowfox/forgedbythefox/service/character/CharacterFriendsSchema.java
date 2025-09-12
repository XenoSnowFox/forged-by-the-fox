package com.xenosnowfox.forgedbythefox.service.character;

import com.xenosnowfox.forgedbythefox.models.character.CharacterFriend;
import com.xenosnowfox.forgedbythefox.models.character.CharacterFriends;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticImmutableTableSchema;

@UtilityClass
public class CharacterFriendsSchema {

    @Getter
    private final TableSchema<CharacterFriends> tableSchema = StaticImmutableTableSchema.builder(
                    CharacterFriends.class, CharacterFriends.Builder.class)
            .newItemBuilder(CharacterFriends::builder, CharacterFriends.Builder::build)
            .addAttribute(String.class, attributeBuilder -> attributeBuilder
                    .name("type")
                    .getter(CharacterFriends::type)
                    .setter(CharacterFriends.Builder::type))
            .addAttribute(
                    EnhancedType.mapOf(
                            EnhancedType.of(String.class),
                            EnhancedType.documentOf(CharacterFriend.class, CharacterFriendSchema.getTableSchema())),
                    attributeBuilder -> attributeBuilder
                            .name("list")
                            .getter(CharacterFriends::list)
                            .setter(CharacterFriends.Builder::list))
            .build();
}
