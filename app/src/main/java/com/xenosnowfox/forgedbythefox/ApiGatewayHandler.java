package com.xenosnowfox.forgedbythefox;

import com.xenosnowfox.forgedbythefox.routes.AuthenticationRoute;
import com.xenosnowfox.forgedbythefox.service.account.AccountManagementService;
import com.xenosnowfox.forgedbythefox.service.campaign.CampaignService;
import com.xenosnowfox.forgedbythefox.service.character.CharacterManagementService;
import com.xenosnowfox.forgedbythefox.service.faction.FactionManagementService;
import com.xenosnowfox.forgedbythefox.service.identity.IdentityManagementService;
import com.xenosnowfox.forgedbythefox.service.session.SessionManagementService;
import com.xenosnowfox.forgedbythefox.service.template.TemplateService;

public class ApiGatewayHandler {

    public interface UrlResolver {
        String resolve(String urlPath);
    }

    public ApiGatewayHandler() {
        super();

        final IdentityManagementService identityManagementService = new IdentityManagementService();

        final TemplateService templateService = TemplateService.builder()
                .templateEngine(TemplateService.TEMPLATE_ENGINE)
                .accountService(new AccountManagementService())
                .campaignService(new CampaignService())
                .characterService(new CharacterManagementService())
                .factionService(new FactionManagementService())
                .sessionService(new SessionManagementService())
                .build();

        AuthenticationRoute.builder()
                .accountManagementService(templateService.accountService())
                .identityManagementService(identityManagementService)
                .sessionManagementService(templateService.sessionService())
                .build();
    }
}
