package com.xenosnowfox.forgedbythefox.lambda.authentication;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Util {

    private static final ObjectMapper OBJECT_MAPPER =
            new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

    public static void logEnvironment(Object event, Context context) {
        try {
            LambdaLogger logger = context.getLogger();
            // log execution details
            logger.log("ENVIRONMENT VARIABLES: "
                    + OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(System.getenv()));
            logger.log(
                    "CONTEXT: " + OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(context));
            // log event details
            logger.log(
                    "EVENT: " + OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(event));
            logger.log("EVENT TYPE: " + event.getClass());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
