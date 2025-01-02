package com.xenosnowfox.forgedbythefox.lambda.homepage;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.xenosnowfox.forgedbythefox.library.models.Playbook;
import com.xenosnowfox.forgedbythefox.library.models.account.Account;
import com.xenosnowfox.forgedbythefox.library.models.character.Character;
import com.xenosnowfox.forgedbythefox.library.models.session.Session;
import com.xenosnowfox.forgedbythefox.library.models.session.SessionIdentifier;
import com.xenosnowfox.forgedbythefox.services.accountmanagement.AccountManagementService;
import com.xenosnowfox.forgedbythefox.services.charactermanagement.CharacterManagementService;
import com.xenosnowfox.forgedbythefox.services.charactermanagement.CreateCharacterRequest;
import com.xenosnowfox.forgedbythefox.services.sessionmanagement.SessionManagementService;
import com.xenosnowfox.forgedbythefox.services.templateengine.TemplateService;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ApiGatewayHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .findAndRegisterModules()
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'"));

    private final AccountManagementService accountManagementService = new AccountManagementService();

    private final CharacterManagementService characterManagementService = new CharacterManagementService();

    private final SessionManagementService sessionManagementService = new SessionManagementService();

    private interface UrlResolver {
        String resolve(String urlPath);
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent event, final Context context) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setIsBase64Encoded(false);
        response.setStatusCode(200);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html");
        headers.put("Cache-control", "private, no-cache, no-store, max-age=0, must-revalidate");
        response.setHeaders(headers);

        UrlResolver urlResolver = urlPath -> {
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

        final Map<String, Object> contextData = new HashMap<>();
        contextData.put("url", urlResolver);

        final Session currentSession = this.getSession(event);
        if (currentSession == null) {
            if (!event.getHttpMethod().equalsIgnoreCase("get")) {
                response.withHeaders(Map.of("Location", event.getPath()));
                response.setStatusCode(307);
                return response;
            }

            // TODO remove session cookie
            final String html = TemplateService.parse("homepage", contextData);
            response.setBody(html);
            return response;
        }
        contextData.put("session", currentSession);

        // fetch account details.
        final Account account = accountManagementService.retrieve(currentSession.accountIdentifier());
        if (account == null) {
            if (!event.getHttpMethod().equalsIgnoreCase("get")) {
                response.withHeaders(Map.of("Location", event.getPath()));
                response.setStatusCode(307);
                return response;
            }

            // TODO remove session cookie
            final String html = TemplateService.parse("homepage", contextData);
            response.setBody(html);
            return response;
        }
        contextData.put("account", account);

        // check if creating a new character
        if (event.getHttpMethod().equalsIgnoreCase("post")) {
            createNewCharacter(event, account, urlResolver, response);
            return response;
        }

        // retrieve list of all characters
        final Set<Character> characterSet =
                characterManagementService.query(b -> b.accountIdentifier(account.identifier()));
        contextData.put("characters", characterSet);

        String json;
        try {
            json = OBJECT_MAPPER
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(Map.of("EVENT", event, "CONTEXT", context));
        } catch (JsonProcessingException e) {
            json = "{}";
        }

        contextData.put("json", json);
        final String html = TemplateService.parse("dashboard", contextData);
        response.setBody(html);
        return response;
    }

    private void createNewCharacter(
            final APIGatewayProxyRequestEvent event,
            Account account,
            UrlResolver urlResolver,
            APIGatewayProxyResponseEvent response) {
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

        final Character character = characterManagementService.create(createCharacterRequest);

        // redirect to character sheet
        response.setStatusCode(307);
        response.withHeaders(Map.of(
                "Location",
                urlResolver.resolve("/characters/" + character.identifier().value())));
    }

    private SessionIdentifier getSessionIdentifierFromEvent(final APIGatewayProxyRequestEvent event) {
        if (event == null) {
            return null;
        }

        final Map<String, String> headers = event.getHeaders();
        if (headers == null || headers.isEmpty() || !headers.containsKey("Cookie")) {
            return null;
        }

        final String cookieHeader = headers.get("Cookie");
        if (cookieHeader == null || cookieHeader.isBlank()) {
            return null;
        }

        final String[] cookies = cookieHeader.split(";");
        for (String cookie : cookies) {
            final String[] cookieParts = cookie.split("=");
            if (cookieParts[0].trim().equalsIgnoreCase("session")) {
                return new SessionIdentifier(cookieParts[1].trim());
            }
        }

        return null;
    }

    private Session getSession(final APIGatewayProxyRequestEvent event) {
        final SessionIdentifier sessionIdentifier = this.getSessionIdentifierFromEvent(event);
        if (sessionIdentifier == null) {
            return null;
        }

        System.out.println("Fetching session: " + sessionIdentifier);
        return sessionManagementService.retrieve(sessionIdentifier);
    }
}
