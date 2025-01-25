package com.xenosnowfox.forgedbythefox.routes.fragment;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.xenosnowfox.forgedbythefox.http.ApiGatewayProxyResponseEventBuilder;
import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.character.Character;
import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.service.template.TemplateService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class CharacterStressBarDisplayFragment extends CharacterDisplayFragment {

    @lombok.Builder(builderClassName = "Builder")
    public CharacterStressBarDisplayFragment(final TemplateService templateService) {
        super("stress-bar", templateService);
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

        int stress = character.stress();
        if (form.containsKey("decrement")) {

            stress -= Optional.ofNullable(form.get("decrement"))
                    .map(List::getFirst)
                    .map(Integer::parseInt)
                    .orElse(0);
        }

        if (form.containsKey("increment")) {
            stress += Optional.ofNullable(form.get("increment"))
                    .map(List::getFirst)
                    .map(Integer::parseInt)
                    .orElse(0);
        }

        stress = Math.clamp(stress, 0, 9);

        final Character mutatedCharacter = this.templateService()
                .characterService()
                .mutate(character)
                .withStress(stress)
                .orNull();

        return renderPage(event, account, mutatedCharacter);
    }

    @Override
    public APIGatewayProxyResponseEvent renderPage(
            final APIGatewayProxyRequestEvent event, final Account account, final Character character) {

        final Map<String, Object> ctx = new HashMap<>();
        ctx.put("character", character);

        final String html = this.templateService().parse("component/stress-meter", Set.of("root"), ctx);

        return ApiGatewayProxyResponseEventBuilder.newInstance()
                .withStatusCode(200)
                .withHeader("Cache-Control", "private, no-cache, no-store, max-age=0, must-revalidate")
                .withHtml(html);
    }
}
