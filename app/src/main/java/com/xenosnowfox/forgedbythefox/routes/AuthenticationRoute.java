package com.xenosnowfox.forgedbythefox.routes;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.xenosnowfox.forgedbythefox.models.account.AccountName;
import com.xenosnowfox.forgedbythefox.models.identity.Identity;
import com.xenosnowfox.forgedbythefox.models.identity.IdentityIdentifier;
import com.xenosnowfox.forgedbythefox.models.identity.IdentityProvider;
import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.service.account.AccountManagementService;
import com.xenosnowfox.forgedbythefox.service.identity.IdentityManagementService;
import com.xenosnowfox.forgedbythefox.service.session.SessionManagementService;
import io.javalin.http.ContentType;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpStatus;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import lombok.Builder;
import lombok.NonNull;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@Builder(builderClassName = "Builder")
public record AuthenticationRoute(
        @NonNull AccountManagementService accountManagementService,
        @NonNull IdentityManagementService identityManagementService,
        @NonNull SessionManagementService sessionManagementService)
        implements JavalinRoute {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .findAndRegisterModules()
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'"));

    private static final String GOOGLE_AUTH_URI = "https://accounts.google.com/o/oauth2/auth";

    private static final String GOOGLE_TOKEN_URI = "https://oauth2.googleapis.com/token";

    @Override
    public void handle(@NotNull final io.javalin.http.Context ctx) throws Exception {

        final String code = ctx.queryParam("code");
        final String scope = ctx.queryParam("scope");
        if (code == null || scope == null || code.isBlank() || scope.isBlank()) {
            this.redirectToGoogleAuth(ctx);
            return;
        }

        // Exchange OAuth token
        final Map<String, String> oAuthTokens = exchangeToken(ctx, code);
        if (oAuthTokens == null) {
            this.redirectToGoogleAuth(ctx);
            return;
        }

        // Retrieve Profile
        final Map<String, String> profile = fetchGoogleProfile(oAuthTokens.get("access_token"));

        final IdentityIdentifier identityIdentifier = IdentityIdentifier.builder()
                .provider(IdentityProvider.GOOGLE)
                .value(profile.get("id"))
                .build();

        // Update or Create Identity and/or Account
        final Identity identity = identityManagementService
                .update(builder -> builder.identifier(identityIdentifier)
                        .givenName(profile.get("given_name"))
                        .familyName(profile.get("family_name"))
                        .profilePictureUrl(profile.get("picture")))
                .or(() ->
                        // create a new account
                        accountManagementService
                                .create(builder -> builder.name(new AccountName((profile.get("given_name")
                                                        + " "
                                                        + profile.get("family_name")
                                                        + " ".repeat(AccountName.MAX_LENGTH))
                                                .substring(0, AccountName.MAX_LENGTH)
                                                .trim()))
                                        .profilePictureUrl(profile.get("picture")))
                                .flatMap(account -> identityManagementService.create(
                                        builder -> builder.accountIdentifier(account.identifier())
                                                .identifier(identityIdentifier)
                                                .givenName(profile.get("given_name"))
                                                .familyName(profile.get("family_name"))
                                                .profilePictureUrl(profile.get("picture")))))
                .orElseThrow();

        // create a new session
        final Session session = sessionManagementService
                .create(builder -> builder.accountIdentifier(identity.accountIdentifier()))
                .orElseThrow();
        final String cookieValue = "session="
                + session.identifier().value()
                + "; Secure; HttpOnly; SameSite=Strict; Path=/; Expires="
                + DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
                        .withZone(ZoneId.of("GMT"))
                        .format(session.timestampExpires());

        // redirect url
        final String redirectUrl = ctx.fullUrl().split("/auth")[0];

        // success
        ctx.status(HttpStatus.OK)
                .contentType(ContentType.HTML)
                .header("Cache-control", "private, no-cache, no-store, max-age=0, must-revalidate")
                .header("Set-Cookie", cookieValue)
                .result("<!DOCTYPE html><html><body><script type='text/javascript'>window.location = '" + redirectUrl
                        + "';</script></body></html>");
    }

    public record GoogleOAuth2Credentials(
            @JsonProperty("ClientId") String clientId, @JsonProperty("ClientSecret") String clientSecret) {}

    private void get500Response(@NotNull final io.javalin.http.Context ctx) {
        ctx.status(500)
                .contentType(ContentType.HTML)
                .header("Cache-control", "private, no-cache, no-store, max-age=0, must-revalidate")
                .result("<p>Internal Server Error</p>");
    }

    private GoogleOAuth2Credentials fetchOAuth2Credentials(@NotNull final io.javalin.http.Context ctx) {
        // Create a Secrets Manager client
        SecretsManagerClient client = SecretsManagerClient.builder().build();

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId("credentials/oauth2/google")
                .build();

        GetSecretValueResponse getSecretValueResponse = client.getSecretValue(getSecretValueRequest);

        String secret = getSecretValueResponse.secretString();

        try {
            return OBJECT_MAPPER.readValue(secret, GoogleOAuth2Credentials.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void redirectToGoogleAuth(@NotNull final io.javalin.http.Context ctx) {

        if (ctx.method() != HandlerType.GET) {
            this.get500Response(ctx);
            return;
        }

        // fetch google properties
        final GoogleOAuth2Credentials oAuth2Credentials = this.fetchOAuth2Credentials(ctx);

        final URIBuilder uriBuilder;
        try {
            uriBuilder = new URIBuilder(GOOGLE_AUTH_URI);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        uriBuilder.addParameter("client_id", oAuth2Credentials.clientId());
        uriBuilder.addParameter("redirect_uri", ctx.fullUrl().split("\\?")[0]);
        uriBuilder.addParameter("response_type", "code");
        uriBuilder.addParameter(
                "scope",
                "https://www.googleapis.com/auth/userinfo.profile" + " https://www.googleapis.com/auth/userinfo.email");

        final String redirectUrl;
        try {
            redirectUrl = uriBuilder.build().toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        ctx.status(HttpStatus.TEMPORARY_REDIRECT)
                .header("Cache-control", "private, no-cache, no-store, max-age=0, must-revalidate")
                .redirect(redirectUrl, HttpStatus.TEMPORARY_REDIRECT);
    }

    private Map<String, String> exchangeToken(@NotNull final io.javalin.http.Context ctx, final String withCode) {

        // fetch google properties
        final GoogleOAuth2Credentials oAuth2Credentials = this.fetchOAuth2Credentials(ctx);

        final Map<String, String> payload = Map.ofEntries(
                Map.entry("client_id", oAuth2Credentials.clientId()),
                Map.entry("client_secret", oAuth2Credentials.clientSecret()),
                Map.entry("code", withCode),
                Map.entry("grant_type", "authorization_code"),
                Map.entry("redirect_uri", ctx.fullUrl().split("\\?")[0]));
        final String json;
        try {
            json = OBJECT_MAPPER.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        final HttpRequest tokenRequest = HttpRequest.newBuilder()
                .uri(URI.create(GOOGLE_TOKEN_URI))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            final HttpResponse<String> response = httpClient.send(tokenRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 300) {
                // token exchange failed
                return null;
            }
            return OBJECT_MAPPER.readValue(response.body(), new TypeReference<>() {});
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, String> fetchGoogleProfile(final String withAccessToken) {
        final HttpRequest profileRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://www.googleapis.com/oauth2/v1/userinfo?alt=json"))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + withAccessToken)
                .GET()
                .build();

        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            final HttpResponse<String> profileResponse =
                    httpClient.send(profileRequest, HttpResponse.BodyHandlers.ofString());
            return OBJECT_MAPPER.readValue(profileResponse.body(), new TypeReference<>() {});
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
