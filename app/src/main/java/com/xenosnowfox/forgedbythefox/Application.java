package com.xenosnowfox.forgedbythefox;

import com.xenosnowfox.forgedbythefox.routes.CampaignCharactersRoute;
import com.xenosnowfox.forgedbythefox.routes.CampaignFactionsRoute;
import com.xenosnowfox.forgedbythefox.routes.CharacterSheetRoute;
import com.xenosnowfox.forgedbythefox.routes.HomepageRoute;
import com.xenosnowfox.forgedbythefox.routes.PwaManifestRoute;
import com.xenosnowfox.forgedbythefox.routes.fragment.CampaignFactionDisplayFragment;
import com.xenosnowfox.forgedbythefox.routes.fragment.CharacterAbilitiesDisplayFragment;
import com.xenosnowfox.forgedbythefox.routes.fragment.CharacterDetailsDisplayFragment;
import com.xenosnowfox.forgedbythefox.routes.fragment.CharacterHarmDisplayFragment;
import com.xenosnowfox.forgedbythefox.routes.fragment.CharacterItemDisplayFragment;
import com.xenosnowfox.forgedbythefox.routes.fragment.CharacterStressDisplayFragment;
import com.xenosnowfox.forgedbythefox.service.account.AccountManagementService;
import com.xenosnowfox.forgedbythefox.service.campaign.CampaignService;
import com.xenosnowfox.forgedbythefox.service.character.CharacterManagementService;
import com.xenosnowfox.forgedbythefox.service.faction.FactionManagementService;
import com.xenosnowfox.forgedbythefox.service.identity.IdentityManagementService;
import com.xenosnowfox.forgedbythefox.service.session.SessionManagementService;
import com.xenosnowfox.forgedbythefox.service.template.TemplateService;
import io.javalin.Javalin;
import io.javalin.http.ContentType;
import io.javalin.http.Context;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Locale;
import java.util.stream.Collectors;

public class Application {

    //    private static ApiGatewayHandler API_GATEWAY_HANDLER = new ApiGatewayHandler();

    private static void staticResourceHandler(Context ctx) {
        ctx.header("Cache-Control", "no-store");

        Path p = Path.of("./build/modules/static-resources/" + ctx.path());
        if (!p.toFile().isFile()) {
            ctx.status(404);
            return;
        }

        if (ctx.path().toLowerCase(Locale.ROOT).endsWith(".css")) {
            ctx.contentType(ContentType.CSS);
        }
        if (ctx.path().toLowerCase(Locale.ROOT).endsWith(".js")) {
            ctx.contentType(ContentType.JAVASCRIPT);
        }
        if (ctx.path().toLowerCase(Locale.ROOT).endsWith(".json")) {
            ctx.contentType(ContentType.JSON);
        }
        if (ctx.path().toLowerCase(Locale.ROOT).endsWith(".png")) {
            ctx.contentType(ContentType.IMAGE_PNG);
        }
        if (ctx.path().toLowerCase(Locale.ROOT).endsWith(".ico")) {
            ctx.contentType("image/x-icon");
        }

        try (FileInputStream fileInputStream = new FileInputStream(p.toFile());
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(inputStreamReader); ) {

            ctx.result(reader.lines().collect(Collectors.joining()) + "\n");
        } catch (IOException ex) {
            ctx.status(404);
        }
    }

    public static void registerStaticResources(final Javalin app) {
        // Static Resources
        app.get("/styles/*", Application::staticResourceHandler)
                .get("/scripts/*", Application::staticResourceHandler)
                .get("/images/*", Application::staticResourceHandler)
                .get("/favicon.ico", Application::staticResourceHandler);
    }

    public static void registerRoutes(final Javalin app) {

        final IdentityManagementService identityManagementService = new IdentityManagementService();

        final TemplateService templateService = TemplateService.builder()
                .templateEngine(TemplateService.TEMPLATE_ENGINE)
                .accountService(new AccountManagementService())
                .campaignService(new CampaignService())
                .characterService(new CharacterManagementService())
                .factionService(new FactionManagementService())
                .sessionService(new SessionManagementService())
                .build();

        // PWA Manifest
        PwaManifestRoute.builder().build().applyTo(app::get, "/manifest.json");

        // Homepage/Dashboard
        HomepageRoute.builder()
                .templateService(templateService)
                .build()
                .applyTo(app::get, "")
                .applyTo(app::post, "");

        // Main Character Sheet
        CharacterSheetRoute.builder()
                .templateService(templateService)
                .build()
                .applyTo(app::get, "/characters/{character}");

        // Campaign Factions
        CampaignFactionsRoute.builder()
                .templateService(templateService)
                .build()
                .applyTo(app::get, "campaigns/{campaign}/factions");

        // Campaign Character List
        CampaignCharactersRoute.builder()
                .templateService(templateService)
                .build()
                .applyTo(app::get, "campaigns/{campaign}/characters");

        // Fragment: Campaign Faction List
        CampaignFactionDisplayFragment.builder()
                .templateService(templateService)
                .build()
                .applyTo(app::get, "fragments/campaigns/{campaign}/factions");

        // Fragment: Character Abilities
        CharacterAbilitiesDisplayFragment.builder()
                .templateService(templateService)
                .build()
                .applyTo(app::get, "fragments/characters/{character}/abilities")
                .applyTo(app::post, "fragments/characters/{character}/abilities");

        // Fragment: Character Details
        CharacterDetailsDisplayFragment.builder()
                .templateService(templateService)
                .build()
                .applyTo(app::get, "fragments/characters/{character}/details")
                .applyTo(app::post, "fragments/characters/{character}/details");

        // Fragment: Character Harm
        CharacterHarmDisplayFragment.builder()
                .templateService(templateService)
                .build()
                .applyTo(app::get, "fragments/characters/{character}/harm")
                .applyTo(app::post, "fragments/characters/{character}/harm");

        // Fragment: Character Items & Load
        CharacterItemDisplayFragment.builder()
                .templateService(templateService)
                .build()
                .applyTo(app::get, "fragments/characters/{character}/items")
                .applyTo(app::post, "fragments/characters/{character}/items");

        // Fragment: Character Stress
        CharacterStressDisplayFragment.builder()
                .templateService(templateService)
                .build()
                .applyTo(app::get, "fragments/characters/{character}/stress")
                .applyTo(app::post, "fragments/characters/{character}/stress");
    }

    public static void main(final String[] args) {
        final Javalin app = Javalin.create();
        Application.registerStaticResources(app);
        Application.registerRoutes(app);
        app.start(4567);
    }

    //    private static void handle(Context handler) {
    //        final APIGatewayProxyRequestEvent event = parseEvent(handler);
    //        final com.amazonaws.services.lambda.runtime.Context context = parseContext(handler);
    //        final APIGatewayProxyResponseEvent response = API_GATEWAY_HANDLER.handleRequest(event, context);
    //
    //        handler.status(response.getStatusCode());
    //        handler.result(Optional.ofNullable(response.getBody()).orElse(""));
    //        response.getHeaders().forEach(handler::header);
    //    }

    //    private static APIGatewayProxyRequestEvent parseEvent(Context handler) {
    //        final APIGatewayProxyRequestEvent event = new APIGatewayProxyRequestEvent();
    //
    //        event.setHttpMethod(handler.method().name());
    //        event.setPath(handler.path());
    //        event.setBody(handler.body());
    //        event.setHeaders(handler.headerMap());
    //        event.setIsBase64Encoded(false);
    //        //        event.setQueryStringParameters(handler.queryParamMap());
    //        event.setStageVariables(Map.of(
    //                "GOOGLE_OAUTH2_CREDENTIALS_SECRET_NAME",
    //                "credentials/oauth2/google",
    //                "BUGSNAG_CREDENTIALS_SECRET_NAME",
    //                "credentials/api/bugsnag"));
    //        event.setRequestContext(parseRequestContext(handler));
    //        event.setPathParameters(new HashMap<>());
    //        event.setQueryStringParameters(handler.queryParamMap().entrySet().stream()
    //                .collect(Collectors.toMap(
    //                        Map.Entry::getKey, k -> k.getValue().stream().findAny().orElse(""))));
    //        event.setMultiValueQueryStringParameters(handler.queryParamMap());
    //
    //        return event;
    //    }

    //    private static APIGatewayProxyRequestEvent.ProxyRequestContext parseRequestContext(Context handler) {
    //        URI domain = URI.create(handler.url());
    //
    //        final List<Integer> deaultPorts = List.of(80, 443);
    //
    //        final APIGatewayProxyRequestEvent.ProxyRequestContext context =
    //                new APIGatewayProxyRequestEvent.ProxyRequestContext();
    //        context.setAccountId("000000000");
    //        context.setRequestId(UUID.randomUUID().toString());
    //        context.setPath(handler.path());
    //        context.setHttpMethod(handler.method().name());
    //        context.setDomainName(domain.getHost()
    //                + Optional.of(domain.getPort())
    //                        .filter(p -> !deaultPorts.contains(p))
    //                        .map(p -> ":" + p)
    //                        .orElse(""));
    //        context.setDomainPrefix("");
    //        context.setStage("");
    //
    //        return context;
    //    }

    //    private static com.amazonaws.services.lambda.runtime.Context parseContext(Context handler) {
    //        return new com.amazonaws.services.lambda.runtime.Context() {
    //
    //            @Override
    //            public String getAwsRequestId() {
    //                return "";
    //            }
    //
    //            @Override
    //            public String getLogGroupName() {
    //                return "";
    //            }
    //
    //            @Override
    //            public String getLogStreamName() {
    //                return "";
    //            }
    //
    //            @Override
    //            public String getFunctionName() {
    //                return "";
    //            }
    //
    //            @Override
    //            public String getFunctionVersion() {
    //                return "";
    //            }
    //
    //            @Override
    //            public String getInvokedFunctionArn() {
    //                return "";
    //            }
    //
    //            @Override
    //            public CognitoIdentity getIdentity() {
    //                return null;
    //            }
    //
    //            @Override
    //            public ClientContext getClientContext() {
    //                return null;
    //            }
    //
    //            @Override
    //            public int getRemainingTimeInMillis() {
    //                return 0;
    //            }
    //
    //            @Override
    //            public int getMemoryLimitInMB() {
    //                return 0;
    //            }
    //
    //            @Override
    //            public LambdaLogger getLogger() {
    //                return null;
    //            }
    //        };
    //    }
}
