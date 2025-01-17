package com.xenosnowfox.forgedbythefox;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.AwsProxyExceptionHandler;
import com.amazonaws.serverless.proxy.AwsProxySecurityContextWriter;
import com.amazonaws.serverless.proxy.ExceptionHandler;
import com.amazonaws.serverless.proxy.RequestReader;
import com.amazonaws.serverless.proxy.ResponseWriter;
import com.amazonaws.serverless.proxy.SecurityContextWriter;
import com.amazonaws.serverless.proxy.internal.servlet.AwsHttpServletRequest;
import com.amazonaws.serverless.proxy.internal.servlet.AwsHttpServletResponse;
import com.amazonaws.serverless.proxy.internal.servlet.AwsLambdaServletContainerHandler;
import com.amazonaws.serverless.proxy.internal.servlet.AwsProxyHttpServletRequestReader;
import com.amazonaws.serverless.proxy.internal.servlet.AwsProxyHttpServletResponseWriter;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import io.javalin.Javalin;
import jakarta.servlet.Servlet;
import jakarta.servlet.http.HttpServletRequest;
import java.util.concurrent.CountDownLatch;

public class JavalinLambdaContainerHandler<REQUEST, RESPONSE>
        extends AwsLambdaServletContainerHandler<REQUEST, RESPONSE, HttpServletRequest, AwsHttpServletResponse> {

    public static JavalinLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> getJavalinAwsHandler(
            final Javalin app) throws ContainerInitializationException {
        JavalinLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> newHandler =
                new JavalinLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse>(
                        AwsProxyRequest.class,
                        AwsProxyResponse.class,
                        new AwsProxyHttpServletRequestReader(),
                        new AwsProxyHttpServletResponseWriter(),
                        new AwsProxySecurityContextWriter(),
                        new AwsProxyExceptionHandler(),
                        app.javalinServlet());
        newHandler.initialize();
        return newHandler;
    }

    private final Servlet javalinServlet;

    public JavalinLambdaContainerHandler(
            final Class<REQUEST> requestClass,
            final Class<RESPONSE> responseClass,
            final RequestReader<REQUEST, HttpServletRequest> requestReader,
            final ResponseWriter<AwsHttpServletResponse, RESPONSE> responseWriter,
            final SecurityContextWriter<REQUEST> securityContextWriter,
            final ExceptionHandler<RESPONSE> exceptionHandler,
            final Servlet javalinServlet) {
        super(requestClass, responseClass, requestReader, responseWriter, securityContextWriter, exceptionHandler);
        this.javalinServlet = javalinServlet;
        getServletContext().addServlet("javalin", javalinServlet);
    }

    @Override
    protected AwsHttpServletResponse getContainerResponse(
            final HttpServletRequest awsHttpServletRequest, final CountDownLatch countDownLatch) {
        return new AwsHttpServletResponse(awsHttpServletRequest, countDownLatch);
    }

    @Override
    protected void handleRequest(
            final HttpServletRequest httpServletRequest,
            final AwsHttpServletResponse httpServletResponse,
            final Context context)
            throws Exception {
        if (httpServletRequest instanceof AwsHttpServletRequest awsHttpServletRequest) {
            awsHttpServletRequest.setServletContext(getServletContext());
        }
        doFilter(httpServletRequest, httpServletResponse, javalinServlet);
    }
}
