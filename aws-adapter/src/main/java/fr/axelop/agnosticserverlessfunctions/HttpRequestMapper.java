package fr.axelop.agnosticserverlessfunctions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent.RequestContext.Http;

class HttpRequestMapper {

    public HttpRequest map(APIGatewayV2HTTPEvent request) {
        final Map<String, List<String>> headers = Collections.unmodifiableMap(getHeaders(request));
        final Http httpRequestContext = request.getRequestContext().getHttp();
        return new HttpRequest() {

            @Override
            public Optional<String> getBody() {
                return Optional.ofNullable(request.getBody());
            }

            @Override
            public long getContentLength() {
                return Optional.ofNullable(headers.getOrDefault("Content-Length", headers.get("content-length")))
                        .map(l -> l.get(0))
                        .map(Long::parseLong)
                        .orElse(0L);
            }

            @Override
            public Map<String, List<String>> getHeaders() {
                return headers;
            }

            @Override
            public String getMethod() {
                return httpRequestContext.getMethod();
            }

            @Override
            public String getPath() {
                return httpRequestContext.getPath();
            }

        };
    }

    private Map<String, List<String>> getHeaders(APIGatewayV2HTTPEvent request) {
        final Map<String, List<String>> headers = new HashMap<>();
        final Set<Map.Entry<String, String>> entrySet = Optional.ofNullable(request.getHeaders())
                .orElse(Map.of())
                .entrySet();
        for (Map.Entry<String, String> e : entrySet) {
            headers
                    .computeIfAbsent(e.getKey(), k -> new ArrayList<>(1))
                    .add(e.getValue());
        }
        return headers;
    }

}
