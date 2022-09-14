package fr.axelop.agnosticserverlessfunctions;

import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Supplier;
import java.util.logging.Logger;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

public class FunctionInvoker {

    private static final HttpRequestMapper requestMapper = new HttpRequestMapper();
    private static final HttpResponseMapper responseMapper = new HttpResponseMapper();
    private static final Optional<Handler> optHandler = lookupHandler();
    private static final Supplier<String> exceptionMessageSupplier = () -> "Implementation of "
            + Handler.class.getName()
            + " not found. Make sure that it is referenced as a service under META-INF/services.";

    @FunctionName("handler")
    public HttpResponseMessage run(
        @HttpTrigger(
            name = "httpTrigger",
            authLevel = AuthorizationLevel.ANONYMOUS,
            methods = {
                HttpMethod.CONNECT,
                HttpMethod.DELETE,
                HttpMethod.GET,
                HttpMethod.HEAD,
                HttpMethod.OPTIONS,
                HttpMethod.PATCH,
                HttpMethod.POST,
                HttpMethod.PUT,
                HttpMethod.TRACE
            }
        )
        HttpRequestMessage<Optional<String>> request,
        ExecutionContext context
    ) throws Exception {
        final Logger logger = context.getLogger();
        if (optHandler.isEmpty()) {
            logger.severe(() -> "NO IMPLEMENTATION OF " + Handler.class.getName() + " FOUND. TERMINATING EXECUTION.");
            throw new RuntimeException(exceptionMessageSupplier.get());
        }
        final Handler handler = optHandler.get();
        final HttpResponse handlerResponse = handler.handle(requestMapper.map(request), logger);
        return responseMapper.map(handlerResponse, request);
    }

    private static Optional<Handler> lookupHandler() {
        return ServiceLoader.load(Handler.class).findFirst();
    }
    
}
