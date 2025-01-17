package com.xenosnowfox.forgedbythefox;

import com.xenosnowfox.forgedbythefox.routes.AuthenticationRoute;
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

        app.before(ctx -> ctx.req().setCharacterEncoding(StandardCharsets.UTF_8.toString()));
        app.before(ctx -> ctx.res().setCharacterEncoding(StandardCharsets.UTF_8.toString()));

        // PWA Manifest
        PwaManifestRoute.builder().build().applyTo(app::get, "/manifest.json");

        // Auth
        AuthenticationRoute.builder()
                .accountManagementService(templateService.accountService())
                .sessionManagementService(templateService.sessionService())
                .identityManagementService(identityManagementService)
                .build()
                .applyTo(app::get, "/auth")
                .applyTo(app::post, "/auth");

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
}
