# Google Cloud Functions

Google Cloud Functions in Java [should be packaged as an Uber JAR](https://cloud.google.com/functions/docs/concepts/java-deploy#deploy_from_a_jar). To be deployed, the JAR should be placed in a directory that must not contain any other JAR.

```bash
DEPLOYMENT_DIR=deployment
```

## Packaging

The [`maven-shade-plugin`](https://maven.apache.org/plugins/maven-shade-plugin) is used to build the Uber JAR. While configuring it in the `pom.xml` is the recommended way to use it, here we execute it from the command line to keep the `pom.xml` file agnostic to the cloud provider.

```bash
mvn package shade:shade

# This is the Maven "target" directory
BUILD_DIR=$(mvn help:evaluate -Dexpression=project.build.directory -q -DforceStdout)

# The Maven shade plugin adds the "original-" prefix to the non-shaded JAR.
# Here we select the shaded JAR which does not have this prefix.
JAR_FILE=$(ls ${BUILD_DIR} | grep '.jar$' | grep -v '^original')

# the Uber JAR is copied into the deployment directory
mkdir -p ${BUILD_DIR}/${DEPLOYMENT_DIR}
cp ${BUILD_DIR}/${JAR_FILE} ${BUILD_DIR}/${DEPLOYMENT_DIR}/${JAR_FILE}
```

## Deployment

The [Google Cloud documentation](https://cloud.google.com/functions/docs/deploy) describes all the possible ways to deploy your function.

For example, [using the `gcloud` CLI](https://cloud.google.com/sdk/gcloud/reference/functions/deploy):

```bash
gcloud functions deploy ${FUNCTION_NAME} \
    --region ${REGION} \
    --entry-point fr.axelop.agnosticserverlessfunctions.FunctionInvoker \
    --source ${BUILD_DIR}/${DEPLOYMENT_DIR} \
    --runtime java11 \
    --trigger-http \
    --gen2
```

The entrypoint class is always `fr.axelop.agnosticserverlessfunctions.FunctionInvoker`.
