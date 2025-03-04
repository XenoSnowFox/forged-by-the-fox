package com.xenosnowfox.forgedbythefox.routes.page;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.xenosnowfox.forgedbythefox.ApiGatewayHandler;
import com.xenosnowfox.forgedbythefox.http.ApiGatewayProxyResponseEventBuilder;
import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.campaign.Campaign;
import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.service.template.TemplateService;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;

public class CampaignDetailsPageRoute extends AbstractCampaignPageRoute {
    public CampaignDetailsPageRoute(@NonNull final TemplateService templateService) {
        super(templateService);
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(
            final APIGatewayProxyRequestEvent event,
            final Context context,
            final Account account,
            final Session session,
            final Campaign campaign) {

        ApiGatewayHandler.UrlResolver urlResolver = urlPath -> urlPath;

        final Map<String, Object> contextData = new HashMap<>();
        contextData.put("account", account);
        contextData.put("campaign", campaign);
        contextData.put("url", urlResolver);

        final String html = this.templateService().parse("campaign-details", contextData);
        return ApiGatewayProxyResponseEventBuilder.newInstance()
                .withStatusCode(200)
                .withHtml(html);
    }
}
