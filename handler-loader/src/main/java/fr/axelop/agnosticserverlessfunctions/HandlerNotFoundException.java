package fr.axelop.agnosticserverlessfunctions;

public class HandlerNotFoundException extends RuntimeException {

    private static final Class<Handler> HANDLER_CLASS = Handler.class;

    HandlerNotFoundException() {
        super("Implementation of "
                + HANDLER_CLASS.getName()
                + " not found. Make sure that the handler implementation is referenced as a service provider under META-INF/services/"
                + HANDLER_CLASS.getName()
                + ".");
    }
    
}
