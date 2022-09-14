package fr.axelop.agnosticserverlessfunctions;

import java.util.Optional;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.cloud.functions.HttpFunction;

public class FunctionInvoker implements HttpFunction {

    private static final HttpRequestMapper requestMapper = new HttpRequestMapper();
    private static final HttpResponseMapper responseMapper = new HttpResponseMapper();
    private static final Logger logger = Logger.getLogger(FunctionInvoker.class.getName());
    private static final Supplier<String> implNotFoundMessageSupplier = () -> "Implementation of "
            + Handler.class.getName()
            + " not found. Make sure that the handler implementation is referenced as a service provider under META-INF/services/"
            + Handler.class.getName()
            + ".";

    private Optional<Handler> optHandler = Optional.empty();

    @Override
    public void service(
        com.google.cloud.functions.HttpRequest request,
        com.google.cloud.functions.HttpResponse response
    ) throws Exception {
        optHandler = optHandler.or(this::lookupHandler);
        if (optHandler.isEmpty()) {
            logger.severe(implNotFoundMessageSupplier);
            throw new RuntimeException(implNotFoundMessageSupplier.get());
        }
        final Handler handler = optHandler.get();
        final HttpResponse handlerResponse = handler.handle(requestMapper.map(request), logger);
        responseMapper.map(handlerResponse, response);
    }

    private Optional<Handler> lookupHandler() {
        try {
            return ServiceLoader.load(Handler.class).findFirst();
        } catch (ServiceConfigurationError e) {
            logger.log(Level.SEVERE, e, () -> "Cannot load the handler class");
        }
        return Optional.empty();
    }

}
