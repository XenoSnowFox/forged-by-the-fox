package com.xenosnowfox.forgedbythefox.routes.fragment;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.xenosnowfox.forgedbythefox.models.Ability;
import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.character.Character;
import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.service.template.TemplateService;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class CharacterAbilitiesDisplayFragment extends CharacterDisplayFragment {

    @lombok.Builder(builderClassName = "Builder")
    public CharacterAbilitiesDisplayFragment(final TemplateService templateService) {
        super("abilities", templateService);
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

        final Set<Ability> abilities =
                new HashSet<>(List.of(character.playbook().startingAbility()));
        character.playbook().specialAbilities().stream()
                .filter(ability -> form.containsKey("ability." + ability.name().toLowerCase(Locale.ROOT)))
                .forEach(abilities::add);

        final Character mutatedCharacter = this.templateService()
                .characterService()
                .mutate(character)
                .withAbilities(abilities)
                .orNull();

        return renderPage(event, account, mutatedCharacter);
    }
}
