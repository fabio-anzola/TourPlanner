package at.tw.tourplanner;

import at.tw.tourplanner.object.Tour;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    private ObservableList<Tour> observableTourList = FXCollections.observableArrayList();

    private MainModel model;

    /**
     * This method is called after scene has initialized!
     */
    @FXML
    public void initialize() {
        // Disable text fields by default
        tourName.setDisable(true);
        tourDescription.setDisable(true);
        fromLocation.setDisable(true);
        toLocation.setDisable(true);

        // Initialize the model
        model = new MainModel();

        // Bind observable list to tour list
        tourList.setItems(model.getTours());

        tourList.setCellFactory(listView -> new ListCell<Tour>() {
            @Override
            protected void updateItem(Tour tour, boolean empty) {
                super.updateItem(tour, empty);
                setText(empty || tour == null ? null : tour.getName());
            }
        });

        tourList.getSelectionModel().selectedItemProperty().addListener((obs, oldTour, newTour) -> {
            if (newTour != null) {
                tourName.setText(newTour.getName());
                tourDescription.setText(newTour.getDescription());
                fromLocation.setText(newTour.getFromLocation());
                toLocation.setText(newTour.getToLocation());
            }
        });

    }

    public void onAddTour(ActionEvent actionEvent) {
        Tour newTour = new Tour("New Tour", "A new tour description", "Start Location", "End Location");
        model.addTour(newTour);
    }

    public void onEditTour(ActionEvent actionEvent) {
        if (((Button)actionEvent.getSource()).getText().contains("Edit")) {
            // tourName.setDisable(false); // name cannot be changed
            tourDescription.setDisable(false);
            fromLocation.setDisable(false);
            toLocation.setDisable(false);

            ((Button)actionEvent.getSource()).setText("Apply");
        } else if (((Button) actionEvent.getSource()).getText().contains("Apply")) {
            tourName.setDisable(true);
            tourDescription.setDisable(true);
            fromLocation.setDisable(true);
            toLocation.setDisable(true);

            ((Button)actionEvent.getSource()).setText("Edit");

            Tour selectedTourName = tourList.getSelectionModel().getSelectedItem();
            if (selectedTourName != null) {
                Tour updatedTour = new Tour(selectedTourName.getName(), tourDescription.getText(), fromLocation.getText(), toLocation.getText());
                int code = model.editTour(updatedTour);
                if (code != 0) {
                    // TODO: Display error message
                }
            }
        }
    }

    public void onDeleteTour(ActionEvent actionEvent) {
        Tour selectedTourName = tourList.getSelectionModel().getSelectedItem();
        if (selectedTourName != null) {
            int code = model.deleteTour(selectedTourName.getName());
            if (code != 0) {
                // TODO: Display error message
            }
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

    public void onExitFile(ActionEvent actionEvent) {
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