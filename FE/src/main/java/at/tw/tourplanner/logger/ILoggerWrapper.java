package at.tw.tourplanner.logger;

public interface ILoggerWrapper {
    void debug(String message);
    void info(String message);
    void warn(String message);
    void error(String message);
    void fatal(String message);
}
