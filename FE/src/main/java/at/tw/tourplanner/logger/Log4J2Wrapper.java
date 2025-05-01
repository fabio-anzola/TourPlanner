package at.tw.tourplanner.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Wraps the log4j2 logger instances by realizing interface ILoggerWrapper
// This avoids direct dependencies to log4j2 package
public class Log4J2Wrapper implements ILoggerWrapper {

    private Logger logger;
    private LoggerStateBase state = new UninitializedState();
    private final Class<?> clazz;

    public Log4J2Wrapper(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void debug(String message) {
        this.state.debug(message);
    }

    @Override
    public void info(String message) {
        this.state.info(message);
    }

    @Override
    public void warn(String message) {
        this.state.warn(message);
    }

    @Override
    public void error(String message) {
        this.state.error(message);
    }

    @Override
    public void fatal(String message) {
        this.state.fatal(message);
    }

    public synchronized void initialize() { // Thread safe
        if (!(state instanceof InitialzedState)) {
            this.state = new InitialzedState(LogManager.getLogger(clazz));
        }
    }
}