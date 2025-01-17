package com.xenosnowfox.forgedbythefox.routes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.javalin.http.ContentType;
import io.javalin.http.HttpStatus;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import org.jetbrains.annotations.NotNull;

@Builder(builderClassName = "Builder")
public record PwaManifestRoute() implements JavalinRoute {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .findAndRegisterModules()
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'"));

    @Override
    public void handle(@NotNull final io.javalin.http.Context ctx) throws Exception {
        final Map<String, Object> manifestData = new HashMap<>();
        manifestData.put("short_name", "Forged by the Fox");
        manifestData.put("name", "Forged by the Fox");
        manifestData.put("id", "/?source=pwa");
        manifestData.put("start_url", "/?source=pwa");
        manifestData.put("background_color", "#212529");
        manifestData.put("display", "standalone");
        manifestData.put("scope", "/");
        manifestData.put("theme_color", "#f48c06");
        manifestData.put(
                "description",
                "Forged by the Fox is a fan-made online character sheet for your Blades in the Dark, Scum & Villainy and potentially other games built upon the Forged in the Dark system.");
        manifestData.put(
                "icons",
                List.of(
                        Map.of("src", "/images/icon-192.png", "type", "image/png", "sizes", "192x192"),
                        Map.of("src", "/images/icon-200.png", "type", "image/png", "sizes", "200x200"),
                        Map.of("src", "/images/icon-512.png", "type", "image/png", "sizes", "512x512"),
                        Map.of("src", "/images/icon-tall-200.png", "type", "image/png", "sizes", "200x360"),
                        Map.of("src", "/images/icon-tall-360.png", "type", "image/png", "sizes", "360x648")));

        String json;
        try {
            json = OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(manifestData);
        } catch (JsonProcessingException e) {
            json = "{}";
        }

        ctx.status(HttpStatus.OK)
                .contentType(ContentType.JSON)
                .header("Cache-control", "private, no-cache, no-store, max-age=0, must-revalidate")
                .result(json);
    }
}
