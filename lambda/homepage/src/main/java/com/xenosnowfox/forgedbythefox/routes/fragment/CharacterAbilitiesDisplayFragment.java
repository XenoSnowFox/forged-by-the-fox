package com.xenosnowfox.forgedbythefox.routes.fragment;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.xenosnowfox.forgedbythefox.ApiGatewayHandler;
import com.xenosnowfox.forgedbythefox.AuthenticatedRequestHandler;
import com.xenosnowfox.forgedbythefox.models.Ability;
import com.xenosnowfox.forgedbythefox.models.Attribute;
import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.character.Character;
import com.xenosnowfox.forgedbythefox.models.character.CharacterIdentifier;
import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.service.account.AccountManagementService;
import com.xenosnowfox.forgedbythefox.service.session.SessionManagementService;
import com.xenosnowfox.forgedbythefox.service.template.TemplateService;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.Builder;

@Builder(builderClassName = "Builder")
public record CharacterAbilitiesDisplayFragment(
        AccountManagementService accountManagementService,
        SessionManagementService sessionManagementService,
        TemplateService templateService)
        implements AuthenticatedRequestHandler {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(
            final APIGatewayProxyRequestEvent event,
            final Context context,
            final Account account,
            final Session session) {

        final String characterPathParameter = event.getPathParameters().get("character");
        if (characterPathParameter == null || characterPathParameter.isBlank()) {
            return null;
        }

        final CharacterIdentifier characterIdentifier = new CharacterIdentifier(characterPathParameter);
        final Character character = this.templateService.characterService().retrieve(characterIdentifier);
        if (character == null) {
            return null;
        }

        if (event.getHttpMethod().equalsIgnoreCase("post")) {
            final APIGatewayProxyResponseEvent postResponse =
                    this.handlePostRequest(event, context, account, session, character);
            if (postResponse != null) {
                return postResponse;
            }
        }

        return renderPage(event, context, account, session, character);
    }

    public APIGatewayProxyResponseEvent renderPage(
            final APIGatewayProxyRequestEvent event,
            final Context context,
            final Account account,
            final Session session,
            final Character character) {

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setIsBase64Encoded(false);
        response.setStatusCode(200);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html");
        headers.put("Cache-control", "private, no-cache, no-store, max-age=0, must-revalidate");
        response.setHeaders(headers);

        final Map<String, Object> ctx = new HashMap<>();
        ctx.put("account", account);
        ctx.put("attributes", Attribute.values());
        ctx.put("character", character);
        ctx.put("url", urlResolver(event));

        final String html = templateService.parse(
                "partial/character-sheet",
                Set.of(Optional.of(event)
                                .map(APIGatewayProxyRequestEvent::getQueryStringParameters)
                                .filter(x -> x.containsKey("edit"))
                                .map(x -> "edit")
                                .orElse("view")
                        + "-abilities"),
                ctx);
        response.setBody(html);
        return response;
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

        final Map<String, List<String>> data = parseBodyString(event.getBody());

        final Set<Ability> abilities =
                new HashSet<>(List.of(character.playbook().startingAbility()));
        character.playbook().specialAbilities().stream()
                .filter(ability -> data.containsKey("ability." + ability.name().toLowerCase(Locale.ROOT)))
                .forEach(abilities::add);

        final Character mutatedCharacter = this.templateService
                .characterService()
                .mutate(character)
                .withAbilities(abilities)
                .orNull();

        return renderPage(event, context, account, session, mutatedCharacter);
    }

    private ApiGatewayHandler.UrlResolver urlResolver(final APIGatewayProxyRequestEvent event) {
        return urlPath -> {
            if (!event.getRequestContext()
                    .getDomainName()
                    .toLowerCase(Locale.ROOT)
                    .endsWith(".amazonaws.com")) {
                return urlPath;
            }

            return URI.create("https://" + event.getRequestContext().getDomainName() + "/")
                    .resolve(urlPath)
                    .toString()
                    .replace(
                            "https://" + event.getRequestContext().getDomainName() + "/",
                            "https://" + event.getRequestContext().getDomainName() + "/"
                                    + event.getRequestContext().getStage() + "/");
        };
    }

    private Map<String, List<String>> parseBodyString(final String withBody) {
        final Map<String, List<String>> map = new HashMap<>();
        if (withBody == null || withBody.isBlank()) {
            return map;
        }

        final String[] parts = withBody.split("&");
        for (String part : parts) {
            final String[] subpart = part.split("=", 2);
            final String value = subpart.length > 1 ? URLDecoder.decode(subpart[1], StandardCharsets.UTF_8) : null;
            map.computeIfAbsent(subpart[0].toLowerCase(Locale.ROOT), k -> new ArrayList<>())
                    .add(value);
        }

        return map;
    }
}
