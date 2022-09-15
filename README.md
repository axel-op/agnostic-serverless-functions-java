# Agnostic Serverless Functions in Java

Serverless functions are a great way to deploy backend services at a low cost. However, while the infrastructure is abstracted, the code has a strong dependency on the framework used. Once a provider is chosen, it's difficult to switch to another one without some refactoring.

This project aims at reducing the dependency of serverless functions to the platform they run on. The same code can be compiled to a function deployable on any supported FaaS platform.

## Usage

### Configuration

You'll need to add two dependencies from [my Maven repository](https://github.com/axel-op/maven-packages):

- the `interfaces` dependency contains the classes that will be used to code the agnostic function.
- the `adapter` is the dependency that will add the classes needed to execute the function on the selected provider.

```xml
<!-- pom.xml -->
<project>

    <properties>
        <!-- Can be: azure, gcloud -->
        <!-- This property can also be set by a command-line parameter -->
        <faas.provider>gcloud</faas.provider>
    </properties>

    <dependencies>
        <dependency>
            <groupId>fr.axelop.agnosticserverlessfunctions</groupId>
            <artifactId>agnostic-serverless-functions-interfaces</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>

        <dependency>
            <groupId>fr.axelop.agnosticserverlessfunctions</groupId>
            <artifactId>agnostic-serverless-functions-${faas.provider}-adapter</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
    </dependencies>

</project>
```

### Code

The class containing the agnostic function must extend the `Handler` class. It must be [registered as a *service provider*](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/ServiceLoader.html) so it can be loaded with a `ServiceLoader`. This means that there must be a resource file `META-INF/services/fr.axelop.agnosticserverlessfunctions.Handler` containing the fully qualified binary name of your handler class (in this example, it would be `com.example.MyHandler`). You can automate this process by using the [Google @AutoService](https://github.com/google/auto/tree/master/service) annotation.

There can only be one handler in your JAR.

```java
package com.example;

import java.util.logging.Logger;

import fr.axelop.agnosticserverlessfunctions.Handler;
import fr.axelop.agnosticserverlessfunctions.HttpRequest;
import fr.axelop.agnosticserverlessfunctions.HttpResponse;

public class MyHandler implements Handler {

    @Override
    public HttpResponse handle(HttpRequest request, Logger logger) {
        return HttpResponse.newBuilder()
                .setStatusCode(200)
                .setBody(request.getBody().isPresent()
                        ? "The request body was: " + request.getBody().get()
                        : "There was no body in this " + request.getMethod() + " request!")
                .build();
    }

}
```

### Packaging and deployment

These steps are specific to each FaaS provider:

- [Azure Functions](./azure-adapter)
- [Google Cloud Functions](./gcloud-adapter)

## Advantages

- Scalability: the deployment of the same code on different providers can be completely automated.
- Migration: as there is no direct dependency between the business logic and the provider-specific framework, it's easy to switch to a different provider without modifying the code at all.
- A/B testing: a same function can be deployed on multiple providers at the same time, making it easy to compare their performance.

## Limitations and future improvements

### Limited trigger types

For now, this project can only deploy HTTP-triggered functions.

### Non-agnostic packaging and deployment

While they can be entirely automated, the packaging and deployment steps differ between the cloud providers. A possible improvement is to script them so they can be triggered by a single command.
