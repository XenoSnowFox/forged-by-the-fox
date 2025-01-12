package com.xenosnowfox.forgedbythefox.routes.fragment;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.xenosnowfox.forgedbythefox.ApiGatewayHandler;
import com.xenosnowfox.forgedbythefox.AuthenticatedRequestHandler;
import com.xenosnowfox.forgedbythefox.models.Attribute;
import com.xenosnowfox.forgedbythefox.models.HarmLevel;
import com.xenosnowfox.forgedbythefox.models.Trauma;
import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.character.Character;
import com.xenosnowfox.forgedbythefox.models.character.CharacterIdentifier;
import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.service.template.TemplateService;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public abstract class CharacterDisplayFragment implements AuthenticatedRequestHandler {

    private final String templateName;

    private final TemplateService templateService;

    public abstract APIGatewayProxyResponseEvent handlePostRequest(
            final APIGatewayProxyRequestEvent event,
            final Context context,
            final Account account,
            final Session session,
            final Character character);

    public APIGatewayProxyResponseEvent handleRequest(
            final APIGatewayProxyRequestEvent event,
            final Context context,
            final Account account,
            final Session session,
            final Character character) {

        if (event.getHttpMethod().equalsIgnoreCase("post")) {
            final APIGatewayProxyResponseEvent postResponse =
                    this.handlePostRequest(event, context, account, session, character);
            if (postResponse != null) {
                return postResponse;
            }
        }

        return renderPage(event, account, character);
    }

    @Override
    public final APIGatewayProxyResponseEvent handleRequest(
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

        return this.handleRequest(event, context, account, session, character);
    }

    public APIGatewayProxyResponseEvent renderPage(
            final APIGatewayProxyRequestEvent event, final Account account, final Character character) {

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
        ctx.put(
                "HarmLevel",
                Arrays.stream(HarmLevel.values()).collect(Collectors.toMap(HarmLevel::name, Function.identity())));
        ctx.put("Trauma", Arrays.stream(Trauma.values()).collect(Collectors.toMap(Enum::name, Function.identity())));

        final String html = templateService.parse(
                "partial/character-sheet",
                Set.of(Optional.of(event)
                                .map(APIGatewayProxyRequestEvent::getQueryStringParameters)
                                .filter(x -> x.containsKey("edit"))
                                .map(x -> "edit")
                                .orElse("view")
                        + "-" + this.templateName),
                ctx);
        response.setBody(html);
        return response;
    }

    protected ApiGatewayHandler.UrlResolver urlResolver(final APIGatewayProxyRequestEvent event) {
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

    protected Map<String, List<String>> parseBodyString(final String withBody) {
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
