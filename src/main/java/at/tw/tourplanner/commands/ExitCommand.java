package at.tw.tourplanner.commands;

import javafx.stage.Stage;

public class ExitCommand extends Command {

    public ExitCommand(Stage parentStage) {
        super(parentStage);
    }

    @Override
    void innerExecute() {
        // Close stage
        getParentStage().close();
    }
}
