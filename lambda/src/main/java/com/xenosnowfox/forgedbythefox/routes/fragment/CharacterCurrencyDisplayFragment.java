package com.xenosnowfox.forgedbythefox.routes.fragment;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.character.Character;
import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.service.character.CharacterMutationExecutor;
import com.xenosnowfox.forgedbythefox.service.template.TemplateService;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CharacterCurrencyDisplayFragment extends CharacterDisplayFragment {

    @lombok.Builder(builderClassName = "Builder")
    public CharacterCurrencyDisplayFragment(final TemplateService templateService) {
        super("currency", templateService);
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

        final CharacterMutationExecutor mutator =
                this.templateService().characterService().mutate(character);

        Map<String, List<String>> form = this.parseBodyString(event.getBody());

        String attribute =
                Optional.ofNullable(form.get("attribute")).map(List::getFirst).orElse(null);

        if (attribute.equalsIgnoreCase("credits")) {
            int amount = character.credits();
            if (form.containsKey("decrement")) {

                amount -= Optional.ofNullable(form.get("decrement"))
                        .map(List::getFirst)
                        .map(Integer::parseInt)
                        .orElse(0);
            }

            if (form.containsKey("increment")) {
                amount += Optional.ofNullable(form.get("increment"))
                        .map(List::getFirst)
                        .map(Integer::parseInt)
                        .orElse(0);
            }
            amount = Math.clamp(amount, 0, 4);
            mutator.withCredits(amount);
        }

        if (attribute.equalsIgnoreCase("stash")) {
            int amount = character.stash();
            if (form.containsKey("decrement")) {

                amount -= Optional.ofNullable(form.get("decrement"))
                        .map(List::getFirst)
                        .map(Integer::parseInt)
                        .orElse(0);
            }

            if (form.containsKey("increment")) {
                amount += Optional.ofNullable(form.get("increment"))
                        .map(List::getFirst)
                        .map(Integer::parseInt)
                        .orElse(0);
            }
            amount = Math.clamp(amount, 0, 40);
            mutator.withStash(amount);
        }

        return renderPage(event, account, mutator.orNull());
    }
}
