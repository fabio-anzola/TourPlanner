package at.tw.tourplanner.commands;

import javafx.stage.Stage;
import lombok.Getter;

public abstract class Command {

    @Getter
    private Stage parentStage;

    public Command(Stage parentStage) {
        this.parentStage = parentStage;
    }

    abstract void innerExecute();

    public void execute() {
        // Log logic
        innerExecute();
    }
}
