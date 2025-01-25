package com.xenosnowfox.forgedbythefox.routes;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.xenosnowfox.forgedbythefox.ApiGatewayHandler;
import com.xenosnowfox.forgedbythefox.AuthenticatedRequestHandler;
import com.xenosnowfox.forgedbythefox.http.ApiGatewayProxyResponseEventBuilder;
import com.xenosnowfox.forgedbythefox.models.Playbook;
import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.character.Character;
import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.service.character.CreateCharacterRequest;
import com.xenosnowfox.forgedbythefox.service.template.TemplateService;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@lombok.Builder(builderClassName = "Builder")
public record HomepageRoute(TemplateService templateService) implements AuthenticatedRequestHandler {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .findAndRegisterModules()
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'"));

    @Override
    public APIGatewayProxyResponseEvent handleRequest(
            final APIGatewayProxyRequestEvent event,
            final Context context,
            final Account account,
            final Session session) {

        ApiGatewayHandler.UrlResolver urlResolver = urlPath -> urlPath;

        if (event.getHttpMethod().equalsIgnoreCase("POST")) {
            return createNewCharacter(event, context, account, urlResolver);
        }

        final Map<String, Object> contextData = new HashMap<>();
        contextData.put("url", urlResolver);
        contextData.put("account", account);
        contextData.put("session", session);

        // retrieve list of all characters
        final Set<Character> characterSet =
                templateService.characterService().query(b -> b.accountIdentifier(account.identifier()));
        contextData.put("characters", characterSet);

        final String html = templateService.parse("dashboard", contextData);

        return ApiGatewayProxyResponseEventBuilder.newInstance()
                .withStatusCode(200)
                .withHeader("Cache-Control", "private, no-cache, no-store, max-age=0, must-revalidate")
                .withHtml(html);
    }

    @Override
    public APIGatewayProxyResponseEvent handleUnauthenticatedRequest(
            final APIGatewayProxyRequestEvent event, final Context context) {
        if (!event.getHttpMethod().equalsIgnoreCase("GET")) {
            return ApiGatewayProxyResponseEventBuilder.newInstance().withTemporaryRedirect(event.getPath());
        }

        ApiGatewayHandler.UrlResolver urlResolver = urlPath -> urlPath;

        final Map<String, Object> contextData = new HashMap<>();
        contextData.put("url", urlResolver);

        final String html = templateService.parse("homepage", contextData);
        return ApiGatewayProxyResponseEventBuilder.newInstance()
                .withStatusCode(200)
                .withHtml(html);
    }

    private APIGatewayProxyResponseEvent createNewCharacter(
            final APIGatewayProxyRequestEvent event,
            final Context context,
            Account account,
            ApiGatewayHandler.UrlResolver urlResolver) {
        final String[] bodyParts = event.getBody().split("&");
        Map<String, String> formFields = new HashMap<>();
        for (String b : bodyParts) {
            final String[] x = b.split("=", 2);
            if (x[0].isBlank()) {
                continue;
            }

            formFields.put(x[0].trim().toLowerCase(Locale.ROOT), x[1].trim());
        }

        CreateCharacterRequest createCharacterRequest = CreateCharacterRequest.builder()
                .accountIdentifier(account.identifier())
                .playbook(Playbook.valueOf(formFields.get("playbook")))
                .build();

        final Character character = templateService.characterService().create(createCharacterRequest);

        // redirect to character sheet
        return ApiGatewayProxyResponseEventBuilder.newInstance()
                .withTemporaryRedirect(urlResolver.resolve(
                        "/characters/" + character.identifier().value()));
    }
}
