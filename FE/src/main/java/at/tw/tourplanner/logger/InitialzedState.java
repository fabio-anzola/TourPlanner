package at.tw.tourplanner.logger;

import org.apache.logging.log4j.Logger;

public class InitialzedState extends LoggerStateBase {

    private final Logger logger;

    public InitialzedState(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void debug(String message) {
        this.logger.debug(message);
    }

    @Override
    public void info(String message) {
        this.logger.info(message);
    }

    @Override
    public void warn(String message) {
        this.logger.warn(message);
    }

    @Override
    public void error(String message) {
        this.logger.error(message);
    }

    @Override
    public void fatal(String message) {
        this.logger.fatal(message);
    }
}