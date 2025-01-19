package com.xenosnowfox.forgedbythefox.routes;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Builder;

@Builder(builderClassName = "Builder")
public record PwaManifestRoute() implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .findAndRegisterModules()
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'"));

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent event, final Context context) {
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

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Cache-control", "private, no-cache, no-store, max-age=0, must-revalidate");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setIsBase64Encoded(false);
        response.setStatusCode(200);
        response.setHeaders(headers);
        response.setBody(json);
        return response;
    }
}
