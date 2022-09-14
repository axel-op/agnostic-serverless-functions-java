package fr.axelop.agnosticserverlessfunctions;

import java.util.logging.Logger;

public interface Handler {

    HttpResponse handle(HttpRequest request, Logger logger);

}
