package fr.axelop.agnosticserverlessfunctions;

import java.util.List;
import java.util.Map;

import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;

class HttpResponseMapper {

    HttpResponseMessage map(HttpResponse handlerResponse, HttpRequestMessage<?> azureRequest) {
        final HttpStatus httpStatus = HttpStatus.valueOf(handlerResponse.getStatusCode());
        final HttpResponseMessage.Builder builder = azureRequest.createResponseBuilder(httpStatus);
        for (Map.Entry<String, List<String>> headerEntry : handlerResponse.getHeaders().entrySet()) {
            for (String value : headerEntry.getValue()) {
                builder.header(headerEntry.getKey(), value);
            }
        }
        if (handlerResponse.getBody().isPresent()) {
            builder.body(handlerResponse.getBody().get());
        }   
        return builder.build();
    }
    
}
