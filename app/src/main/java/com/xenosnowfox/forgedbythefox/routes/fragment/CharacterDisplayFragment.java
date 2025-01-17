package com.xenosnowfox.forgedbythefox.routes.fragment;

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
import io.javalin.http.ContentType;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpStatus;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public abstract class CharacterDisplayFragment implements AuthenticatedRequestHandler {

    private final String templateName;

    private final TemplateService templateService;

    public abstract void handlePostRequest(
            @NotNull final io.javalin.http.Context context,
            final Account account,
            final Session session,
            final Character character);

    public void handleRequest(
            @NotNull final io.javalin.http.Context context,
            final Account account,
            final Session session,
            final Character character) {

        if (context.method() == HandlerType.POST) {
            this.handlePostRequest(context, account, session, character);
            return;
        }

        renderPage(context, account, character);
    }

    @Override
    public void handleRequest(
            @NotNull final io.javalin.http.Context context, final Account account, final Session session) {

        final String characterPathParameter = context.pathParam("character");
        if (characterPathParameter.isBlank()) {
            return;
        }

        final CharacterIdentifier characterIdentifier = new CharacterIdentifier(characterPathParameter);
        final Character character = this.templateService.characterService().retrieve(characterIdentifier);
        if (character == null) {
            return;
        }

        this.handleRequest(context, account, session, character);
    }

    public void renderPage(
            @NotNull final io.javalin.http.Context context, final Account account, final Character character) {

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
        ctx.put("url", urlResolver());
        ctx.put(
                "HarmLevel",
                Arrays.stream(HarmLevel.values()).collect(Collectors.toMap(HarmLevel::name, Function.identity())));
        ctx.put("Trauma", Arrays.stream(Trauma.values()).collect(Collectors.toMap(Enum::name, Function.identity())));

        final String html = templateService.parse(
                "partial/character-sheet",
                Set.of((context.queryParam("edit") == null ? "view" : "edit") + "-" + this.templateName),
                ctx);

        context.status(HttpStatus.OK)
                .contentType(ContentType.HTML)
                .header("Cache-Control", "private, no-cache, no-store, max-age=0, must-revalidate")
                .result(html);
    }

    protected ApiGatewayHandler.UrlResolver urlResolver() {
        return urlPath -> urlPath;
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
