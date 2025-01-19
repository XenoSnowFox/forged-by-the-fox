package com.xenosnowfox.forgedbythefox.routes.fragment;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.xenosnowfox.forgedbythefox.models.HarmLevel;
import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.character.Character;
import com.xenosnowfox.forgedbythefox.models.character.CharacterHarm;
import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.service.template.TemplateService;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CharacterHarmDisplayFragment extends CharacterDisplayFragment {

    @lombok.Builder(builderClassName = "Builder")
    public CharacterHarmDisplayFragment(final TemplateService templateService) {
        super("harm", templateService);
    }

    public APIGatewayProxyResponseEvent handlePostRequest(
            final APIGatewayProxyRequestEvent event,
            final Context context,
            final Account account,
            final Session session,
            final Character character) {

        // ensure the character can only be edited by the owner's account
        if (!character.accountIdentifier().equals(account.identifier())) {
            return null;
        }

        Map<String, List<String>> form = this.parseBodyString(event.getBody());

        final CharacterHarm harm = new CharacterHarm();
        form.forEach((key, list) ->
                list.forEach(value -> harm.append(HarmLevel.valueOf(key.toUpperCase(Locale.ROOT)), value)));

        final Character mutatedCharacter = this.templateService()
                .characterService()
                .mutate(character)
                .withHarm(harm)
                .orNull();

        return renderPage(event, account, mutatedCharacter);
    }
}
