package com.xenosnowfox.forgedbythefox.routes.fragment;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
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
import com.xenosnowfox.forgedbythefox.util.ApiGatewayProxyResponseEventBuilder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public abstract class CampaignDisplayFragment implements AuthenticatedRequestHandler {

    private final String templateName;

    private final TemplateService templateService;

    public abstract APIGatewayProxyResponseEvent handlePostRequest(
            final APIGatewayProxyRequestEvent event,
            final Context context,
            final Account account,
            final Session session,
            Map<String, Object> contextObjects);

    public APIGatewayProxyResponseEvent handleRequest(
            final APIGatewayProxyRequestEvent event,
            final Context context,
            final Account account,
            final Session session,
            Map<String, Object> contextObjects) {

        if (event.getHttpMethod().equalsIgnoreCase("POST")) {
            return this.handlePostRequest(event, context, account, session, contextObjects);
        }

        return renderPage(event, account, contextObjects);
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(
            final APIGatewayProxyRequestEvent event,
            final Context context,
            final Account account,
            final Session session) {

        final String pathParameter = event.getPathParameters().get("campaign");
        if (pathParameter == null || pathParameter.isBlank()) {
            return null;
        }

        final CampaignIdentifier identifier = new CampaignIdentifier(pathParameter);
        final Campaign instance = this.templateService
                .campaignService()
                .fetch()
                .withIdentifier(identifier)
                .orElse(null);
        if (instance == null) {
            return null;
        }

        return this.handleRequest(event, context, account, session, Map.of("campaign", instance));
    }

    public APIGatewayProxyResponseEvent renderPage(
            final APIGatewayProxyRequestEvent event, final Account account, Map<String, Object> contextObjects) {
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
                Set.of(Optional.of(event)
                                .map(APIGatewayProxyRequestEvent::getQueryStringParameters)
                                .filter(x -> x.containsKey("edit"))
                                .map(x -> "edit")
                                .orElse("view")
                        + "-" + this.templateName),
                ctx);

        return ApiGatewayProxyResponseEventBuilder.newInstance()
                .withStatusCode(200)
                .withHeader("Cache-Control", "private, no-cache, no-store, max-age=0, must-revalidate")
                .withHtml(html);
    }

    protected ApiGatewayHandler.UrlResolver urlResolver() {
        return urlPath -> urlPath;
    }
}
