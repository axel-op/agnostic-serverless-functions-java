package fr.axelop.agnosticserverlessfunctions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.microsoft.azure.functions.HttpRequestMessage;

class HttpRequestMapper {

    public HttpRequest map(HttpRequestMessage<Optional<String>> azureRequest) {
        final Map<String, List<String>> headers = Collections.unmodifiableMap(getHeaders(azureRequest));
        return new HttpRequest() {

            @Override
            public Optional<String> getBody() {
                return azureRequest.getBody();
            }

            @Override
            public long getContentLength() {
                return Long.parseLong(azureRequest.getHeaders().getOrDefault("Content-Length", "0"));
            }

            @Override
            public Map<String, List<String>> getHeaders() {
                return headers;
            }

            @Override
            public String getMethod() {
                return azureRequest.getHttpMethod().toString();
            }

            @Override
            public String getPath() {
                return azureRequest.getUri().getPath();
            };

        };
    }

    private Map<String, List<String>> getHeaders(HttpRequestMessage<Optional<String>> azureRequest) {
        final Map<String, List<String>> headers = new HashMap<>(azureRequest.getHeaders().size());
        for (Map.Entry<String, String> header : azureRequest.getHeaders().entrySet()) {
            headers
                    .computeIfAbsent(header.getKey(), k -> new ArrayList<>(1))
                    .add(header.getValue());
        }
        return headers;
    }

}
