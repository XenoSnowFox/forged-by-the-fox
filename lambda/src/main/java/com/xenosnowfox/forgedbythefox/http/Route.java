package com.xenosnowfox.forgedbythefox.http;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import lombok.NonNull;

public class Route implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private Map<HttpMethod, List<RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>>> handlers =
            new HashMap<>();

    private final Map<String, Route> subroutes = new HashMap<>();

    private final Map<String, Route> wildcardRoutes = new HashMap<>();

    public Route register(
            @NonNull final HttpMethod httpMethod,
            @NonNull final String path,
            @NonNull final RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> handler) {
        final String[] pathParts = path.split("/");
        Queue<String> urlParts = new LinkedList<>(Arrays.asList(pathParts));
        while (!urlParts.isEmpty()) {
            final String part = urlParts.remove();
            if (part == null || part.isBlank()) {
                continue;
            }

            // is this a wildcard field
            final Route r;
            if (part.startsWith("{") && part.endsWith("}")) {
                r = this.wildcardRoutes.computeIfAbsent(part.substring(1, part.length() - 1), unused -> new Route());
            } else {
                r = this.subroutes.computeIfAbsent(part, unused -> new Route());
            }

            final StringBuilder sb = new StringBuilder();
            while (!urlParts.isEmpty()) {
                final String subpart = urlParts.remove();
                if (subpart == null || subpart.isBlank()) {
                    continue;
                }
                sb.append(subpart).append("/");
            }
            r.register(httpMethod, sb.toString(), handler);
            return this;
        }

        this.handlers.computeIfAbsent(httpMethod, unused -> new ArrayList<>()).add(handler);
        return this;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent event, final Context context) {
        final String path = event.getPath();
        final String[] pathParts = path.split("/");
        Queue<String> urlParts = new LinkedList<>(Arrays.asList(pathParts));
        while (!urlParts.isEmpty()) {
            final String part = urlParts.remove();
            if (part == null || part.isBlank()) {
                continue;
            }

            final StringBuilder sb = new StringBuilder();
            while (!urlParts.isEmpty()) {
                final String subpart = urlParts.remove();
                if (subpart == null || subpart.isBlank()) {
                    continue;
                }
                sb.append(subpart).append("/");
            }
            event.setPath(sb.toString());

            // first check for a specific route
            if (this.subroutes.containsKey(part)) {
                final APIGatewayProxyResponseEvent response =
                        this.subroutes.get(part).handleRequest(event, context);
                if (response != null) {
                    return response;
                }
            }

            // attempt to match on wildcard routes
            final Map<String, String> pathParameters = new HashMap<>(event.getPathParameters());
            for (var x : this.wildcardRoutes.entrySet()) {
                final Map<String, String> pp = new HashMap<>(pathParameters);
                pp.put(x.getKey(), part);
                event.setPathParameters(pp);
                final APIGatewayProxyResponseEvent response = x.getValue().handleRequest(event, context);
                if (response != null) {
                    return response;
                }
            }

            // no sub paths to handle this request
            event.setPath(path);
            event.setPathParameters(pathParameters);
            return null;
        }

        // this route handles the request - try same http verb
        final HttpMethod httpMethod = HttpMethod.valueOf(event.getHttpMethod().toUpperCase(Locale.ROOT));
        for (var handler : this.handlers.computeIfAbsent(httpMethod, unused -> new ArrayList<>())) {
            final APIGatewayProxyResponseEvent response = handler.handleRequest(event, context);
            if (response != null) {
                return response;
            }
        }

        // look for an ANY handler
        for (var handler : this.handlers.computeIfAbsent(HttpMethod.ANY, unused -> new ArrayList<>())) {
            final APIGatewayProxyResponseEvent response = handler.handleRequest(event, context);
            if (response != null) {
                return response;
            }
        }

        return null;
    }
}
