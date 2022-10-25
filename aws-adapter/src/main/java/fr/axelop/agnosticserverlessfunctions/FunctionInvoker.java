package fr.axelop.agnosticserverlessfunctions;

import java.util.logging.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;

public class FunctionInvoker implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

    private static final HttpRequestMapper REQUEST_MAPPER = new HttpRequestMapper();
    private static final HttpResponseMapper RESPONSE_MAPPER = new HttpResponseMapper();

    public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent request, Context context) {
        final Logger logger = Logger.getAnonymousLogger();
        logger.addHandler(new LambdaLoggerHandler(context.getLogger()));
        final Handler handler = HandlerLoader.getInstance(logger).loadOrThrow();
        final HttpResponse handlerResponse = handler.handle(REQUEST_MAPPER.map(request), logger);
        return RESPONSE_MAPPER.map(handlerResponse);
    }
    
}
