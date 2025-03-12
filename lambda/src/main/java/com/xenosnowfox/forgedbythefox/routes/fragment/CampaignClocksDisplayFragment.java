package com.xenosnowfox.forgedbythefox.routes.fragment;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.campaign.Campaign;
import com.xenosnowfox.forgedbythefox.models.clock.Clock;
import com.xenosnowfox.forgedbythefox.models.clock.ClockIdentifier;
import com.xenosnowfox.forgedbythefox.models.clock.ClockName;
import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.service.template.TemplateService;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class CampaignClocksDisplayFragment extends CampaignDisplayFragment {

    @lombok.Builder(builderClassName = "Builder")
    public CampaignClocksDisplayFragment(final TemplateService templateService) {
        super("clocks", templateService);
    }

    @Override
    public APIGatewayProxyResponseEvent handlePostRequest(
            final APIGatewayProxyRequestEvent event,
            final Context context,
            final Account account,
            final Session session,
            final Map<String, Object> contextObjects) {
        final Map<String, Object> ctx = new HashMap<>(contextObjects);

        if (ctx.get("campaign") instanceof Campaign campaign
                && campaign.account().equals(account.identifier())) {
            Map<String, List<String>> form = this.parseBodyString(event.getBody());

            if (form.containsKey("name") && form.containsKey("segments")) {

                final Clock clock = Clock.builder()
                        .timestampCreated(Instant.now())
                        .markedSegments(0)
                        .identifier(ClockIdentifier.random())
                        .name(Optional.ofNullable(form.get("name"))
                                .map(List::getFirst)
                                .map(ClockName::new)
                                .orElse(ClockName.UNKNOWN))
                        .totalSegments(Optional.ofNullable(form.get("segments"))
                                .map(List::getFirst)
                                .map(Integer::parseInt)
                                .orElse(1))
                        .build();

                final Campaign mutatedCampaign = templateService()
                        .campaignService()
                        .mutate()
                        .withIdentifier(campaign.identifier())
                        .addClock(clock)
                        .orNull();
                ctx.put("campaign", mutatedCampaign);
            }
        }

        return super.renderPage(event, account, ctx);
    }

    @Override
    public APIGatewayProxyResponseEvent renderPage(
            final APIGatewayProxyRequestEvent event, final Account account, final Map<String, Object> contextObjects) {
        final Map<String, Object> ctx = new HashMap<>(contextObjects);

        //        // we have a campaign
        //        if (ctx.get("campaign") instanceof Campaign campaign) {
        //            // search factions for this campaign
        //            ctx.put(
        //                    "factions",
        //                    this.templateService()
        //                            .factionService()
        //                            .query()
        //                            .withCampaign(campaign.identifier())
        //                            .withAlignment(Optional.of(event)
        //                                    .map(APIGatewayProxyRequestEvent::getQueryStringParameters)
        //                                    .map(params -> params.get("alignment"))
        //                                    .map(FactionAlignmentIdentifier::new)
        //                                    .orElse(null))
        //                            .orNothing()
        //                            .stream()
        //                            .sorted(Comparator.comparingInt(Faction::tier).reversed())
        //                            .toList());
        //        }

        return super.renderPage(event, account, ctx);
    }
}
