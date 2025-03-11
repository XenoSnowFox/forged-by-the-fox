package com.xenosnowfox.forgedbythefox.routes.fragment;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.character.Character;
import com.xenosnowfox.forgedbythefox.models.character.CharacterHarm;
import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.service.template.TemplateService;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CharacterHealingDisplayFragment extends CharacterDisplayFragment {

    @lombok.Builder(builderClassName = "Builder")
    public CharacterHealingDisplayFragment(final TemplateService templateService) {
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

        final CharacterHarm harm = new CharacterHarm(character.harm());
        if (form.containsKey("decrement")) {
            harm.setHealing(character.harm().getHealing()
                    - Optional.ofNullable(form.get("decrement"))
                            .map(List::getFirst)
                            .map(Integer::parseInt)
                            .orElse(0));
        }

        if (form.containsKey("increment")) {
            harm.setHealing(character.harm().getHealing()
                    + Optional.ofNullable(form.get("increment"))
                            .map(List::getFirst)
                            .map(Integer::parseInt)
                            .orElse(0));
        }

        final Character mutatedCharacter = this.templateService()
                .characterService()
                .mutate(character)
                .withHarm(harm)
                .orNull();

        return renderPage(event, account, mutatedCharacter);
    }
}
