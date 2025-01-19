package com.xenosnowfox.forgedbythefox.routes.fragment;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.xenosnowfox.forgedbythefox.models.Trauma;
import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.character.Character;
import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.service.template.TemplateService;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class CharacterStressDisplayFragment extends CharacterDisplayFragment {

    @lombok.Builder(builderClassName = "Builder")
    public CharacterStressDisplayFragment(final TemplateService templateService) {
        super("stress", templateService);
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

        final Set<Trauma> trauma = new HashSet<>();
        Arrays.stream(Trauma.values())
                .filter(t -> !form.containsKey("trauma." + t.name().toLowerCase(Locale.ROOT)))
                .forEach(trauma::add);

        final Character mutatedCharacter = this.templateService()
                .characterService()
                .mutate(character)
                .withStress(Optional.ofNullable(form.get("stress"))
                        .map(List::getFirst)
                        .map(Integer::parseInt)
                        .orElse(null))
                .withTrauma(trauma)
                .orNull();

        return renderPage(event, account, mutatedCharacter);
    }
}
