package com.xenosnowfox.forgedbythefox.lambda.authentication;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.xenosnowfox.forgedbythefox.library.models.account.AccountName;
import com.xenosnowfox.forgedbythefox.library.models.identity.Identity;
import com.xenosnowfox.forgedbythefox.library.models.identity.IdentityIdentifier;
import com.xenosnowfox.forgedbythefox.library.models.identity.IdentityProvider;
import com.xenosnowfox.forgedbythefox.library.models.session.Session;
import com.xenosnowfox.forgedbythefox.services.accountmanagement.AccountManagementService;
import com.xenosnowfox.forgedbythefox.services.identitymanagement.IdentityManagementService;
import com.xenosnowfox.forgedbythefox.services.sessionmanagement.SessionManagementService;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.apache.http.client.utils.URIBuilder;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

public class ApiGatewayHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .findAndRegisterModules()
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'"));

    private static final String GOOGLE_AUTH_URI = "https://accounts.google.com/o/oauth2/auth";

    private static final String GOOGLE_TOKEN_URI = "https://oauth2.googleapis.com/token";

    private final AccountManagementService accountManagementService = new AccountManagementService();

    private final IdentityManagementService identityManagementService = new IdentityManagementService();

    private final SessionManagementService sessionManagementService = new SessionManagementService();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent event, final Context context) {
        return this.handleAuthentication(event)
                .or(() -> this.redirectToGoogleAuth(event))
                .orElseGet(this::get500Response);
    }

    private APIGatewayProxyResponseEvent get500Response() {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setIsBase64Encoded(false);
        response.setStatusCode(200);
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html");
        headers.put("Cache-control", "private, no-cache, no-store, max-age=0, must-revalidate");
        response.setHeaders(headers);
        response.setStatusCode(500);
        response.setBody("Internal Server Error");
        return response;
    }

    private GoogleOAuth2Credentials fetchOAuth2Credentials(final APIGatewayProxyRequestEvent event) {
        // Create a Secrets Manager client
        SecretsManagerClient client = SecretsManagerClient.builder().build();

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(event.getStageVariables().get("GOOGLE_OAUTH2_CREDENTIALS_SECRET_NAME"))
                .build();

        GetSecretValueResponse getSecretValueResponse = client.getSecretValue(getSecretValueRequest);

        String secret = getSecretValueResponse.secretString();

        try {
            return OBJECT_MAPPER.readValue(secret, GoogleOAuth2Credentials.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<APIGatewayProxyResponseEvent> redirectToGoogleAuth(final APIGatewayProxyRequestEvent event) {

        if (!event.getHttpMethod().equalsIgnoreCase("GET")) {
            return Optional.empty();
        }

        // fetch google properties
        final GoogleOAuth2Credentials oAuth2Credentials = this.fetchOAuth2Credentials(event);

        final URIBuilder uriBuilder;
        try {
            uriBuilder = new URIBuilder(GOOGLE_AUTH_URI);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        uriBuilder.addParameter("client_id", oAuth2Credentials.clientId());
        uriBuilder.addParameter(
                "redirect_uri",
                "https://"
                        + event.getRequestContext().getDomainName()
                        + event.getRequestContext().getPath());
        uriBuilder.addParameter("response_type", "code");
        uriBuilder.addParameter(
                "scope",
                "https://www.googleapis.com/auth/userinfo.profile" + " https://www.googleapis.com/auth/userinfo.email");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setIsBase64Encoded(false);
        response.setStatusCode(307);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Cache-control", "private, no-cache, no-store, max-age=0, must-revalidate");
        try {
            headers.put("Location", uriBuilder.build().toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        response.setHeaders(headers);

        return Optional.of(response);
    }

    private Optional<APIGatewayProxyResponseEvent> handleAuthentication(final APIGatewayProxyRequestEvent event) {

        if (event.getQueryStringParameters() == null
                || event.getQueryStringParameters().isEmpty()) {
            return Optional.empty();
        }

        final String code = event.getQueryStringParameters().get("code");
        final String scope = event.getQueryStringParameters().get("scope");
        if (code == null || scope == null || code.isBlank() || scope.isBlank()) {
            return Optional.empty();
        }

        // Exchange OAuth token
        final Map<String, String> oAuthTokens = exchangeToken(event, code);
        if (oAuthTokens == null) {
            return Optional.empty();
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
        final String redirectUrl = ("https://"
                        + event.getRequestContext().getDomainName()
                        + event.getRequestContext().getPath())
                .split("/auth")[0];

        // success
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setIsBase64Encoded(false);
        response.setStatusCode(307);
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Set-Cookie", cookieValue);
        headers.put("Location", redirectUrl);
        headers.put("Cache-control", "private, no-cache, no-store, max-age=0, must-revalidate");
        response.setHeaders(headers);
        response.setBody("");
        return Optional.of(response);
    }

    private Map<String, String> exchangeToken(final APIGatewayProxyRequestEvent event, final String withCode) {

        // fetch google properties
        final GoogleOAuth2Credentials oAuth2Credentials = this.fetchOAuth2Credentials(event);

        final Map<String, String> payload = Map.ofEntries(
                Map.entry("client_id", oAuth2Credentials.clientId()),
                Map.entry("client_secret", oAuth2Credentials.clientSecret()),
                Map.entry("code", withCode),
                Map.entry("grant_type", "authorization_code"),
                Map.entry(
                        "redirect_uri",
                        "https://"
                                + event.getRequestContext().getDomainName()
                                + event.getRequestContext().getPath()));
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
