package com.xenosnowfox.forgedbythefox;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.SecretValue;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apigateway.IntegrationResponse;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.apigateway.MethodOptions;
import software.amazon.awscdk.services.apigateway.MethodResponse;
import software.amazon.awscdk.services.apigateway.StageOptions;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.BillingMode;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.Policy;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.logs.RetentionDays;
import software.amazon.awscdk.services.secretsmanager.Secret;

@Accessors(chain = true)
@Getter
public class StackBuilder extends Stack {

    public static StackBuilder withScope(@NonNull final App withApplication) {
        return new StackBuilder(withApplication);
    }

    private static final SecretValue BLANK_SECRET_VALUE = SecretValue.unsafePlainText("");

    private final App app;

    public StackBuilder(@NonNull final App withApplication) {
        this.app = withApplication;
    }

    public Stack build() {
        final Stack stack = new Stack(
                this.app,
                "ForgedByTheFox",
                StackProps.builder()
                        .stackName("forged-by-the-fox")
                        .description("Web application to track and manage character sheets for Forged by the Dark"
                                + " systems.")
                        .build());

        Secret googleOAuth2CredentialsSecret = Secret.Builder.create(stack, "GoogleOAuth2Credentials")
                .secretName("credentials/oauth2/google")
                .removalPolicy(RemovalPolicy.RETAIN)
                .secretObjectValue(Map.of("ClientId", BLANK_SECRET_VALUE, "ClientSecret", BLANK_SECRET_VALUE))
                .build();

        Secret bugsnagApiCredentialsSecret = Secret.Builder.create(stack, "BugsnagApiKey")
                .secretName("credentials/api/bugsnag")
                .removalPolicy(RemovalPolicy.RETAIN)
                .secretObjectValue(Map.of("ApiKey", BLANK_SECRET_VALUE))
                .build();

        Table table = Table.Builder.create(stack, "Database")
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .removalPolicy(RemovalPolicy.RETAIN)
                .pointInTimeRecovery(true)
                .partitionKey(Attribute.builder()
                        .name("partition-key")
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name("sort-key")
                        .type(AttributeType.STRING)
                        .build())
                .tableName("forged-by-the-fox")
                .timeToLiveAttribute("ttl")
                .build();
        Policy databaseReadWritePolicy = Policy.Builder.create(stack, "DatabaseReadWritePolicy")
                .statements(List.of(PolicyStatement.Builder.create()
                        .resources(List.of(table.getTableArn()))
                        .effect(Effect.ALLOW)
                        .actions(List.of(
                                "dynamodb:BatchGetItem",
                                "dynamodb:BatchWriteItem",
                                "dynamodb:ConditionCheckItem",
                                "dynamodb:PutItem",
                                "dynamodb:DescribeTable",
                                "dynamodb:DeleteItem",
                                "dynamodb:GetItem",
                                "dynamodb:Scan",
                                "dynamodb:Query",
                                "dynamodb:UpdateItem"))
                        .build()))
                .build();

        Function homepageLambdaFunction = Function.Builder.create(stack, "HomepageLambda")
                .runtime(Runtime.JAVA_21)
                .code(Code.fromAsset("./build/modules/lambda-homepage/libs/forged-by-the-fox-homepage.jar"))
                .handler("com.xenosnowfox.forgedbythefox.lambda.homepage.ApiGatewayHandler::handleRequest")
                .memorySize(512)
                .timeout(Duration.minutes(5))
                .logRetention(RetentionDays.ONE_WEEK)
                .build();

        Function authenticationFunction = Function.Builder.create(stack, "AuthenticationLambda")
                .runtime(Runtime.JAVA_21)
                .code(Code.fromAsset("./build/modules/lambda-authentication/libs/forged-by-the-fox-authentication.jar"))
                .handler("com.xenosnowfox.forgedbythefox.lambda.authentication.ApiGatewayHandler::handleRequest")
                .memorySize(512)
                .timeout(Duration.minutes(5))
                .logRetention(RetentionDays.ONE_WEEK)
                .build();
        googleOAuth2CredentialsSecret.grantRead(Objects.requireNonNull(authenticationFunction.getRole()));
        authenticationFunction.getRole().attachInlinePolicy(databaseReadWritePolicy);

        final Map<String, String> stageVariables = Map.of(
                "GOOGLE_OAUTH2_CREDENTIALS_SECRET_NAME",
                googleOAuth2CredentialsSecret.getSecretName(),
                "BUGSNAG_CREDENTIALS_SECRET_NAME",
                bugsnagApiCredentialsSecret.getSecretName());

        final StageOptions stageOptions = StageOptions.builder()
                .stageName("production")
                .variables(stageVariables)
                .build();

        LambdaRestApi apiGateway = LambdaRestApi.Builder.create(stack, "ApiGateway")
                .restApiName("forged-by-the-fox:api-gateway")
                .handler(homepageLambdaFunction)
                .proxy(false)
                .deploy(true)
                .deployOptions(stageOptions)
                .build();

        apiGateway
                .getRoot()
                .addMethod(
                        "GET",
                        LambdaIntegration.Builder.create(homepageLambdaFunction)
                                .proxy(true)
                                .integrationResponses(List.of(IntegrationResponse.builder()
                                        .statusCode("200")
                                        .build()))
                                .build(),
                        MethodOptions.builder()
                                .methodResponses(List.of(MethodResponse.builder()
                                        .statusCode("200")
                                        .build()))
                                .build());

        apiGateway
                .getRoot()
                .addResource("auth")
                .addMethod(
                        "ANY",
                        LambdaIntegration.Builder.create(authenticationFunction)
                                .proxy(true)
                                .integrationResponses(List.of(IntegrationResponse.builder()
                                        .statusCode("200")
                                        .build()))
                                .build(),
                        MethodOptions.builder()
                                .methodResponses(List.of(MethodResponse.builder()
                                        .statusCode("200")
                                        .build()))
                                .build());

        return stack;
    }
}
