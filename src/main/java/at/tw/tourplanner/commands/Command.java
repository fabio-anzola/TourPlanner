package at.tw.tourplanner.commands;

import javafx.stage.Stage;
import lombok.Getter;

/**
 * Command class as an example for the Command Design Pattern
 */
public abstract class Command {

    /**
     * The parent stage is used as the command level.
     */
    @Getter
    private Stage parentStage;

    /**
     * Default Constructor with the parent stage
     *
     * @param parentStage
     */
    public Command(Stage parentStage) {
        this.parentStage = parentStage;
    }

    /**
     * The inner execute method needs to be implemented by the command itself
     */
    abstract void innerExecute();

    /**
     * The execute function handles the real executing. Here logging logic can be implemented.
     */
    public void execute() {
        // Log logic
        innerExecute();
    }
}
