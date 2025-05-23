package at.tw.tourplanner.logger;

import org.apache.logging.log4j.Logger;

/**
 * Logger state that delegates logging to an initialized logger.
 */
public class InitializedState extends LoggerStateBase {

    private final Logger logger;

    /**
     * Creates a new InitializedState with the given logger.
     *
     * @param logger the underlying logger
     */
    public InitializedState(Logger logger) {
        this.logger = logger;
    }

    /** Logs a debug message. */
    @Override
    public void debug(String message) {
        this.logger.debug(message);
    }

    /** Logs an info message. */
    @Override
    public void info(String message) {
        this.logger.info(message);
    }

    /** Logs a warning message. */
    @Override
    public void warn(String message) {
        this.logger.warn(message);
    }

    /** Logs an error message. */
    @Override
    public void error(String message) {
        this.logger.error(message);
    }

    /** Logs a fatal error message. */
    @Override
    public void fatal(String message) {
        this.logger.fatal(message);
    }
}