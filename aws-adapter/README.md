# AWS Lambda

AWS Lambdas in Java [should be packaged as an Uber JAR](https://docs.aws.amazon.com/lambda/latest/dg/java-package.html#java-package-maven).

## Packaging

The [`maven-shade-plugin`](https://maven.apache.org/plugins/maven-shade-plugin) is used to build the Uber JAR. While configuring it in the `pom.xml` is the recommended way to use it, here we execute it from the command line to keep the `pom.xml` file agnostic to the cloud provider.

```bash
mvn package shade:shade

# This is the Maven "target" directory
BUILD_DIR=$(mvn help:evaluate -Dexpression=project.build.directory -q -DforceStdout)

# The Maven shade plugin adds the "original-" prefix to the non-shaded JAR.
# Here we select the shaded JAR which does not have this prefix.
JAR_FILE=$(ls ${BUILD_DIR} | grep '.jar$' | grep -v '^original')
```

## Deployment

The [AWS documentation](https://docs.aws.amazon.com/lambda/latest/dg/lambda-java.html) describes all the possible ways to deploy your function.

For example, [using the `UodateFunctionCode` API with the `aws` CLI](https://docs.aws.amazon.com/lambda/latest/dg/API_UpdateFunctionCode.html):

```bash
aws lambda update-function-code \
    --function-name ${FUNCTION_NAME} \
    --zip-file fileb://./${BUILD_DIR}/${JAR_FILE}
```

The handler is always `fr.axelop.agnosticserverlessfunctions.FunctionInvoker::handleRequest`.
