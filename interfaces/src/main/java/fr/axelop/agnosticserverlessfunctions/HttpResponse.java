package fr.axelop.agnosticserverlessfunctions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HttpResponse implements HttpMessage {

    private final int statusCode;
    private final String body;
    private final Map<String, List<String>> headers;

    HttpResponse(int statusCode, String body, Map<String, List<String>> headers) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = new HashMap<>(headers);
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public Optional<String> getBody() {
        return Optional.ofNullable(body);
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {

        private Builder() {}

        public BuilderWithStatusCode setStatusCode(int statusCode) {
            return new BuilderWithStatusCode(statusCode);
        }

    }

    public static final class BuilderWithStatusCode {

        private final int statusCode;
        private String body;
        private Map<String, List<String>> headers;

        private BuilderWithStatusCode(int statusCode) {
            this.statusCode = statusCode;
            this.headers = new HashMap<>();
        }

        public BuilderWithStatusCode setBody(String body) {
            this.body = body;
            return this;
        }

        public BuilderWithStatusCode addHeader(String header, String value) {
            this.headers.computeIfAbsent(header, k -> new ArrayList<>()).add(value);
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(statusCode, body, headers);
        }

    }

}
