package com.xenosnowfox.forgedbythefox.webserver;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.xenosnowfox.forgedbythefox.ApiGatewayHandler;
import io.javalin.Javalin;
import io.javalin.http.ContentType;
import io.javalin.http.Context;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class Application {

    private static ApiGatewayHandler API_GATEWAY_HANDLER = new ApiGatewayHandler();

    public static void main(final String[] args) {
        Javalin.create()
                .get("/styles/*", ctx -> {
                    Path p = Path.of("./build/modules/static-resources/" + ctx.path());
                    if (!p.toFile().isFile()) {
                        ctx.status(404);
                        return;
                    }

                    // stream, streamReader en buffer om file uit te lezen
                    FileInputStream fileInputStream = new FileInputStream(p.toFile());
                    InputStreamReader inputStreamReader =
                            new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
                    BufferedReader reader = new BufferedReader(inputStreamReader);

                    ctx.contentType(ContentType.CSS);
                    ctx.header("Cache-Control", "no-store");
                    ctx.result(reader.lines().collect(Collectors.joining()) + "\n");
                })
                .get("*", Application::handle)
                .put("*", Application::handle)
                .post("*", Application::handle)
                .patch("*", Application::handle)
                .delete("*", Application::handle)
                .head("*", Application::handle)
                .start(4567);
    }

    private static void handle(Context handler) {
        final APIGatewayProxyRequestEvent event = parseEvent(handler);
        final com.amazonaws.services.lambda.runtime.Context context = parseContext(handler);
        final APIGatewayProxyResponseEvent response = API_GATEWAY_HANDLER.handleRequest(event, context);

        handler.status(response.getStatusCode());
        handler.result(Optional.ofNullable(response.getBody()).orElse(""));
        response.getHeaders().forEach(handler::header);

        System.out.println(response.getHeaders());
    }

    private static APIGatewayProxyRequestEvent parseEvent(Context handler) {
        final APIGatewayProxyRequestEvent event = new APIGatewayProxyRequestEvent();

        event.setHttpMethod(handler.method().name());
        event.setPath(handler.path());
        event.setBody(handler.body());
        event.setHeaders(handler.headerMap());
        event.setIsBase64Encoded(false);
        //        event.setQueryStringParameters(handler.queryParamMap());
        event.setStageVariables(Map.of(
                "GOOGLE_OAUTH2_CREDENTIALS_SECRET_NAME",
                "credentials/oauth2/google",
                "BUGSNAG_CREDENTIALS_SECRET_NAME",
                "credentials/api/bugsnag"));
        event.setRequestContext(parseRequestContext(handler));
        event.setPathParameters(new HashMap<>());
        event.setQueryStringParameters(handler.queryParamMap().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey, k -> k.getValue().stream().findAny().orElse(""))));
        event.setMultiValueQueryStringParameters(handler.queryParamMap());

        return event;
    }

    private static APIGatewayProxyRequestEvent.ProxyRequestContext parseRequestContext(Context handler) {
        URI domain = URI.create(handler.url());

        final List<Integer> deaultPorts = List.of(80, 443);

        final APIGatewayProxyRequestEvent.ProxyRequestContext context =
                new APIGatewayProxyRequestEvent.ProxyRequestContext();
        context.setAccountId("");
        context.setRequestId(UUID.randomUUID().toString());
        context.setPath(handler.path());
        context.setHttpMethod(handler.method().name());
        context.setDomainName(domain.getHost()
                + Optional.of(domain.getPort())
                        .filter(p -> !deaultPorts.contains(p))
                        .map(p -> ":" + p)
                        .orElse(""));
        context.setDomainPrefix("");
        context.setStage("");

        return context;
    }

    private static com.amazonaws.services.lambda.runtime.Context parseContext(Context handler) {
        return new com.amazonaws.services.lambda.runtime.Context() {

            @Override
            public String getAwsRequestId() {
                return "";
            }

            @Override
            public String getLogGroupName() {
                return "";
            }

            @Override
            public String getLogStreamName() {
                return "";
            }

            @Override
            public String getFunctionName() {
                return "";
            }

            @Override
            public String getFunctionVersion() {
                return "";
            }

            @Override
            public String getInvokedFunctionArn() {
                return "";
            }

            @Override
            public CognitoIdentity getIdentity() {
                return null;
            }

            @Override
            public ClientContext getClientContext() {
                return null;
            }

            @Override
            public int getRemainingTimeInMillis() {
                return 0;
            }

            @Override
            public int getMemoryLimitInMB() {
                return 0;
            }

            @Override
            public LambdaLogger getLogger() {
                return null;
            }
        };
    }
}
