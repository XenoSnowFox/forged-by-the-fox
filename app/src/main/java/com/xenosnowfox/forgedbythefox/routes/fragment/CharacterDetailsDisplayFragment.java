package com.xenosnowfox.forgedbythefox.routes.fragment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.character.Character;
import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.service.template.TemplateService;
import java.text.SimpleDateFormat;
import org.jetbrains.annotations.NotNull;

public class CharacterDetailsDisplayFragment extends CharacterDisplayFragment {

    @lombok.Builder(builderClassName = "Builder")
    public CharacterDetailsDisplayFragment(final TemplateService templateService) {
        super("details", templateService);
    }

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .findAndRegisterModules()
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'"));

    public void handlePostRequest(
            @NotNull final io.javalin.http.Context context,
            final Account account,
            final Session session,
            final Character character) {

        // ensure the character can only be edited by the owner's account
        if (!character.accountIdentifier().equals(account.identifier())) {
            return;
        }

        final Character mutatedCharacter = this.templateService()
                .characterService()
                .mutate(character)
                .withName(context.formParam("name"))
                .withAlias(context.formParam("alias"))
                .orNull();

        renderPage(context, account, mutatedCharacter);
    }
}
