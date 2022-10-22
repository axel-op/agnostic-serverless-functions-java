package fr.axelop.agnosticserverlessfunctions;

import java.util.logging.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;

public class FunctionInvoker implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

    private static final HttpRequestMapper REQUEST_MAPPER = new HttpRequestMapper();
    private static final HttpResponseMapper RESPONSE_MAPPER = new HttpResponseMapper();
    private static final Logger LOGGER = Logger.getLogger(FunctionInvoker.class.getName());
    private static final LoggerHandlerManager LOGGER_HANDLER_MANAGER = LoggerHandlerManager.getInstance(LOGGER);

    public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent request, Context context) {
        final LambdaLogger lambdaLogger = context.getLogger();
        LOGGER_HANDLER_MANAGER.addHandler(LambdaLoggerHandler.getInstance(lambdaLogger));
        final Handler handler = HandlerLoader.getInstance(LOGGER).loadOrThrow();
        final HttpResponse handlerResponse = handler.handle(REQUEST_MAPPER.map(request), LOGGER);
        return RESPONSE_MAPPER.map(handlerResponse);
    }
    
}
