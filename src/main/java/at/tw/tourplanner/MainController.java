package at.tw.tourplanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

public class MainController {
    public TextField tourSearchField;
    public ListView tourList;
    public TextField tourName;
    public TextArea tourDescription;
    public TextField fromLocation;
    public TextField toLocation;
    public ComboBox transportType;
    public Label tourDistance;
    public Label estimatedTime;
    public ImageView routeImage;
    public TextField logSearchField;
    public TableView tourLogs;
    public TableColumn logDate;
    public TableColumn logComment;
    public TableColumn logDifficulty;
    public TableColumn logDistance;
    public TableColumn logTime;
    public TableColumn logRating;
    @FXML
    private Label welcomeText;

    public void onAddTour(ActionEvent actionEvent) {
    }

    public void onEditTour(ActionEvent actionEvent) {
    }

    public void onDeleteTour(ActionEvent actionEvent) {
    }

    public void onCalculateRoute(ActionEvent actionEvent) {
    }

    public void onAddLog(ActionEvent actionEvent) {
    }

    public void onEditLog(ActionEvent actionEvent) {
    }

    public void onDeleteLog(ActionEvent actionEvent) {
    }

    public void onImportFile(ActionEvent actionEvent) {
    }

    public void onExportFile(ActionEvent actionEvent) {
    }

    public void onExitFile(ActionEvent actionEvent) {
    }

    public void onTourSearch(ActionEvent actionEvent) {
    }
}