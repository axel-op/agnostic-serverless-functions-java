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
        final Map<String, List<String>> headers = Collections.unmodifiableMap(toListMultimap(request.getHeaders()));
        final Map<String, List<String>> queryParams = Collections.unmodifiableMap(toListMultimap(request.getQueryStringParameters()));
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

            @Override
            public Map<String, List<String>> getQueryParameters() {
                return queryParams;
            }

        };
    }

    private <K, V> Map<K, List<V>> toListMultimap(Map<K, V> map) {
        final Map<K, List<V>> multimap = new HashMap<>();
        final Set<Map.Entry<K, V>> entrySet = Optional.ofNullable(map)
                .orElse(Map.of())
                .entrySet();
        for (Map.Entry<K, V> e : entrySet) {
            multimap
                    .computeIfAbsent(e.getKey(), k -> new ArrayList<>())
                    .add(e.getValue());
        }
        return multimap;
    }

}
