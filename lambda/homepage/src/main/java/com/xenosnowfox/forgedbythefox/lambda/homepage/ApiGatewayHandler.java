package com.xenosnowfox.forgedbythefox.lambda.homepage;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.xenosnowfox.forgedbythefox.lambda.homepage.route.AuthenticationRoute;
import com.xenosnowfox.forgedbythefox.lambda.homepage.route.HomepageRoute;
import com.xenosnowfox.forgedbythefox.lambda.homepage.util.HttpMethod;
import com.xenosnowfox.forgedbythefox.lambda.homepage.util.Route;
import com.xenosnowfox.forgedbythefox.service.account.AccountManagementService;
import com.xenosnowfox.forgedbythefox.service.character.CharacterManagementService;
import com.xenosnowfox.forgedbythefox.service.identity.IdentityManagementService;
import com.xenosnowfox.forgedbythefox.service.session.SessionManagementService;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ApiGatewayHandler extends Route {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .findAndRegisterModules()
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'"));

    public interface UrlResolver {
        String resolve(String urlPath);
    }

    public ApiGatewayHandler() {
        super();

        final AccountManagementService accountManagementService = new AccountManagementService();
        final CharacterManagementService characterManagementService = new CharacterManagementService();
        final IdentityManagementService identityManagementService = new IdentityManagementService();
        final SessionManagementService sessionManagementService = new SessionManagementService();

        this.register(
                        HttpMethod.ANY,
                        "",
                        HomepageRoute.builder()
                                .accountManagementService(accountManagementService)
                                .characterManagementService(characterManagementService)
                                .sessionManagementService(sessionManagementService)
                                .build())
                .register(
                        HttpMethod.ANY,
                        "/auth",
                        AuthenticationRoute.builder()
                                .accountManagementService(accountManagementService)
                                .identityManagementService(identityManagementService)
                                .sessionManagementService(sessionManagementService)
                                .build())
                .register(HttpMethod.GET, "/debug", (e1, c1) -> {
                    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
                    response.setIsBase64Encoded(false);
                    response.setStatusCode(200);

                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Cache-control", "private, no-cache, no-store, max-age=0, must-revalidate");
                    response.setHeaders(headers);

                    String json;
                    try {
                        json = OBJECT_MAPPER
                                .writerWithDefaultPrettyPrinter()
                                .writeValueAsString(Map.of("EVENT", e1, "CONTEXT", c1));
                    } catch (JsonProcessingException e) {
                        json = "{}";
                    }

                    response.setBody(json);
                    return response;
                });
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent event, final Context context) {
        return Optional.ofNullable(super.handleRequest(event, context)).orElseGet(() -> {
            final APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
            response.setIsBase64Encoded(false);
            response.setStatusCode(404);

            HashMap<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "text/html");
            headers.put("Cache-control", "private, no-cache, no-store, max-age=0, must-revalidate");
            response.setHeaders(headers);

            response.setBody("Page Not Found");
            return response;
        });
    }
}
