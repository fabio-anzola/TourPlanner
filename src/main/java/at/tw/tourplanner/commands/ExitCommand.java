package at.tw.tourplanner.commands;

import javafx.stage.Stage;

/**
 * The exit command as an implementation of the command pattern
 */
public class ExitCommand extends Command {

    /**
     * Constructor of the Exit Command
     *
     * @param parentStage here the user can set the parent root stage
     */
    public ExitCommand(Stage parentStage) {
        super(parentStage);
    }

    /**
     * Setup of the inner execute method
     */
    @Override
    void innerExecute() {
        // Close stage
        getParentStage().close();
    }
}
