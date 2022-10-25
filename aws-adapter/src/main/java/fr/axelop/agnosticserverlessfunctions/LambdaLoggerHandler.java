package fr.axelop.agnosticserverlessfunctions;

import java.util.logging.ErrorManager;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

class LambdaLoggerHandler extends Handler {

    private final LambdaLogger lambdaLogger;

    public LambdaLoggerHandler(LambdaLogger lambdaLogger) {
        setFormatter(new SimpleFormatter());
        this.lambdaLogger = lambdaLogger;
    }

    @Override
    public void publish(LogRecord record) {
        if (!isLoggable(record)) {
            return;
        }

        String msg;
        try {
            msg = getFormatter().format(record);
        } catch (Exception ex) {
            // We don't want to throw an exception here, but we
            // report the exception to any registered ErrorManager.
            reportError(null, ex, ErrorManager.FORMAT_FAILURE);
            return;
        }

        lambdaLogger.log(getFormatter().getHead(this));
        lambdaLogger.log(msg);
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }
    
}
