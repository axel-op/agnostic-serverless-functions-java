package fr.axelop.agnosticserverlessfunctions;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;

class HttpResponseMapper {

    HttpResponseMessage map(HttpResponse handlerResponse, HttpRequestMessage<?> azureRequest) {
        final HttpStatus httpStatus = HttpStatus.valueOf(handlerResponse.getStatusCode());
        final HttpResponseMessage.Builder builder = azureRequest.createResponseBuilder(httpStatus);
        for (Map.Entry<String, String> headerEntry : toCombinedValueHeaders(handlerResponse.getHeaders()).entrySet()) {
            builder.header(headerEntry.getKey(), headerEntry.getValue());
        }
        if (handlerResponse.getBody().isPresent()) {
            builder.body(handlerResponse.getBody().get());
        }   
        return builder.build();
    }

    private static Map<String, String> toCombinedValueHeaders(Map<String, List<String>> multiValueHeaders) {
        return multiValueHeaders.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> String.join(",", e.getValue())));
    }
    
}
