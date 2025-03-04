package com.xenosnowfox.forgedbythefox.routes.page;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.xenosnowfox.forgedbythefox.AuthenticatedRequestHandler;
import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.campaign.Campaign;
import com.xenosnowfox.forgedbythefox.models.campaign.CampaignIdentifier;
import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.service.template.TemplateService;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@RequiredArgsConstructor
public abstract class AbstractCampaignPageRoute implements AuthenticatedRequestHandler {

    @NonNull @Getter
    private final TemplateService templateService;

    public abstract APIGatewayProxyResponseEvent handleRequest(
            final APIGatewayProxyRequestEvent event,
            final Context context,
            final Account account,
            final Session session,
            final Campaign campaign);

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

        return this.handleRequest(event, context, account, session, campaign);
    }
}
