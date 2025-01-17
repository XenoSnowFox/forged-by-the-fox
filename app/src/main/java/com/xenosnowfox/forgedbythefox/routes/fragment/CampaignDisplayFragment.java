package com.xenosnowfox.forgedbythefox.routes.fragment;

import com.xenosnowfox.forgedbythefox.ApiGatewayHandler;
import com.xenosnowfox.forgedbythefox.AuthenticatedRequestHandler;
import com.xenosnowfox.forgedbythefox.models.Attribute;
import com.xenosnowfox.forgedbythefox.models.HarmLevel;
import com.xenosnowfox.forgedbythefox.models.Trauma;
import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.campaign.Campaign;
import com.xenosnowfox.forgedbythefox.models.campaign.CampaignIdentifier;
import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.service.template.TemplateService;
import io.javalin.http.ContentType;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpStatus;
import java.util.Arrays;
import java.util.HashMap;
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
public abstract class CampaignDisplayFragment implements AuthenticatedRequestHandler {

    private final String templateName;

    private final TemplateService templateService;

    public abstract void handlePostRequest(
            @NotNull final io.javalin.http.Context context,
            final Account account,
            final Session session,
            Map<String, Object> contextObjects);

    public void handleRequest(
            @NotNull final io.javalin.http.Context context,
            final Account account,
            final Session session,
            Map<String, Object> contextObjects) {

        if (context.method() == HandlerType.POST) {
            this.handlePostRequest(context, account, session, contextObjects);
            return;
        }

        renderPage(context, account, contextObjects);
    }

    @Override
    public void handleRequest(
            @NotNull final io.javalin.http.Context context, final Account account, final Session session) {

        final String pathParameter = context.pathParam("campaign");
        if (pathParameter.isBlank()) {
            return;
        }

        final CampaignIdentifier identifier = new CampaignIdentifier(pathParameter);
        final Campaign instance = this.templateService
                .campaignService()
                .fetch()
                .withIdentifier(identifier)
                .orElse(null);
        if (instance == null) {
            return;
        }

        this.handleRequest(context, account, session, Map.of("campaign", instance));
    }

    public void renderPage(
            @NotNull final io.javalin.http.Context context, final Account account, Map<String, Object> contextObjects) {
        final Map<String, Object> ctx = new HashMap<>();
        ctx.put("account", account);
        ctx.put("attributes", Attribute.values());
        ctx.put("url", urlResolver());
        ctx.put(
                "HarmLevel",
                Arrays.stream(HarmLevel.values()).collect(Collectors.toMap(HarmLevel::name, Function.identity())));
        ctx.put("Trauma", Arrays.stream(Trauma.values()).collect(Collectors.toMap(Enum::name, Function.identity())));
        ctx.putAll(contextObjects);

        final String html = templateService.parse(
                "partial/campaign-sheet",
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
}
