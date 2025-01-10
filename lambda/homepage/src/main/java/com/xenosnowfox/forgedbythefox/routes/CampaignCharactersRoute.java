package com.xenosnowfox.forgedbythefox.routes;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.xenosnowfox.forgedbythefox.ApiGatewayHandler;
import com.xenosnowfox.forgedbythefox.AuthenticatedRequestHandler;
import com.xenosnowfox.forgedbythefox.models.Attribute;
import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.campaign.Campaign;
import com.xenosnowfox.forgedbythefox.models.campaign.CampaignIdentifier;
import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.service.account.AccountManagementService;
import com.xenosnowfox.forgedbythefox.service.session.SessionManagementService;
import com.xenosnowfox.forgedbythefox.service.template.TemplateService;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import lombok.Builder;

@Builder(builderClassName = "Builder")
public record CampaignCharactersRoute(
        AccountManagementService accountManagementService,
        SessionManagementService sessionManagementService,
        TemplateService templateService)
        implements AuthenticatedRequestHandler {

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

        final String campaignPathParameter = event.getPathParameters().get("campaign");
        if (campaignPathParameter == null || campaignPathParameter.isBlank()) {
            return null;
        }

        final Campaign campaign = templateService
                .campaignService()
                .fetch()
                .withIdentifier(new CampaignIdentifier(campaignPathParameter))
                .orElse(null);
        if (campaign == null) {
            return null;
        }

        ApiGatewayHandler.UrlResolver urlResolver = urlPath -> {
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

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setIsBase64Encoded(false);
        response.setStatusCode(200);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html");
        headers.put("Cache-control", "private, no-cache, no-store, max-age=0, must-revalidate");
        response.setHeaders(headers);

        final Map<String, Object> ctx = new HashMap<>();
        ctx.put("account", account);
        ctx.put("attributes", Attribute.values());
        ctx.put("campaign", campaign);
        ctx.put("url", urlResolver);

        final String html = templateService.parse("campaign-characters", ctx);
        response.setBody(html);
        return response;
    }
}
