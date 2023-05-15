package fr.axelop.agnosticserverlessfunctions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import com.microsoft.azure.functions.HttpRequestMessage;

class HttpRequestMapper {

    public HttpRequest map(HttpRequestMessage<Optional<String>> azureRequest) {
        final Map<String, List<String>> headers = Collections.unmodifiableMap(toListMultimap(azureRequest.getHeaders()));
        final Map<String, List<String>> queryParams = Collections.unmodifiableMap(toListMultimap(azureRequest.getQueryParameters()));
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
