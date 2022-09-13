package fr.axelop.agnosticserverlessfunctions;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

class HttpResponseMapper {

    void map(
        HttpResponse handlerResponse,
        com.google.cloud.functions.HttpResponse gcloudResponse
    ) throws IOException {
        gcloudResponse.setStatusCode(handlerResponse.getStatusCode());
        addHeaders(handlerResponse.getHeaders(), gcloudResponse);
        if (handlerResponse.getBody().isPresent()) {
            addBody(handlerResponse.getBody().get(), gcloudResponse);
        }
    }

    private void addHeaders(
        Map<String, List<String>> headers,
        com.google.cloud.functions.HttpResponse gcloudResponse
    ) {
        for (Map.Entry<String, List<String>> headerEntry : headers.entrySet()) {
            for (String value : headerEntry.getValue()) {
                gcloudResponse.appendHeader(headerEntry.getKey(), value);
            }
        }
    }

    private void addBody(
        String body,
        com.google.cloud.functions.HttpResponse gcloudResponse
    ) throws IOException {
        try (var osw = new OutputStreamWriter(gcloudResponse.getOutputStream())) {
            osw.append(body);
        }
    }

}
