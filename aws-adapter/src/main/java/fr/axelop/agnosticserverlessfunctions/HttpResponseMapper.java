package fr.axelop.agnosticserverlessfunctions;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse.APIGatewayV2HTTPResponseBuilder;

class HttpResponseMapper {

    public APIGatewayV2HTTPResponse map(HttpResponse handlerResponse) {
        final APIGatewayV2HTTPResponseBuilder builder = APIGatewayV2HTTPResponse.builder()
                .withStatusCode(handlerResponse.getStatusCode())
                .withMultiValueHeaders(handlerResponse.getHeaders());
        if (handlerResponse.getBody().isPresent()) {
            builder.withBody(handlerResponse.getBody().get());
        }   
        return builder.build();
    }
    
}
