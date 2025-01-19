package com.xenosnowfox.forgedbythefox.routes.fragment;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.campaign.Campaign;
import com.xenosnowfox.forgedbythefox.models.faction.Faction;
import com.xenosnowfox.forgedbythefox.models.faction.FactionAlignmentIdentifier;
import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.service.template.TemplateService;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class CampaignFactionDisplayFragment extends CampaignDisplayFragment {

    @lombok.Builder(builderClassName = "Builder")
    public CampaignFactionDisplayFragment(final TemplateService templateService) {
        super("factions", templateService);
    }

    @Override
    public APIGatewayProxyResponseEvent handlePostRequest(
            final APIGatewayProxyRequestEvent event,
            final Context context,
            final Account account,
            final Session session,
            final Map<String, Object> contextObjects) {
        return null;
    }

    @Override
    public APIGatewayProxyResponseEvent renderPage(
            final APIGatewayProxyRequestEvent event, final Account account, final Map<String, Object> contextObjects) {
        final Map<String, Object> ctx = new HashMap<>(contextObjects);

        // we have a campaign
        if (ctx.get("campaign") instanceof Campaign campaign) {
            // search factions for this campaign
            ctx.put(
                    "factions",
                    this.templateService()
                            .factionService()
                            .query()
                            .withCampaign(campaign.identifier())
                            .withAlignment(Optional.of(event)
                                    .map(APIGatewayProxyRequestEvent::getQueryStringParameters)
                                    .map(params -> params.get("alignment"))
                                    .map(FactionAlignmentIdentifier::new)
                                    .orElse(null))
                            .orNothing()
                            .stream()
                            .sorted(Comparator.comparingInt(Faction::tier).reversed())
                            .toList());
        }

        return super.renderPage(event, account, ctx);
    }
}
