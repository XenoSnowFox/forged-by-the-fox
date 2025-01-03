package com.xenosnowfox.forgedbythefox.lambda.homepage.util;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.NonNull;

public class SimpleRouter implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final Map<String, List<RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>>> routes =
            new HashMap<>();

    public SimpleRouter register(
            @NonNull final String withResourcePath,
            @NonNull final RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> withHandler) {
        this.routes
                .computeIfAbsent(withResourcePath.trim(), unused -> new ArrayList<>())
                .add(withHandler);
        return this;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(
            final APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, final Context context) {
        final String urlPath = apiGatewayProxyRequestEvent.getResource().trim();
        final List<RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>> routeList =
                routes.computeIfAbsent(urlPath, unused -> new ArrayList<>());

        for (RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> handler : routeList) {
            final APIGatewayProxyResponseEvent response = handler.handleRequest(apiGatewayProxyRequestEvent, context);
            if (response != null) {
                return response;
            }
        }

        return null;
    }
}
