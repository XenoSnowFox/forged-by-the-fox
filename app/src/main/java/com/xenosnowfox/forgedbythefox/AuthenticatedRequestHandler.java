package com.xenosnowfox.forgedbythefox;

import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.models.session.SessionIdentifier;
import com.xenosnowfox.forgedbythefox.routes.JavalinRoute;
import com.xenosnowfox.forgedbythefox.service.template.TemplateService;
import org.jetbrains.annotations.NotNull;

public interface AuthenticatedRequestHandler extends JavalinRoute {

    TemplateService templateService();

    void handleRequest(@NotNull io.javalin.http.Context context, final Account account, final Session session);

    private SessionIdentifier getSessionIdentifierFromEvent(@NotNull io.javalin.http.Context context) {
        final String cookieHeader = context.header("cookie");
        if (cookieHeader == null || cookieHeader.isEmpty()) {
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

    private Session getSession(@NotNull io.javalin.http.Context context) {
        final SessionIdentifier identifier = this.getSessionIdentifierFromEvent(context);
        if (identifier == null) {
            return null;
        }

        return this.templateService().sessionService().retrieve(identifier);
    }

    default void handleUnauthenticatedRequest(@NotNull io.javalin.http.Context context) {
        context.status(401).header("Location", "/").cookie("session", "", 0).result("");
    }

    @Override
    default void handle(@NotNull io.javalin.http.Context context) throws Exception {
        final Session session = this.getSession(context);
        if (session == null) {
            this.handleUnauthenticatedRequest(context);
            return;
        }

        final Account account = this.templateService().accountService().retrieve(session.accountIdentifier());
        if (account == null) {
            this.handleUnauthenticatedRequest(context);
            return;
        }

        this.handleRequest(context, account, session);
    }
}
