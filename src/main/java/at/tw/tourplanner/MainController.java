package at.tw.tourplanner;

import at.tw.tourplanner.object.Tour;
import at.tw.tourplanner.object.TransportType;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
    public TableView tourLogs;
    public TableColumn logDate;
    public TableColumn logComment;
    public TableColumn logDifficulty;
    public TableColumn logDistance;
    public TableColumn logTime;
    public TableColumn logRating;

    private final MainModel model = new MainModel();

    /**
     * This method is called after scene has initialized!
     */
    @FXML
    public void initialize() {
        // Disable text fields by default
        disableTourFields(true);

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

        tourList.getSelectionModel().selectedItemProperty().addListener((obs, oldTour, newTour) -> {
            if (null == newTour) {
                model.getFieldTour().setName(null);
                model.getFieldTour().setDescription(null);
                model.getFieldTour().setFromLocation(null);
                model.getFieldTour().setToLocation(null);
                model.getFieldTour().setTransportType(TransportType.DEFAULT);
                return;
            }
            model.getFieldTour().setName(null == newTour.getName() ? null : newTour.getName());
            model.getFieldTour().setDescription(null == newTour.getDescription() ? null : newTour.getDescription());
            model.getFieldTour().setFromLocation(null == newTour.getFromLocation() ? null : newTour.getFromLocation());
            model.getFieldTour().setToLocation(null == newTour.getToLocation() ? null : newTour.getToLocation());
            model.getFieldTour().setTransportType(null == newTour.getTransportType() ? TransportType.DEFAULT : newTour.getTransportType());
        });

        // Populate the combo box with the enum values
        this.transportType.getItems().addAll(TransportType.values());
        this.transportType.setValue(TransportType.CAR);

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

    public void onAddTour(ActionEvent actionEvent) {
        Button srcButton = (Button) actionEvent.getSource();
        if (srcButton.getText().equals("Add")) {
            // Clear selection and fields
            tourList.getSelectionModel().clearSelection();

            // Enable the fields for input
            disableTourFields(false);

            // Change button label to "Confirm"
            srcButton.setText("Confirm");
        } else if (srcButton.getText().equals("Confirm")) {
            /*

            // store input fields as Map
            Map<TextInputControl, String> fields = Map.of(
                tourName, tourName.getText(),
                tourDescription, tourDescription.getText(),
                fromLocation, fromLocation.getText(),
                toLocation, toLocation.getText()
            );

            if (fields.values().stream().allMatch(String::isBlank)) {
                // If all fields are empty skip all checks
            } else if (fields.values().stream().noneMatch(String::isBlank)) { //check if all fields are filled
                // get all tours
                ObservableList<Tour> tours = model.getTours();
                // check if user-given name already exists as a tour. if yes do not add tour.
                if(tours.stream().anyMatch(tour -> tour.getName().equalsIgnoreCase(tourName.getText()))){
                    tourName.setText(""); // Clear any previous text
                    tourName.setPromptText("tour already exists");
                    return;
                }
                // Add tour
                Tour newTour = new Tour(tourName.getText(), tourDescription.getText(), fromLocation.getText(), toLocation.getText());
                if (!model.addTour(newTour)) {
                    // TODO: show error!
                }
            } else {
                fields.forEach((field, text) -> {
                    if (text.isBlank()) field.setPromptText("field must be filled");
                });
                return;
            }
            // Clear the prompt text since the action was successful
            fields.keySet().forEach(field -> field.setPromptText(""));*/

            // Add tour
            if (!model.addTour()) {
                // TODO: show error!
            }

            // Disable fields again
            disableTourFields(true);
            // Reset button label back to "Add"
            srcButton.setText("Add");
        }
    }

    public void onEditTour(ActionEvent actionEvent) {
        Button srcButton = (Button) actionEvent.getSource();
        if (srcButton.getText().equals("Edit") && tourList.getSelectionModel().getSelectedItem() != null) { //ein item muss ausgewählt sein damit man edit verwenden kann
            disableTourFields(false);

            srcButton.setText("Apply");
        } else if (srcButton.getText().equals("Apply")) {

            if (!model.editTour()) {
                // TODO: Display error message
            }

            disableTourFields(true);
            srcButton.setText("Edit");
        }
    }

    public void onDeleteTour(ActionEvent actionEvent) {
        if (!model.deleteTour()) {
            // TODO: Display error message
        }
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
}