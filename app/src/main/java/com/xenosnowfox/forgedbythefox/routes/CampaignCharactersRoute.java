package com.xenosnowfox.forgedbythefox.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.xenosnowfox.forgedbythefox.ApiGatewayHandler;
import com.xenosnowfox.forgedbythefox.AuthenticatedRequestHandler;
import com.xenosnowfox.forgedbythefox.models.Attribute;
import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.campaign.Campaign;
import com.xenosnowfox.forgedbythefox.models.campaign.CampaignIdentifier;
import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.service.template.TemplateService;
import io.javalin.http.ContentType;
import io.javalin.http.HttpStatus;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import org.jetbrains.annotations.NotNull;

@Builder(builderClassName = "Builder")
public record CampaignCharactersRoute(TemplateService templateService) implements AuthenticatedRequestHandler {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .findAndRegisterModules()
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'"));

    @Override
    public void handleRequest(
            @NotNull final io.javalin.http.Context context, final Account account, final Session session) {

        final String campaignPathParameter = context.pathParam("campaign");
        if (campaignPathParameter.isBlank()) {
            return;
        }

        final Campaign campaign = templateService
                .campaignService()
                .fetch()
                .withIdentifier(new CampaignIdentifier(campaignPathParameter))
                .orElse(null);
        if (campaign == null) {
            return;
        }

        ApiGatewayHandler.UrlResolver urlResolver = urlPath -> urlPath;

        final Map<String, Object> ctx = new HashMap<>();
        ctx.put("account", account);
        ctx.put("attributes", Attribute.values());
        ctx.put("campaign", campaign);
        ctx.put("url", urlResolver);

        final String html = templateService.parse("campaign-characters", ctx);

        context.status(HttpStatus.OK)
                .contentType(ContentType.HTML)
                .header("Cache-control", "private, no-cache, no-store, max-age=0, must-revalidate")
                .result(html);
    }
}
