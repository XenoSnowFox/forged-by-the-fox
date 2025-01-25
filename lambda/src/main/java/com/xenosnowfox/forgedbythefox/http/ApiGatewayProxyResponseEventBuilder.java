package com.xenosnowfox.forgedbythefox.http;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiGatewayProxyResponseEventBuilder extends APIGatewayProxyResponseEvent {

    public static ApiGatewayProxyResponseEventBuilder newInstance() {
        return new ApiGatewayProxyResponseEventBuilder();
    }

    public ApiGatewayProxyResponseEventBuilder() {
        super();
        super.setHeaders(new HashMap<>());
    }

    @Override
    public ApiGatewayProxyResponseEventBuilder withStatusCode(final Integer statusCode) {
        super.setStatusCode(statusCode);
        return this;
    }

    public ApiGatewayProxyResponseEventBuilder withHeader(final String header, final String value) {
        this.getHeaders().put(header, value);
        return this;
    }

    @Override
    public ApiGatewayProxyResponseEventBuilder withHeaders(final Map<String, String> headers) {
        super.setHeaders(headers);
        return this;
    }

    @Override
    public ApiGatewayProxyResponseEventBuilder withMultiValueHeaders(
            final Map<String, List<String>> multiValueHeaders) {
        super.setMultiValueHeaders(multiValueHeaders);
        return this;
    }

    @Override
    public ApiGatewayProxyResponseEventBuilder withBody(final String body) {
        super.setBody(body);
        return this;
    }

    @Override
    public ApiGatewayProxyResponseEventBuilder withIsBase64Encoded(final Boolean isBase64Encoded) {
        super.setIsBase64Encoded(isBase64Encoded);
        return this;
    }

    public ApiGatewayProxyResponseEventBuilder withHtml(final String html) {
        return this.withHeader("Content-Type", "text/html").withBody(html);
    }

    public ApiGatewayProxyResponseEventBuilder withTemporaryRedirect(final String url) {
        return this.withStatusCode(307).withHeader("Location", url);
    }
}
