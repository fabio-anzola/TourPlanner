package at.tw.tourplanner.logger;

public class LoggerFactory {
    public static ILoggerWrapper getLogger(Class<?> clazz) {
        var logger = new Log4J2Wrapper(clazz);
        logger.initialize();
        return logger;
    }
}