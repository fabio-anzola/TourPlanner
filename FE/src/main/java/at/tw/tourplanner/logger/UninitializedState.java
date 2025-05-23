package at.tw.tourplanner.logger;

/**
 * Logger state for uninitialized loggers.
 */
public class UninitializedState extends LoggerStateBase {

    /** Prints a warning for a debug message in uninitialized state. */
    @Override
    public void debug(String message) {
        this.printUninitializedWarning();
        return;
    }

    /** Prints a warning for an info message in uninitialized state. */
    @Override
    public void info(String message) {
        this.printUninitializedWarning();
        return;
    }

    /** Prints a warning for a warning message in uninitialized state. */
    @Override
    public void warn(String message) {
        this.printUninitializedWarning();
        return;
    }

    /** Prints a warning for an error message in uninitialized state. */
    @Override
    public void error(String message) {
        this.printUninitializedWarning();
        return;
    }

    /** Prints a warning for a fatal message in uninitialized state. */
    @Override
    public void fatal(String message) {
        this.printUninitializedWarning();
        return;
    }

    /** Prints a warning indicating the logger is uninitialized. */
    private void printUninitializedWarning() {
        System.out.println("Operation was called in state uninitialized.");
    }
}