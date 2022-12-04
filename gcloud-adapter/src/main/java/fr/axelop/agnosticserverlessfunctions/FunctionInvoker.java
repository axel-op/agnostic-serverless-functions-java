package fr.axelop.agnosticserverlessfunctions;

import java.util.logging.Logger;

import com.google.cloud.functions.HttpFunction;

public class FunctionInvoker implements HttpFunction {

    private static final HttpRequestMapper REQUEST_MAPPER = new HttpRequestMapper();
    private static final HttpResponseMapper RESPONSE_MAPPER = new HttpResponseMapper();
    private static final Logger LOGGER = Logger.getLogger(FunctionInvoker.class.getName());
    private static final HandlerLoader HANDLER_LOADER = new HandlerLoader();

    @Override
    public void service(
        com.google.cloud.functions.HttpRequest request,
        com.google.cloud.functions.HttpResponse response
    ) throws Exception {
        final Handler handler = HANDLER_LOADER.loadOrThrow();
        final HttpResponse handlerResponse = handler.handle(REQUEST_MAPPER.map(request), LOGGER);
        RESPONSE_MAPPER.map(handlerResponse, response);
    }

}
