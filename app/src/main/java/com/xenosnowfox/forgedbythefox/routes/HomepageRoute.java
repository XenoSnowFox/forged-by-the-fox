package com.xenosnowfox.forgedbythefox.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.xenosnowfox.forgedbythefox.ApiGatewayHandler;
import com.xenosnowfox.forgedbythefox.AuthenticatedRequestHandler;
import com.xenosnowfox.forgedbythefox.models.Playbook;
import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.character.Character;
import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.service.character.CreateCharacterRequest;
import com.xenosnowfox.forgedbythefox.service.template.TemplateService;
import io.javalin.http.ContentType;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpStatus;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

@lombok.Builder(builderClassName = "Builder")
public record HomepageRoute(TemplateService templateService) implements AuthenticatedRequestHandler {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .findAndRegisterModules()
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'"));

    @Override
    public void handleRequest(
            @NotNull final io.javalin.http.Context context, final Account account, final Session session) {

        ApiGatewayHandler.UrlResolver urlResolver = urlPath -> urlPath;

        if (context.method() == HandlerType.POST) {
            createNewCharacter(context, account, urlResolver);
            return;
        }

        final Map<String, Object> contextData = new HashMap<>();
        contextData.put("url", urlResolver);
        contextData.put("account", account);
        contextData.put("session", session);

        // retrieve list of all characters
        final Set<Character> characterSet =
                templateService.characterService().query(b -> b.accountIdentifier(account.identifier()));
        contextData.put("characters", characterSet);

        final String html = templateService.parse("dashboard", contextData);

        context.status(HttpStatus.OK)
                .contentType(ContentType.HTML)
                .header("Cache-Control", "private, no-cache, no-store, max-age=0, must-revalidate")
                .result(html);
    }

    @Override
    public void handleUnauthenticatedRequest(@NotNull final io.javalin.http.Context context) {
        if (context.method() != HandlerType.GET) {
            context.redirect(context.path(), HttpStatus.TEMPORARY_REDIRECT);
            return;
        }

        ApiGatewayHandler.UrlResolver urlResolver = urlPath -> urlPath;

        final Map<String, Object> contextData = new HashMap<>();
        contextData.put("url", urlResolver);

        final String html = templateService.parse("homepage", contextData);
        context.status(200).contentType(ContentType.HTML).result(html);
    }

    private void createNewCharacter(
            @NotNull final io.javalin.http.Context context,
            Account account,
            ApiGatewayHandler.UrlResolver urlResolver) {
        CreateCharacterRequest createCharacterRequest = CreateCharacterRequest.builder()
                .accountIdentifier(account.identifier())
                .playbook(Playbook.valueOf(context.formParam("playbook")))
                .build();

        final Character character = templateService.characterService().create(createCharacterRequest);

        // redirect to character sheet
        context.redirect(
                urlResolver.resolve("/characters/" + character.identifier().value()), HttpStatus.TEMPORARY_REDIRECT);
    }
}
