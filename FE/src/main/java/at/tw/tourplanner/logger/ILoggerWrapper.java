package at.tw.tourplanner.logger;

/**
 * Interface for logging messages at different levels.
 */
public interface ILoggerWrapper {
    /** Logs a debug message. */
    void debug(String message);

    /** Logs an info message. */
    void info(String message);

    /** Logs a warning message. */
    void warn(String message);

    /** Logs an error message. */
    void error(String message);

    /** Logs a fatal error message. */
    void fatal(String message);
}