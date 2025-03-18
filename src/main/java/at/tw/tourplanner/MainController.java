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
import javafx.scene.image.Image;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

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
    public Label errorLabel;

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
        tourLogs.setItems(this.model.getTourLogs());

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
            // Add tour
            if (!model.addTour()) {
                // TODO: show error!
            } else {
                // Yuhu - confirm!

                // Disable fields again
                disableTourFields(true);
                // Reset button label back to "Add"
                srcButton.setText("Add");
            }
        }
    }

    public void onEditTour(ActionEvent actionEvent) {
        Button srcButton = (Button) actionEvent.getSource();
        if (srcButton.getText().equals("Edit") && tourList.getSelectionModel().getSelectedItem() != null) { //ein item muss ausgewählt sein damit man edit verwenden kann

            disableTourFields(false);
            tourName.setDisable(true);

            srcButton.setText("Apply");
        } else if (srcButton.getText().equals("Apply")) {

            if (!model.editTour(tourList.getSelectionModel().getSelectedItem().getName())) { // get currently selected (under edit) tour name
                // TODO: Display error message
            } else {
                // Yuhu - confirm!

                disableTourFields(true);
                srcButton.setText("Edit");
            }
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
        Button srcButton = (Button) actionEvent.getSource();
        if (srcButton.getText().equals("Add Log")) {
            /*
            // Values set here are overwritten by bind
            TourLog tourLog = new TourLog(LocalDate.now().toString(), "Enter comment", 0, 0, 0, 0, "");

            tourLog.dateProperty().bindBidirectional(this.model.getCurrentTourLog().dateProperty());
            tourLog.commentProperty().bindBidirectional(this.model.getCurrentTourLog().commentProperty());
            tourLog.difficultyProperty().bindBidirectional(this.model.getCurrentTourLog().difficultyProperty());
            tourLog.totalDistanceProperty().bindBidirectional(this.model.getCurrentTourLog().totalDistanceProperty());
            tourLog.totalTimeProperty().bindBidirectional(this.model.getCurrentTourLog().totalTimeProperty());
            tourLog.ratingProperty().bindBidirectional(this.model.getCurrentTourLog().ratingProperty());
            tourLog.tourNameProperty().bindBidirectional(this.model.getCurrentTourLog().tourNameProperty());
            this.model.getTourLogs().add(tourLog);
            */

            this.model.getTourLogs().add(this.model.getCurrentTourLog());
            tourLogs.refresh();

            tourLogs.setEditable(true);

            // Change button label to "Confirm"
            srcButton.setText("Confirm");
        } else if (srcButton.getText().equals("Confirm")) {
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

                tourLogs.setEditable(false);

                // Reset button label back to "Add"
                srcButton.setText("Add Log");
                System.out.println(Arrays.deepToString(this.model.getTourLogs().toArray()));
            }
        }
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