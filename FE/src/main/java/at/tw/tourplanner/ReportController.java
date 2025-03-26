package at.tw.tourplanner;

import at.tw.tourplanner.commands.ExitCommand;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ReportController {
    public void closeReport(ActionEvent actionEvent) {
        ExitCommand exitCommand = new ExitCommand((Stage) ((Button) actionEvent.getSource()).getParent().getScene().getWindow());
        exitCommand.execute();
    }

    public void closeSummary(ActionEvent actionEvent) {
        ExitCommand exitCommand = new ExitCommand((Stage) ((Button) actionEvent.getSource()).getParent().getScene().getWindow());
        exitCommand.execute();
    }
}
