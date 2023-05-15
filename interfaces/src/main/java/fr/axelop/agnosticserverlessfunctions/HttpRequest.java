package fr.axelop.agnosticserverlessfunctions;

import java.util.List;
import java.util.Map;

public interface HttpRequest extends HttpMessage {

    String getMethod();

    long getContentLength();

    String getPath();

    Map<String, List<String>> getQueryParameters();

}
