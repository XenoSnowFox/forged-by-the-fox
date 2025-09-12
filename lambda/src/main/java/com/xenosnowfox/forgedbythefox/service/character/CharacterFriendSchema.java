package com.xenosnowfox.forgedbythefox.service.character;

import com.xenosnowfox.forgedbythefox.models.FriendDisposition;
import com.xenosnowfox.forgedbythefox.models.character.CharacterFriend;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticImmutableTableSchema;

@UtilityClass
public class CharacterFriendSchema {

    @Getter
    private final TableSchema<CharacterFriend> tableSchema = StaticImmutableTableSchema.builder(
                    CharacterFriend.class, CharacterFriend.Builder.class)
            .newItemBuilder(CharacterFriend::builder, CharacterFriend.Builder::build)
            .addAttribute(String.class, attributeBuilder -> attributeBuilder
                    .name("name")
                    .getter(CharacterFriend::name)
                    .setter(CharacterFriend.Builder::name))
            .addAttribute(String.class, attributeBuilder -> attributeBuilder
                    .name("description")
                    .getter(CharacterFriend::description)
                    .setter(CharacterFriend.Builder::description))
            .addAttribute(String.class, builder -> builder.name("disposition")
                    .getter(record -> record.disposition().toString())
                    .setter((b, i) -> b.disposition(FriendDisposition.valueOf(i))))
            .build();
}
