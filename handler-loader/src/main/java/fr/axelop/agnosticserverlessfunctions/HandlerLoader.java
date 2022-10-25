package fr.axelop.agnosticserverlessfunctions;

import java.util.Optional;
import java.util.ServiceLoader;

public class HandlerLoader {

    private static final Class<Handler> HANDLER_CLASS = Handler.class;
    
    public Handler loadOrThrow() throws HandlerNotFoundException {
        final Optional<Handler> handler = load();
        if (handler.isPresent()) {
            return handler.get();
        }
        throw new HandlerNotFoundException();
    }
    
    public Optional<Handler> load() {
        return ServiceLoader.load(HANDLER_CLASS).findFirst();
    }
    
}
