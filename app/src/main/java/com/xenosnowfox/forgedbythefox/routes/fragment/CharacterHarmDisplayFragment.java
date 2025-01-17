package com.xenosnowfox.forgedbythefox.routes.fragment;

import com.xenosnowfox.forgedbythefox.models.HarmLevel;
import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.character.Character;
import com.xenosnowfox.forgedbythefox.models.character.CharacterHarm;
import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.service.template.TemplateService;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class CharacterHarmDisplayFragment extends CharacterDisplayFragment {

    @lombok.Builder(builderClassName = "Builder")
    public CharacterHarmDisplayFragment(final TemplateService templateService) {
        super("harm", templateService);
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

        final Map<String, List<String>> data = context.formParamMap();

        final CharacterHarm harm = new CharacterHarm();
        data.forEach((k, v) -> v.forEach(i -> harm.append(HarmLevel.valueOf(k.toUpperCase(Locale.ROOT)), i)));

        final Character mutatedCharacter = this.templateService()
                .characterService()
                .mutate(character)
                .withHarm(harm)
                .orNull();

        renderPage(context, account, mutatedCharacter);
    }
}
