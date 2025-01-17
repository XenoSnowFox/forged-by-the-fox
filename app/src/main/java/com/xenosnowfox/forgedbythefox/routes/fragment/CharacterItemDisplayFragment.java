package com.xenosnowfox.forgedbythefox.routes.fragment;

import com.xenosnowfox.forgedbythefox.models.Item;
import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.character.Character;
import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.service.template.TemplateService;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class CharacterItemDisplayFragment extends CharacterDisplayFragment {

    @lombok.Builder(builderClassName = "Builder")
    public CharacterItemDisplayFragment(final TemplateService templateService) {
        super("items", templateService);
    }

    public void handlePostRequest(
            @NotNull final io.javalin.http.Context context,
            final Account account,
            final Session session,
            final Character character) {

        // ensure the character can only be edited by the owner's account
        if (!character.accountIdentifier().equals(account.identifier())) {
            return;
        }

        final Set<Item> items = new HashSet<>();
        Arrays.stream(Item.values())
                .filter(t -> context.formParam("item." + t.name()) != null)
                .forEach(items::add);

        final Character mutatedCharacter = this.templateService()
                .characterService()
                .mutate(character)
                .withItems(items)
                .orNull();

        super.renderPage(context, account, mutatedCharacter);
    }
}
