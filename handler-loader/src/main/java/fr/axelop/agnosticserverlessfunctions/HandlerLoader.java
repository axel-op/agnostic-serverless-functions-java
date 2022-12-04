package fr.axelop.agnosticserverlessfunctions;

import java.util.Optional;
import java.util.ServiceLoader;

public class HandlerLoader {

    private static final Class<Handler> HANDLER_CLASS = Handler.class;
    private static Optional<Handler> cached = Optional.empty();
    
    public Handler loadOrThrow() throws HandlerNotFoundException {
        return load().orElseThrow(HandlerNotFoundException::new);
    }
    
    public Optional<Handler> load() {
        cached = cached.or(ServiceLoader.load(HANDLER_CLASS)::findFirst);
        return cached;
    }
    
}
