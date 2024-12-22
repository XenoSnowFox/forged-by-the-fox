package com.xenosnowfox.forgedbythefox;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.internal.LambdaContainerHandler;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spark.SparkLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import spark.Spark;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.logging.Logger;

/** AWS Lambda entry point. */
public class LambdaHandler implements RequestStreamHandler {

    private static final Logger LOG = Logger.getLogger(LambdaHandler.class.getName());

    /** Spark/Lambda Container. */
    private static final SparkLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> LAMBDA_CONTAINER_HANDLER;

    /** Random identifier for this running instance. */
    private static final UUID INSTANCE_UUID = UUID.randomUUID();

    private static boolean coldStart = true;

    static {
        long startTime = System.currentTimeMillis();

        try {
            LOG.info("Instance UUID: " + INSTANCE_UUID);
            LambdaContainerHandler.getContainerConfig().setDefaultContentCharset("UTF-8");
            LAMBDA_CONTAINER_HANDLER = SparkLambdaContainerHandler.getAwsProxyHandler();

            // start the application
            final Application application = new Application();
            application.run();

            Spark.awaitInitialization();
        } catch (ContainerInitializationException e) {
            // if we fail here. We re-throw the exception to force another cold start
            //			ExceptionHandler.notify(e);
            throw new RuntimeException("Could not initialize Spark container", e);
        }

        LOG.info("Spark Initialization Duration: " + (System.currentTimeMillis() - startTime) + " milliseconds.");
    }

    @Override
    public final void handleRequest(
            final InputStream inputStream, final OutputStream outputStream, final Context context) throws IOException {
        long startTime = System.currentTimeMillis();
        LAMBDA_CONTAINER_HANDLER.proxyStream(inputStream, outputStream, context);
        LOG.info((LambdaHandler.coldStart ? "[COLD START]" : "")
                + "Lambda Request Duration: "
                + (System.currentTimeMillis() - startTime)
                + " milliseconds.");
        LambdaHandler.coldStart = false;
    }
}
