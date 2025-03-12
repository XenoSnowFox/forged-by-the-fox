package com.xenosnowfox.forgedbythefox;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.xenosnowfox.forgedbythefox.http.HttpMethod;
import com.xenosnowfox.forgedbythefox.http.Route;
import com.xenosnowfox.forgedbythefox.routes.AuthenticationRoute;
import com.xenosnowfox.forgedbythefox.routes.CampaignCharactersRoute;
import com.xenosnowfox.forgedbythefox.routes.CampaignFactionsRoute;
import com.xenosnowfox.forgedbythefox.routes.CharacterSheetRoute;
import com.xenosnowfox.forgedbythefox.routes.HomepageRoute;
import com.xenosnowfox.forgedbythefox.routes.PwaManifestRoute;
import com.xenosnowfox.forgedbythefox.routes.fragment.CampaignClocksDisplayFragment;
import com.xenosnowfox.forgedbythefox.routes.fragment.CampaignFactionDisplayFragment;
import com.xenosnowfox.forgedbythefox.routes.fragment.CharacterAbilitiesDisplayFragment;
import com.xenosnowfox.forgedbythefox.routes.fragment.CharacterDetailsDisplayFragment;
import com.xenosnowfox.forgedbythefox.routes.fragment.CharacterExperienceDisplayFragment;
import com.xenosnowfox.forgedbythefox.routes.fragment.CharacterHarmDisplayFragment;
import com.xenosnowfox.forgedbythefox.routes.fragment.CharacterHealingDisplayFragment;
import com.xenosnowfox.forgedbythefox.routes.fragment.CharacterItemDisplayFragment;
import com.xenosnowfox.forgedbythefox.routes.fragment.CharacterStressBarDisplayFragment;
import com.xenosnowfox.forgedbythefox.routes.fragment.CharacterStressDisplayFragment;
import com.xenosnowfox.forgedbythefox.routes.page.CampaignDetailsPageRoute;
import com.xenosnowfox.forgedbythefox.routes.page.campaign.crew.CharacterPlaybookPageRoute;
import com.xenosnowfox.forgedbythefox.service.account.AccountManagementService;
import com.xenosnowfox.forgedbythefox.service.campaign.CampaignService;
import com.xenosnowfox.forgedbythefox.service.character.CharacterManagementService;
import com.xenosnowfox.forgedbythefox.service.faction.FactionManagementService;
import com.xenosnowfox.forgedbythefox.service.identity.IdentityManagementService;
import com.xenosnowfox.forgedbythefox.service.session.SessionManagementService;
import com.xenosnowfox.forgedbythefox.service.template.TemplateService;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ApiGatewayHandler extends Route {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .findAndRegisterModules()
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'"));

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

        this.register(
                        HttpMethod.GET,
                        "/manifest.json",
                        PwaManifestRoute.builder().build())
                .register(
                        HttpMethod.ANY,
                        "/",
                        HomepageRoute.builder().templateService(templateService).build())
                .register(
                        HttpMethod.ANY,
                        "/auth",
                        AuthenticationRoute.builder()
                                .accountManagementService(templateService.accountService())
                                .identityManagementService(identityManagementService)
                                .sessionManagementService(templateService.sessionService())
                                .build())
                .register(
                        HttpMethod.ANY,
                        "/characters/{character}",
                        CharacterSheetRoute.builder()
                                .templateService(templateService)
                                .build())
                // Campaign Details Page
                .register(HttpMethod.GET, "/campaigns/{campaign}", new CampaignDetailsPageRoute(templateService))
                .register(
                        HttpMethod.GET,
                        "/campaigns/{campaign}/crew/{character}",
                        new CharacterPlaybookPageRoute(templateService))
                .register(
                        HttpMethod.ANY,
                        "/campaigns/{campaign}/characters",
                        CampaignCharactersRoute.builder()
                                .templateService(templateService)
                                .build())
                .register(
                        HttpMethod.ANY,
                        "/campaigns/{campaign}/factions",
                        CampaignFactionsRoute.builder()
                                .templateService(templateService)
                                .build())
                .register(
                        HttpMethod.ANY,
                        "/fragments/campaigns/{campaign}/clocks",
                        CampaignClocksDisplayFragment.builder()
                                .templateService(templateService)
                                .build())
                .register(
                        HttpMethod.ANY,
                        "/fragments/campaigns/{campaign}/factions",
                        CampaignFactionDisplayFragment.builder()
                                .templateService(templateService)
                                .build())
                .register(
                        HttpMethod.ANY,
                        "/fragments/characters/{character}/details",
                        CharacterDetailsDisplayFragment.builder()
                                .templateService(templateService)
                                .build())
                .register(
                        HttpMethod.ANY,
                        "/fragments/characters/{character}/abilities",
                        CharacterAbilitiesDisplayFragment.builder()
                                .templateService(templateService)
                                .build())
                .register(
                        HttpMethod.ANY,
                        "/fragments/characters/{character}/experience",
                        CharacterExperienceDisplayFragment.builder()
                                .templateService(templateService)
                                .build())
                .register(
                        HttpMethod.ANY,
                        "/fragments/characters/{character}/harm",
                        CharacterHarmDisplayFragment.builder()
                                .templateService(templateService)
                                .build())
                .register(
                        HttpMethod.ANY,
                        "/fragments/characters/{character}/healing",
                        CharacterHealingDisplayFragment.builder()
                                .templateService(templateService)
                                .build())
                .register(
                        HttpMethod.ANY,
                        "/fragments/characters/{character}/stress",
                        CharacterStressDisplayFragment.builder()
                                .templateService(templateService)
                                .build())
                .register(
                        HttpMethod.ANY,
                        "/fragments/characters/{character}/stress-bar",
                        CharacterStressBarDisplayFragment.builder()
                                .templateService(templateService)
                                .build())
                .register(
                        HttpMethod.ANY,
                        "/fragments/characters/{character}/items",
                        CharacterItemDisplayFragment.builder()
                                .templateService(templateService)
                                .build())
                .register(HttpMethod.GET, "/debug", (e1, c1) -> {
                    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
                    response.setIsBase64Encoded(false);
                    response.setStatusCode(200);

                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Cache-control", "private, no-cache, no-store, max-age=0, must-revalidate");
                    response.setHeaders(headers);

                    String json;
                    try {
                        json = OBJECT_MAPPER
                                .writerWithDefaultPrettyPrinter()
                                .writeValueAsString(Map.of("EVENT", e1, "CONTEXT", c1));
                    } catch (JsonProcessingException e) {
                        json = "{}";
                    }

                    response.setBody(json);
                    return response;
                });
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent event, final Context context) {
        return Optional.ofNullable(super.handleRequest(event, context)).orElseGet(() -> {
            final APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
            response.setIsBase64Encoded(false);
            response.setStatusCode(404);

            HashMap<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "text/html");
            headers.put("Cache-control", "private, no-cache, no-store, max-age=0, must-revalidate");
            response.setHeaders(headers);

            response.setBody("Page Not Found");
            return response;
        });
    }
}
