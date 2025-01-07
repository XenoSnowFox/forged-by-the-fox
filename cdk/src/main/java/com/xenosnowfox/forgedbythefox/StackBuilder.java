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
import software.amazon.awscdk.services.apigateway.AwsIntegration;
import software.amazon.awscdk.services.apigateway.IntegrationOptions;
import software.amazon.awscdk.services.apigateway.IntegrationResponse;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.apigateway.MethodOptions;
import software.amazon.awscdk.services.apigateway.MethodResponse;
import software.amazon.awscdk.services.apigateway.PassthroughBehavior;
import software.amazon.awscdk.services.apigateway.StageOptions;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.BillingMode;
import software.amazon.awscdk.services.dynamodb.GlobalSecondaryIndexProps;
import software.amazon.awscdk.services.dynamodb.ProjectionType;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.Policy;
import software.amazon.awscdk.services.iam.PolicyDocument;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.logs.RetentionDays;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.BucketAccessControl;
import software.amazon.awscdk.services.s3.deployment.BucketDeployment;
import software.amazon.awscdk.services.s3.deployment.Source;
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

        Attribute partitionKeyAttribute = Attribute.builder()
                .name("partition-key")
                .type(AttributeType.STRING)
                .build();
        Attribute sortKeyAttribute =
                Attribute.builder().name("sort-key").type(AttributeType.STRING).build();
        Attribute accountIdentifierAttribute = Attribute.builder()
                .name("account-identifier")
                .type(AttributeType.STRING)
                .build();
        Table table = Table.Builder.create(stack, "Database")
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .removalPolicy(RemovalPolicy.RETAIN)
                .pointInTimeRecovery(true)
                .partitionKey(partitionKeyAttribute)
                .sortKey(sortKeyAttribute)
                .tableName("forged-by-the-fox")
                .timeToLiveAttribute("ttl")
                .build();
        table.addGlobalSecondaryIndex(GlobalSecondaryIndexProps.builder()
                .indexName("documents-by-account")
                .partitionKey(accountIdentifierAttribute)
                .sortKey(sortKeyAttribute)
                .projectionType(ProjectionType.ALL)
                .build());
        Policy databaseReadWritePolicy = Policy.Builder.create(stack, "DatabaseReadWritePolicy")
                .statements(List.of(PolicyStatement.Builder.create()
                        .resources(List.of(table.getTableArn(), table.getTableArn() + "/*"))
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
                .handler("com.xenosnowfox.forgedbythefox.ApiGatewayHandler::handleRequest")
                .memorySize(512)
                .timeout(Duration.minutes(5))
                .logRetention(RetentionDays.ONE_WEEK)
                .build();
        googleOAuth2CredentialsSecret.grantRead(Objects.requireNonNull(homepageLambdaFunction.getRole()));
        homepageLambdaFunction.getRole().attachInlinePolicy(databaseReadWritePolicy);

        final Map<String, String> stageVariables = Map.of(
                "GOOGLE_OAUTH2_CREDENTIALS_SECRET_NAME",
                googleOAuth2CredentialsSecret.getSecretName(),
                "BUGSNAG_CREDENTIALS_SECRET_NAME",
                bugsnagApiCredentialsSecret.getSecretName());

        final StageOptions stageOptions = StageOptions.builder()
                .stageName("forged-by-the-fox")
                .variables(stageVariables)
                .build();

        LambdaRestApi apiGateway = LambdaRestApi.Builder.create(stack, "ApiGateway")
                .restApiName("forged-by-the-fox:api-gateway")
                .handler(homepageLambdaFunction)
                .proxy(true)
                .deploy(true)
                .deployOptions(stageOptions)
                .binaryMediaTypes(List.of(
                        "application/octet-stream", "image/png", "image/avif", "image/webp", "image/apng", "image/*"))
                .build();

        // Set up S3 bucket for storing static resources
        final Bucket bucket = Bucket.Builder.create(stack, "static-resources-s3-bucket")
                .bucketName("forged-by-the-fox-static-resources")
                .publicReadAccess(false)
                .accessControl(BucketAccessControl.PRIVATE)
                .removalPolicy(RemovalPolicy.DESTROY)
                .autoDeleteObjects(true)
                .build();
        Role role = Role.Builder.create(stack, "forged-by-the-fox-static-resources-s3-bucket-api-gateway-role")
                .assumedBy(ServicePrincipal.fromStaticServicePrincipleName("apigateway.amazonaws.com"))
                .inlinePolicies(Map.of(
                        "s3read",
                        PolicyDocument.Builder.create()
                                .statements(List.of(PolicyStatement.Builder.create()
                                        .actions(List.of("s3:GetObject"))
                                        .effect(Effect.ALLOW)
                                        .resources(List.of("*"))
                                        .build()))
                                .build()))
                .build();

        apiGateway
                .getRoot()
                .addResource("styles")
                .addResource("{object}")
                .addMethod(
                        "GET",
                        AwsIntegration.Builder.create()
                                .service("s3")
                                .integrationHttpMethod("GET")
                                .path("forged-by-the-fox-static-resources/styles/{object}")
                                .options(IntegrationOptions.builder()
                                        .requestParameters(
                                                Map.of("integration.request.path.object", "method.request.path.object"))
                                        .integrationResponses(List.of(IntegrationResponse.builder()
                                                .statusCode("200")
                                                .responseParameters(Map.of(
                                                        "method.response.header.Content-Type",
                                                        "integration.response.header.Content-Type"))
                                                .build()))
                                        .credentialsRole(role)
                                        .build())
                                .build(),
                        MethodOptions.builder()
                                .requestParameters(Map.of("method.request.path.object", true))
                                .methodResponses(List.of(MethodResponse.builder()
                                        .statusCode("200")
                                        .responseParameters(Map.of("method.response.header.Content-Type", true))
                                        .build()))
                                .build());

        apiGateway
                .getRoot()
                .addResource("images")
                .addResource("{object}")
                .addMethod(
                        "GET",
                        AwsIntegration.Builder.create()
                                .service("s3")
                                .integrationHttpMethod("GET")
                                .path("forged-by-the-fox-static-resources/images/{object}")
                                .options(IntegrationOptions.builder()
                                        .requestParameters(Map.of(
                                                "integration.request.path.object",
                                                "method.request.path.object",
                                                "integration.request.header.Accept",
                                                "method.request.header.Accept"))
                                        .integrationResponses(List.of(IntegrationResponse.builder()
                                                .statusCode("200")
                                                .responseParameters(Map.of(
                                                        "method.response.header.Content-Type",
                                                        "integration.response.header.Content-Type"))
                                                .build()))
                                        .credentialsRole(role)
                                        .passthroughBehavior(PassthroughBehavior.WHEN_NO_TEMPLATES)
                                        .build())
                                .build(),
                        MethodOptions.builder()
                                .requestParameters(Map.of(
                                        "method.request.path.object", true, "method.request.header.Accept", true))
                                .methodResponses(List.of(MethodResponse.builder()
                                        .statusCode("200")
                                        .responseParameters(Map.of("method.response.header.Content-Type", true))
                                        .build()))
                                .build());

        // Upload styles to S3 bucket
        BucketDeployment.Builder.create(stack, "forged-by-the-fox-static-resources-deployment")
                .sources(List.of(Source.asset("./build/modules/static-resources")))
                .destinationBucket(bucket)
                .prune(true)
                .build();

        return stack;
    }
}
