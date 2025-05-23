package at.tw.tourplanner.logger;

/**
 * Factory for creating logger wrappers.
 */
public class LoggerFactory {
    /**
     * Returns an initialized logger wrapper for the given class.
     *
     * @param clazz the class to associate with the logger
     * @return an initialized ILoggerWrapper instance
     */
    public static ILoggerWrapper getLogger(Class<?> clazz) {
        var logger = new Log4J2Wrapper(clazz);
        logger.initialize();
        return logger;
    }
}