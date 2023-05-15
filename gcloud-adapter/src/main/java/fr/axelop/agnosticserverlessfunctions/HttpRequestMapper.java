package fr.axelop.agnosticserverlessfunctions;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

class HttpRequestMapper {

    HttpRequest map(com.google.cloud.functions.HttpRequest gcloudRequest) throws IOException {
        final Optional<String> optBody = hasBody(gcloudRequest)
            ? Optional.of(readBody(gcloudRequest))
            : Optional.empty();
        return new HttpRequest() {

            @Override
            public Optional<String> getBody() {
                return optBody;
            }

            @Override
            public long getContentLength() {
                return gcloudRequest.getContentLength();
            }

            @Override
            public Map<String, List<String>> getHeaders() {
                return Collections.unmodifiableMap(gcloudRequest.getHeaders());
            }

            @Override
            public String getMethod() {
                return gcloudRequest.getMethod();
            }

            @Override
            public String getPath() {
                return gcloudRequest.getPath();
            }

            @Override
            public Map<String, List<String>> getQueryParameters() {
                return gcloudRequest.getQueryParameters();
            }

        };
    }

    private String readBody(com.google.cloud.functions.HttpRequest gcloudRequest)
            throws IOException {
        return gcloudRequest.getReader().lines()
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private boolean hasBody(com.google.cloud.functions.HttpRequest gcloudRequest) {
        return gcloudRequest.getHeaders().containsKey("Content-Length")
            || gcloudRequest.getHeaders().containsKey("Transfer-Encoding");
    }

}
