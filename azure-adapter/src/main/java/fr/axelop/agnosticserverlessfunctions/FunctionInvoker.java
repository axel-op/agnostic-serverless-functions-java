package fr.axelop.agnosticserverlessfunctions;

import java.util.Optional;
import java.util.logging.Logger;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

public class FunctionInvoker {

    private static final HttpRequestMapper REQUEST_MAPPER = new HttpRequestMapper();
    private static final HttpResponseMapper RESPONSE_MAPPER = new HttpResponseMapper();

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
        final Handler handler = HandlerLoader.getInstance(logger).loadOrThrow();
        final HttpResponse handlerResponse = handler.handle(REQUEST_MAPPER.map(request), logger);
        return RESPONSE_MAPPER.map(handlerResponse, request);
    }
    
}
