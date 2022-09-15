# Google Cloud Functions

Google Cloud Functions in Java [should be packaged as an Uber JAR](https://cloud.google.com/functions/docs/concepts/java-deploy#deploy_from_a_jar). To be deployed, the JAR should be placed in a directory that must not contain any other JAR.

```bash
DEPLOYMENT_DIR=deployment
```

## Packaging

The [`maven-shade-plugin`](https://maven.apache.org/plugins/maven-shade-plugin) is used to build the Uber JAR. While configuring it in the `pom.xml` is the recommended way to use it, here we execute it from the command line to keep the `pom.xml` file agnostic to the cloud provider.

```bash
mvn package shade:shade

# the Uber JAR is copied into the deployment directory
JAR_FILE=$(ls target | grep '.jar$' | grep -v '^original')
mkdir -p target/${DEPLOYMENT_DIR}
cp target/${JAR_FILE} target/${DEPLOYMENT_DIR}/${JAR_FILE}
```

## Deployment

The [Google Cloud documentation](https://cloud.google.com/functions/docs/deploy) describes all the possible ways to deploy your function.

For example, [using the `gcloud` CLI](https://cloud.google.com/sdk/gcloud/reference/functions/deploy):

```bash
gcloud functions deploy ${FUNCTION_NAME} \
    --region ${REGION} \
    --entry-point fr.axelop.agnosticserverlessfunctions.FunctionInvoker \
    --source target/${DEPLOYMENT_DIR} \
    --runtime java11 \
    --trigger-http \
    --gen2
```

The entrypoint class is always `fr.axelop.agnosticserverlessfunctions.FunctionInvoker`.
