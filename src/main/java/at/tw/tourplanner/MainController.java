package at.tw.tourplanner;

import at.tw.tourplanner.object.Tour;
import at.tw.tourplanner.object.TourLog;
import at.tw.tourplanner.object.TransportType;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class MainController {
    public TextField tourSearchField;
    public ListView<Tour> tourList;
    public TextField tourName;
    public TextArea tourDescription;
    public TextField fromLocation;
    public TextField toLocation;
    public ComboBox<TransportType> transportType;
    public Label tourDistance;
    public Label estimatedTime;
    public ImageView routeImage;
    public TextField logSearchField;
    public TableView<TourLog> tourLogs;
    public TableColumn logDate;
    public TableColumn logComment;
    public TableColumn logDifficulty;
    public TableColumn logDistance;
    public TableColumn logTime;
    public TableColumn logRating;
    public Label errorLabel;

    // buttons
    // tour
    public Button addTourButton;
    public Button editTourButton;
    public Button deleteTourButton;
    public Button cancelTourButton;
    // log
    public Button addLogButton;
    public Button editLogButton;
    public Button deleteLogButton;
    public Button cancelLogButton;

    private final MainModel model = new MainModel();

    /**
     * This method is called after scene has initialized!
     */
    @FXML
    public void initialize() {
        // Disable text fields by default
        disableTourFields(true);

        // Bind error label
        errorLabel.textProperty().bind(model.errorFieldProperty());

        // Bind the text fiends to a model class
        tourName.textProperty().bindBidirectional(model.getFieldTour().nameProperty());
        tourDescription.textProperty().bindBidirectional(model.getFieldTour().descriptionProperty());
        fromLocation.textProperty().bindBidirectional(model.getFieldTour().fromLocationProperty());
        toLocation.textProperty().bindBidirectional(model.getFieldTour().toLocationProperty());

        // Bind observable list to model tour list
        tourList.setItems(model.getTours());
        tourList.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Tour tour, boolean empty) {
                super.updateItem(tour, empty);
                setText(empty || tour == null ? null : tour.getName());
            }
        });

        // Bind Images
        routeImage.imageProperty().bindBidirectional(model.getFieldTour().routeImageProperty());

        // Bind observable list to model tour logs list
        logDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        logComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        logDifficulty.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        logDistance.setCellValueFactory(new PropertyValueFactory<>("totalDistance"));
        logTime.setCellValueFactory(new PropertyValueFactory<>("totalTime"));
        logRating.setCellValueFactory(new PropertyValueFactory<>("rating"));

        logDate.setCellFactory(TextFieldTableCell.forTableColumn());
        logComment.setCellFactory(TextFieldTableCell.forTableColumn());
        logDifficulty.setCellFactory(TextFieldTableCell.forTableColumn());
        logDistance.setCellFactory(TextFieldTableCell.forTableColumn());
        logTime.setCellFactory(TextFieldTableCell.forTableColumn());
        logRating.setCellFactory(TextFieldTableCell.forTableColumn());

        tourList.getSelectionModel().selectedItemProperty().addListener((obs, oldTour, newTour) -> {
            if (newTour == null) {
                tourLogs.setItems(FXCollections.observableArrayList()); // Show empty list if no tour is selected
            } else {
                tourLogs.setItems(new FilteredList<>(model.getTourLogs(), log -> log.getTourName().equalsIgnoreCase(newTour.getName())));
            }
        });

        tourList.getSelectionModel().selectedItemProperty().addListener((obs, oldTour, newTour) -> {
            if (null == newTour) {
                model.getFieldTour().setName(null);
                model.getFieldTour().setDescription(null);
                model.getFieldTour().setFromLocation(null);
                model.getFieldTour().setToLocation(null);
                model.getFieldTour().setTransportType(TransportType.DEFAULT);
                model.getFieldTour().setRouteImage(null);

                model.getCurrentTourLog().setTourName("");
                return;
            }
            model.getFieldTour().setName(newTour.getName());
            model.getFieldTour().setDescription(newTour.getDescription());
            model.getFieldTour().setFromLocation(newTour.getFromLocation());
            model.getFieldTour().setToLocation(newTour.getToLocation());
            model.getFieldTour().setTransportType(newTour.getTransportType());
            model.getFieldTour().setRouteImage(newTour.getRouteImage());

            model.getCurrentTourLog().setTourName(newTour.getName());
            //tourLogs.setItems(new FilteredList<TourLog>(this.model.getTourLogs(), log -> log.getTourName().equalsIgnoreCase(newTour.getName())));
        });

        // Populate the combo box with the enum values
        this.transportType.getItems().addAll(TransportType.values());
        this.transportType.getItems().remove(TransportType.DEFAULT);

        // Bind the combo box's value property to the model's transportType property
        this.transportType.valueProperty().bindBidirectional(this.model.getFieldTour().transportTypeProperty());
    }

    private void disableTourFields(boolean b) {
        tourName.setDisable(b);
        tourDescription.setDisable(b);
        fromLocation.setDisable(b);
        toLocation.setDisable(b);
        transportType.setDisable(b);
    }
    private boolean noCurrentAction(){
        return addTourButton.getText().equals("Add") &&
                editTourButton.getText().equals("Edit") &&
                addLogButton.getText().equals("Add Log") &&
                editLogButton.getText().equals("Edit Log");
    }

    public void onAddTour(ActionEvent actionEvent) {
        if (noCurrentAction()) {

            // Clear selection and fields
            tourList.getSelectionModel().clearSelection();

            // Disable choosing tours
            tourList.setDisable(true);

            // Enable the fields for input
            disableTourFields(false);

            // Change button label to "Confirm"
            addTourButton.setText("Confirm");
            cancelTourButton.setVisible(true);
        } else if (addTourButton.getText().equals("Confirm")) {
            // Add tour
            if (!model.addTour()) {
                // TODO: show error!
            } else {
                // Yuhu - confirm!

                // Enable choosing tours
                tourList.setDisable(false);

                // Disable fields again
                disableTourFields(true);
                // Reset button label back to "Add"
                addTourButton.setText("Add");
                cancelTourButton.setVisible(false);
            }
        }
    }

    public void onEditTour(ActionEvent actionEvent) {
        if (noCurrentAction() && tourList.getSelectionModel().getSelectedItem() != null) { //ein item muss ausgewählt sein damit man edit verwenden kann

            disableTourFields(false);
            tourName.setDisable(true);
            // Disable choosing tours
            tourList.setDisable(true);

            editTourButton.setText("Apply");
            cancelTourButton.setVisible(true);
        } else if (editTourButton.getText().equals("Apply")) {

            if (!model.editTour(tourList.getSelectionModel().getSelectedItem().getName())) { // get currently selected (under edit) tour name
                // TODO: Display error message
            } else {
                // Yuhu - confirm!

                // Enable choosing tours
                tourList.setDisable(false);

                disableTourFields(true);
                editTourButton.setText("Edit");
                cancelTourButton.setVisible(false);
            }
        }
    }

    public void onDeleteTour(ActionEvent actionEvent) {
        if (noCurrentAction()) {
            if (!model.deleteTour()) {
                // TODO: Display error message
            }
        }
    }

    public void onCalculateRoute(ActionEvent actionEvent) {
    }

    public void onAddLog(ActionEvent actionEvent) {
        if (noCurrentAction() && tourList.getSelectionModel().getSelectedItem() != null) {
            if (this.model.addTourLogPreCheck()) {
                // Disable choosing tours
                tourList.setDisable(true);

                tourLogs.refresh();

                tourLogs.setEditable(true);

                // Change button label to "Confirm"
                addLogButton.setText("Confirm");
                cancelLogButton.setVisible(true);
            }
        } else if (addLogButton.getText().equals("Confirm")) {
            // Add tour log
            if (!model.addTourLog()) {
                // TODO: show error!
            } else {
                // Yuhu - confirm!

                /*
                this.model.getCurrentTourLog().dateProperty().unbind();
                this.model.getCurrentTourLog().commentProperty().unbind();
                this.model.getCurrentTourLog().difficultyProperty().unbind();
                this.model.getCurrentTourLog().totalDistanceProperty().unbind();
                this.model.getCurrentTourLog().totalTimeProperty().unbind();
                this.model.getCurrentTourLog().ratingProperty().unbind();
                this.model.getCurrentTourLog().tourNameProperty().unbind();

                this.model.getTourLogs().get(this.model.getTourLogs().size() - 1).tourNameProperty().unbind();
                */

                // Enable choosing tours
                tourList.setDisable(false);

                tourLogs.setEditable(false);

                // Reset button label back to "Add"
                addLogButton.setText("Add Log");
                System.out.println(Arrays.deepToString(this.model.getTourLogs().toArray()));
                cancelLogButton.setVisible(false);
            }
        } else if (tourList.getSelectionModel().getSelectedItem() != null) {
            System.out.println("No tour selected for log");
            tourList.getFocusModel().focus(0);
        }
    }

    public void onEditLog(ActionEvent actionEvent) {
        if (noCurrentAction()){
            // Disable choosing tours
            tourList.setDisable(true);
            // Enable editing Logs
            tourLogs.setEditable(true);

            editLogButton.setText("Confirm");
            cancelLogButton.setVisible(true);
        } else if (editLogButton.getText().equals("Confirm")) {

            // Enable choosing tours
            tourList.setDisable(false);
            // Disable editing Logs
            tourLogs.setEditable(false);

            editLogButton.setText("Edit Log");
            cancelLogButton.setVisible(false);
        }
    }

    public void onDeleteLog(ActionEvent actionEvent) {
        if (noCurrentAction() && tourLogs.getSelectionModel().getSelectedItem() != null) {
            //nimmt selected
            TourLog selectedTourLog = tourLogs.getSelectionModel().getSelectedItem();
            if(!model.deleteTourLog(selectedTourLog)) { // callt deleteTourLog methode aus MainModel
                // TODO: show error!
            }
            //For Debugging: Print out all remaining tour logs
            System.out.println("Remaining tour logs:");
            for (TourLog log : tourLogs.getItems()) {
                System.out.println(log);  // You might want to customize this print statement if necessary
            }
            tourLogs.refresh();
        }
    }

    public void onImportFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import File");

        // Set file type filters (optional)
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("Tourplanner Files", "*.tourplanner")
        );

        // Get the current stage
        Stage stage = (Stage) ((javafx.scene.control.MenuItem) actionEvent.getSource()).getParentPopup().getOwnerWindow();

        // Show open file dialog
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            System.out.println("File selected: " + selectedFile.getAbsolutePath());

            try {
                String content = new String(Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath())));
                //System.out.println("File content:\n" + content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File selection cancelled.");
        }
    }

    public void onExportFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export File");

        // Set file type filters (optional)
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("Tourplanner Files", "*.tourplanner")
        );
        // Get the current stage
        Stage stage = (Stage) ((MenuItem) actionEvent.getSource()).getParentPopup().getOwnerWindow();

        // Show save file dialog
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                // Replace with object information
                String content = "This is an exported file.\nReplace this with actual content.";

                writer.write(content);
                System.out.println("File saved to: " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File save cancelled.");
        }
    }

    public void onExitWindow(ActionEvent actionEvent) {
    }

    public void onTourSearch(ActionEvent actionEvent) {
    }

    public void onGenTourReport(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("tour-reports-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Tour Report");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onGenSummaryReport(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("summary-reports-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Summary Report");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onCancelTour(ActionEvent actionEvent) {
        if (addTourButton.getText().equals("Confirm")) {addTourButton.setText("Add");}
        if (editTourButton.getText().equals("Apply")) {editTourButton.setText("Edit");}
        // TODO: unselect or clear fields!

        disableTourFields(true);

        // Clean error outputs
        model.setErrorField("");

        // Enable choosing tours
        tourList.setDisable(false);

        // Hide button
        cancelTourButton.setVisible(false);
    }

    @FXML
    public void onCancelLog(ActionEvent actionEvent) {
        if (addLogButton.getText().equals("Confirm")) {
            this.model.getTourLogs().remove(this.model.getTourLogs().size() - 1);

            addLogButton.setText("Add Log");
        }
        if (editLogButton.getText().equals("Confirm")) { editLogButton.setText("Edit Log"); }
        // TODO: dis select or clear fields!

        tourLogs.setEditable(false);

        // Clean error outputs
        model.setErrorField("");

        // Enable choosing tours
        tourList.setDisable(false);

        // Hide button
        cancelLogButton.setVisible(false);
    }
}