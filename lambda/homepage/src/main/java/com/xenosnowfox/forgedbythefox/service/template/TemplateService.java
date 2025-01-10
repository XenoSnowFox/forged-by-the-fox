package com.xenosnowfox.forgedbythefox.service.template;

import com.xenosnowfox.forgedbythefox.service.campaign.CampaignService;
import com.xenosnowfox.forgedbythefox.service.character.CharacterManagementService;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import lombok.Builder;
import lombok.NonNull;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Builder(builderClassName = "Builder", toBuilder = true)
public record TemplateService(
        @NonNull ITemplateEngine templateEngine,
        @NonNull CampaignService campaignService,
        @NonNull CharacterManagementService characterService) {

    public static final ITemplateEngine TEMPLATE_ENGINE = buildTemplateEngine();

    private static ITemplateEngine buildTemplateEngine() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

        //  HTML is the default mode, but we will set it anyway for better understanding of code
        templateResolver.setTemplateMode(TemplateMode.HTML);

        // This will convert "home" to "/WEB-INF/templates/home.html"
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");

        // Set template cache TTL to 1 hour. If not set, entries would live in cache until expelled by
        // LRU
        templateResolver.setCacheTTLMs(3600000L);
        templateResolver.setCacheable(true);

        final TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }

    public String parse(final String withTemplate, final Map<String, Object> withContextData) {
        final Map<String, Object> serviceMap = new HashMap<>();
        serviceMap.put("campaigns", this.campaignService);
        serviceMap.put("characters", this.characterService);

        final Map<String, Object> contextData = new HashMap<>();
        contextData.put("ctx", withContextData);
        contextData.put("service", serviceMap);

        final IContext context = new Context(Locale.ENGLISH, contextData);
        return TemplateService.TEMPLATE_ENGINE.process(withTemplate, context);
    }

    public String parse(
            final String withTemplate, final Set<String> selectors, final Map<String, Object> withContextData) {
        final Map<String, Object> serviceMap = new HashMap<>();
        serviceMap.put("campaigns", this.campaignService);
        serviceMap.put("characters", this.characterService);

        final Map<String, Object> contextData = new HashMap<>();
        contextData.put("ctx", withContextData);
        contextData.put("service", serviceMap);

        final IContext context = new Context(Locale.ENGLISH, contextData);
        return TemplateService.TEMPLATE_ENGINE.process(withTemplate, selectors, context);
    }
}
