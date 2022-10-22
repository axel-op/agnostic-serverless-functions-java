package fr.axelop.agnosticserverlessfunctions;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Handler;
import java.util.logging.Logger;

class LoggerHandlerManager {

    private static final ConcurrentMap<Logger, LoggerHandlerManager> INSTANCES = new ConcurrentHashMap<>();

    private final Logger logger;
    private final Object lock = new Object();
    private Set<Handler> currentHandlers = new HashSet<>();

    private LoggerHandlerManager(Logger logger) {
        this.logger = Objects.requireNonNull(logger);
    }

    public static LoggerHandlerManager getInstance(Logger logger) {
        return INSTANCES.computeIfAbsent(logger, LoggerHandlerManager::new);
    }

    public void addHandler(LambdaLoggerHandler loggerHandler) {
        if (!currentHandlers.contains(loggerHandler)) {
            synchronized (lock) {
                if (currentHandlers.add(loggerHandler)) {
                    logger.addHandler(loggerHandler);
                }
            }
        }
    }

}
