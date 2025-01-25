package com.xenosnowfox.forgedbythefox.routes;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.xenosnowfox.forgedbythefox.ApiGatewayHandler;
import com.xenosnowfox.forgedbythefox.AuthenticatedRequestHandler;
import com.xenosnowfox.forgedbythefox.http.ApiGatewayProxyResponseEventBuilder;
import com.xenosnowfox.forgedbythefox.models.Attribute;
import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.campaign.Campaign;
import com.xenosnowfox.forgedbythefox.models.campaign.CampaignIdentifier;
import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.service.template.TemplateService;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Builder;

@Builder(builderClassName = "Builder")
public record CampaignFactionsRoute(TemplateService templateService) implements AuthenticatedRequestHandler {

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

        final String pathParameter = event.getPathParameters().get("campaign");
        if (pathParameter == null || pathParameter.isBlank()) {
            return null;
        }

        final Campaign campaign = templateService
                .campaignService()
                .fetch()
                .withIdentifier(new CampaignIdentifier(pathParameter))
                .orElse(null);
        if (campaign == null) {
            return null;
        }

        ApiGatewayHandler.UrlResolver urlResolver = urlPath -> urlPath;

        final Map<String, Object> ctx = new HashMap<>();
        ctx.put("account", account);
        ctx.put("attributes", Attribute.values());
        ctx.put("campaign", campaign);
        ctx.put("factions", List.of());
        ctx.put("url", urlResolver);

        final String html = templateService.parse("campaign-factions", ctx);
        return ApiGatewayProxyResponseEventBuilder.newInstance()
                .withStatusCode(200)
                .withHeader("Cache-control", "private, no-cache, no-store, max-age=0, must-revalidate")
                .withHtml(html);
    }
}
