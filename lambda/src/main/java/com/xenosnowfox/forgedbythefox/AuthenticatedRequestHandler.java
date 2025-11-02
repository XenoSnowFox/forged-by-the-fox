package com.xenosnowfox.forgedbythefox;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.models.session.SessionIdentifier;
import com.xenosnowfox.forgedbythefox.service.template.TemplateService;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public interface AuthenticatedRequestHandler
        extends RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    TemplateService templateService();

    APIGatewayProxyResponseEvent handleRequest(
            final APIGatewayProxyRequestEvent event,
            final Context context,
            final Account account,
            final Session session);

    private SessionIdentifier getSessionIdentifierFromEvent(final APIGatewayProxyRequestEvent event) {
        if (event == null) {
            return null;
        }

        final Map<String, String> headers = event.getHeaders();
        if (headers == null || headers.isEmpty()) {
            return null;
        }

        for (Map.Entry<String, String> header : headers.entrySet()) {
            if (!header.getKey().equalsIgnoreCase("cookie")) {
                continue;
            }

            final String cookieHeader = header.getValue();
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
        }

        return null;
    }

    private Session getSession(final APIGatewayProxyRequestEvent event) {
        return Optional.of(event)
                .map(this::getSessionIdentifierFromEvent)
                .map(templateService().sessionService()::retrieve)
                .orElse(null);
    }

    default APIGatewayProxyResponseEvent handleUnauthenticatedRequest(
            final APIGatewayProxyRequestEvent event, final Context context) {

        final String cookieValue = "session=; Secure; HttpOnly; SameSite=Lax; Path=/; Expires="
                + DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
                        .withZone(ZoneId.of("GMT"))
                        .format(Instant.now());

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setIsBase64Encoded(false);

        if (event.getPath().toLowerCase(Locale.ROOT).startsWith("/fragments/")) {
            response.setStatusCode(401);
        } else {
            response.setStatusCode(307);
        }

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Location", "/");
        headers.put("Set-Cookie", cookieValue);
        response.setHeaders(headers);

        response.setBody("");

        return response;
    }

    default APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent event, final Context context) {

        final Session session = this.getSession(event);
        if (session == null) {
            return this.handleUnauthenticatedRequest(event, context);
        }

        final Account account = this.templateService().accountService().retrieve(session.accountIdentifier());
        if (account == null) {
            return this.handleUnauthenticatedRequest(event, context);
        }

        return this.handleRequest(event, context, account, session);
    }
}
