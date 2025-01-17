package com.xenosnowfox.forgedbythefox.routes.fragment;

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
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
public class CampaignFactionDisplayFragment extends CampaignDisplayFragment {

    @lombok.Builder(builderClassName = "Builder")
    public CampaignFactionDisplayFragment(final TemplateService templateService) {
        super("factions", templateService);
    }

    @Override
    public void renderPage(
            @NotNull final io.javalin.http.Context context,
            final Account account,
            final Map<String, Object> contextObjects) {
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
                            .withAlignment(Optional.of(context)
                                    .map(params -> params.queryParam("alignment"))
                                    .map(FactionAlignmentIdentifier::new)
                                    .orElse(null))
                            .orNothing()
                            .stream()
                            .sorted(Comparator.comparingInt(Faction::tier).reversed())
                            .toList());
        }

        super.renderPage(context, account, ctx);
    }

    public void handlePostRequest(
            @NotNull final io.javalin.http.Context context,
            final Account account,
            final Session session,
            final Map<String, Object> contextObjects) {}
}
