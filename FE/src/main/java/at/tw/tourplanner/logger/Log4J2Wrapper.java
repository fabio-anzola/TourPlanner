package at.tw.tourplanner.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Wraps a Log4j2 logger and implements ILoggerWrapper.
 */
public class Log4J2Wrapper implements ILoggerWrapper {

    private Logger logger;
    private LoggerStateBase state = new UninitializedState();
    private final Class<?> clazz;

    /**
     * Creates a new Log4J2Wrapper for the given class.
     *
     * @param clazz the class to associate with the logger
     */
    public Log4J2Wrapper(Class<?> clazz) {
        this.clazz = clazz;
    }

    /** Logs a debug message. */
    @Override
    public void debug(String message) {
        this.state.debug(message);
    }

    /** Logs an info message. */
    @Override
    public void info(String message) {
        this.state.info(message);
    }

    /** Logs a warning message. */
    @Override
    public void warn(String message) {
        this.state.warn(message);
    }

    /** Logs an error message. */
    @Override
    public void error(String message) {
        this.state.error(message);
    }

    /** Logs a fatal error message. */
    @Override
    public void fatal(String message) {
        this.state.fatal(message);
    }

    /**
     * Initializes the logger if not already initialized.
     */
    public synchronized void initialize() {
        if (!(state instanceof InitializedState)) {
            this.state = new InitializedState(LogManager.getLogger(clazz));
        }
    }
}