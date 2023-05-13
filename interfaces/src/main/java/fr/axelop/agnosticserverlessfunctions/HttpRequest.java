package fr.axelop.agnosticserverlessfunctions;

public interface HttpRequest extends HttpMessage {

    String getMethod();

    long getContentLength();

    String getPath();

}
