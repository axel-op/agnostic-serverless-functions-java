package fr.axelop.agnosticserverlessfunctions;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse.APIGatewayV2HTTPResponseBuilder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class HttpResponseMapper {

    public APIGatewayV2HTTPResponse map(HttpResponse handlerResponse) {
        final APIGatewayV2HTTPResponseBuilder builder = APIGatewayV2HTTPResponse.builder()
                .withStatusCode(handlerResponse.getStatusCode())
                .withMultiValueHeaders(handlerResponse.getHeaders())
                .withHeaders(toCombinedValueHeaders(handlerResponse.getHeaders()));
        if (handlerResponse.getBody().isPresent()) {
            builder.withBody(handlerResponse.getBody().get());
        }
        return builder.build();
    }

    private static Map<String, String> toCombinedValueHeaders(Map<String, List<String>> multiValueHeaders) {
        return multiValueHeaders.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> String.join(",", e.getValue())));
    }
    
}
