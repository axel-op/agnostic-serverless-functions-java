package fr.axelop.agnosticserverlessfunctions;

import java.util.Objects;
import java.util.Optional;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HandlerLoader {

    private static final ConcurrentMap<Logger, HandlerLoader> INSTANCES = new ConcurrentHashMap<>();
    private static final Class<Handler> HANDLER_CLASS = Handler.class;
    
    private final Logger logger;

    private HandlerLoader(Logger logger) {
        this.logger = Objects.requireNonNull(logger);
    }

    public static HandlerLoader getInstance(Logger logger) {
        return INSTANCES.computeIfAbsent(logger, HandlerLoader::new);
    }

    public Handler loadOrThrow() throws HandlerNotFoundException {
        final Optional<Handler> handler = load();
        if (handler.isPresent()) {
            return handler.get();
        }
        final HandlerNotFoundException e = new HandlerNotFoundException();
        logger.log(Level.SEVERE, e, e::getMessage);
        throw e;
    }
    
    public Optional<Handler> load() {
        try {
            return ServiceLoader.load(HANDLER_CLASS).findFirst();
        } catch (ServiceConfigurationError e) {
            logger.log(Level.SEVERE, e, () -> "Cannot load the handler class");
            throw e;
        }
    }
    
}
