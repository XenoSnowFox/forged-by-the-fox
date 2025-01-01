package com.xenosnowfox.forgedbythefox.lambda.homepage;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.xenosnowfox.forgedbythefox.services.templateengine.TemplateService;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ApiGatewayHandler implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

    private static final ObjectMapper OBJECT_MAPPER =
            new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

    @Override
    public APIGatewayV2HTTPResponse handleRequest(final APIGatewayV2HTTPEvent event, final Context context) {

        APIGatewayV2HTTPResponse response = new APIGatewayV2HTTPResponse();
        response.setIsBase64Encoded(false);
        response.setStatusCode(200);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html");
        headers.put("Cache-control", "private, no-cache, no-store, max-age=0, must-revalidate");
        response.setHeaders(headers);

        final String html = TemplateService.parse(isSignedIn(event) ? "dashboard" : "homepage", Collections.emptyMap());
        response.setBody(html);

        return response;
    }

    private boolean isSignedIn(final APIGatewayV2HTTPEvent event) {
        if (event == null) {
            return false;
        }

        final Map<String, String> headers = event.getHeaders();
        if (headers == null || headers.isEmpty() || !headers.containsKey("Cookie")) {
            return false;
        }

        final String cookieHeader = headers.get("Cookie");
        if (cookieHeader == null || cookieHeader.isBlank()) {
            return false;
        }

        final String[] cookies = cookieHeader.split(";");
        for (String cookie : cookies) {
            if (cookie.split("=")[0].trim().equalsIgnoreCase("session")) {
                return true;
            }
        }

        return false;
    }
}
