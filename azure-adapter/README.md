# Azure Functions

## Packaging

This step uses the [`azure-functions-maven-plugin`](https://github.com/microsoft/azure-maven-plugins/tree/develop/azure-functions-maven-plugin).

```bash
mvn -DappName=${FUNCTION_NAME} package com.microsoft.azure:azure-functions-maven-plugin:package
```

## Deployment

While the `azure-functions-maven-plugin` contains [a goal to deploy a function app](https://docs.microsoft.com/en-us/azure/azure-functions/create-first-function-cli-java?tabs=bash%2Cazure-cli%2Cbrowser#deploy-the-function-project-to-azure), editing the `pom.xml` is required to make it work.

To keep the `pom.xml` agnostic to the provider:

1. [Create your function app](https://docs.microsoft.com/en-us/cli/azure/functionapp?view=azure-cli-latest#az-functionapp-create) resource.
2. [Set](https://docs.microsoft.com/en-us/azure/azure-functions/functions-how-to-use-azure-function-app-settings?tabs=azure-cli#settings) the `SCM_DO_BUILD_DURING_DEPLOYMENT` app setting to `true`.
3. Use a [zip deployment](https://docs.microsoft.com/en-us/azure/azure-functions/deployment-zip-push) to deploy the files contained in `target/azure-functions`.

// TODO: add an example
