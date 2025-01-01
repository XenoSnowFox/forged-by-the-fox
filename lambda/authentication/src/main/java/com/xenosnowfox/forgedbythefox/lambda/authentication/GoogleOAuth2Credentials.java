package com.xenosnowfox.forgedbythefox.lambda.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleOAuth2Credentials(
        @JsonProperty("ClientId") String clientId, @JsonProperty("ClientSecret") String clientSecret) {}
