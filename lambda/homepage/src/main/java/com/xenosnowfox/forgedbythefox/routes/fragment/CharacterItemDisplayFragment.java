package com.xenosnowfox.forgedbythefox.routes.fragment;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.xenosnowfox.forgedbythefox.models.Item;
import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.character.Character;
import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.service.template.TemplateService;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class CharacterItemDisplayFragment extends CharacterDisplayFragment {

    @lombok.Builder(builderClassName = "Builder")
    public CharacterItemDisplayFragment(final TemplateService templateService) {
        super("items", templateService);
    }

    @Override
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

        final Map<String, List<String>> data = parseBodyString(event.getBody());

        final Set<Item> items = new HashSet<>();
        Arrays.stream(Item.values())
                .filter(t -> data.containsKey("item." + t.name().toLowerCase(Locale.ROOT)))
                .forEach(items::add);

        final Character mutatedCharacter = this.templateService()
                .characterService()
                .mutate(character)
                //                        .withStress(data.computeIfAbsent("stress", k -> new ArrayList<>()).stream()
                //                                .findFirst()
                //                                .map(Integer::parseInt)
                //                                .orElse(null))
                .withItems(items)
                .orNull();

        return super.renderPage(event, account, mutatedCharacter);
    }
}
