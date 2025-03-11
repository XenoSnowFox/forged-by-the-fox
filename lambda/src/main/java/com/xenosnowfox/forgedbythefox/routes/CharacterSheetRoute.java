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
import com.xenosnowfox.forgedbythefox.models.HarmLevel;
import com.xenosnowfox.forgedbythefox.models.Trauma;
import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.character.Character;
import com.xenosnowfox.forgedbythefox.models.character.CharacterIdentifier;
import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.service.campaign.CampaignRetrievalExecutor;
import com.xenosnowfox.forgedbythefox.service.template.TemplateService;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder(builderClassName = "Builder")
public record CharacterSheetRoute(TemplateService templateService) implements AuthenticatedRequestHandler {

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

        final String characterPathParameter = event.getPathParameters().get("character");
        if (characterPathParameter == null || characterPathParameter.isBlank()) {
            return null;
        }

        final CharacterIdentifier characterIdentifier = new CharacterIdentifier(characterPathParameter);
        final Character character = this.templateService.characterService().retrieve(characterIdentifier);
        if (character == null) {
            return null;
        }

        ApiGatewayHandler.UrlResolver urlResolver = urlPath -> urlPath;

        final Map<String, Object> ctx = new HashMap<>();
        ctx.put("account", account);
        ctx.put("attributes", Attribute.values());
        ctx.put("character", character);
        ctx.put("url", urlResolver);
        ctx.put(
                "HarmLevel",
                Arrays.stream(HarmLevel.values()).collect(Collectors.toMap(HarmLevel::name, Function.identity())));
        ctx.put("Trauma", Arrays.stream(Trauma.values()).collect(Collectors.toMap(Enum::name, Function.identity())));

        Optional.ofNullable(character.campaignIdentifier())
                .map(templateService.campaignService().fetch()::withIdentifier)
                .map(CampaignRetrievalExecutor::get)
                .ifPresent(campaign -> ctx.put("campaign", campaign));

        final String html = templateService.parse("character-sheet", ctx);
        return ApiGatewayProxyResponseEventBuilder.newInstance()
                .withStatusCode(200)
                .withHeader("Cache-control", "private, no-cache, no-store, max-age=0, must-revalidate")
                .withHtml(html);
    }
}
