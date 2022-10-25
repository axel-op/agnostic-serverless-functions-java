package fr.axelop.agnosticserverlessfunctions;

import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingRethrower {

    private final Logger logger;

    public LoggingRethrower(Logger logger) {
        this.logger = logger;
    }

    public <R, T extends RuntimeException> R logAndRethrow(
            SupplierWithThrowable<R, T> supplier,
            Function<RuntimeException, String> message
    ) {
        try {
            return supplier.get();
        } catch (RuntimeException t) {
            logger.log(Level.SEVERE, t, () -> message.apply(t));
            throw t;
        }
    }

    public <R, T extends RuntimeException> R logAndRethrow(
            SupplierWithThrowable<R, T> supplier
    ) {
        return logAndRethrow(supplier, RuntimeException::getMessage);
    }

}
