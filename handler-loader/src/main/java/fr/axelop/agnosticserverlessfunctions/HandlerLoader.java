package fr.axelop.agnosticserverlessfunctions;

import java.util.Optional;
import java.util.ServiceLoader;

public class HandlerLoader {

    private static final Class<Handler> HANDLER_CLASS = Handler.class;
    
    public Handler loadOrThrow() throws HandlerNotFoundException {
        return load().orElseThrow(HandlerNotFoundException::new);
    }
    
    public Optional<Handler> load() {
        return ServiceLoader.load(HANDLER_CLASS).findFirst();
    }
    
}
