package at.tw.tourplanner;

import at.tw.tourplanner.object.Tour;
import at.tw.tourplanner.object.TourLog;
import at.tw.tourplanner.object.TransportType;
import at.tw.tourplanner.service.pdfGenerationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
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
import java.util.Comparator;

public class MainController {
    /**
     * Search field for filtering tours by name.
     */
    public TextField tourSearchField;

    /**
     * Contains Filtered Tours.
     */
    public FilteredList<Tour> filteredTours;

    /**
     * Button for filtering tours by name.
     */
    public Button tourSearchButton;

    /**
     * Contains Filtered Tour Logs.
     */
    public FilteredList<TourLog> filteredLogs;

    /**
     * List view displaying all tours.
     */
    public ListView<Tour> tourList;

    /**
     * Input field for the tour name.
     */
    public TextField tourName;

    /**
     * Input area for the tour description.
     */
    public TextArea tourDescription;

    /**
     * Input field for the tour's starting location.
     */
    public TextField fromLocation;

    /**
     * Input field for the tour's destination.
     */
    public TextField toLocation;

    /**
     * Dropdown for selecting the type of transport.
     */
    public ComboBox<TransportType> transportType;

    /**
     * Label displaying the total distance of the tour.
     */
    public Label tourDistance;

    /**
     * Label displaying the estimated time for the tour.
     */
    public Label estimatedTime;

    /**
     * Image view showing the route map of the tour.
     */
    public ImageView routeImage;

    /**
     * Search field for filtering tour logs.
     */
    public TextField logSearchField;

    /**
     * Button for filtering tour logs.
     */
    public Button logSearchButton;

    /**
     * Table view showing all logs related to the selected tour.
     */
    public TableView<TourLog> tourLogs;

    /**
     * Table column for the date of the log.
     */
    public TableColumn logDate;

    /**
     * Table column for the comment in the log.
     */
    public TableColumn logComment;

    /**
     * Table column for the difficulty level of the tour.
     */
    public TableColumn logDifficulty;

    /**
     * Table column for the total distance covered in the log.
     */
    public TableColumn logDistance;

    /**
     * Table column for the total time taken in the log.
     */
    public TableColumn logTime;

    /**
     * Table column for the rating given in the log.
     */
    public TableColumn logRating;

    /**
     * Label for displaying error messages or validation feedback.
     */
    public Label errorLabel;

    /**
     * Button for adding a new tour.
     */
    public Button addTourButton;

    /**
     * Button for editing the selected tour.
     */
    public Button editTourButton;

    /**
     * Button for deleting the selected tour.
     */
    public Button deleteTourButton;

    /**
     * Button to cancel adding or editing a tour.
     */
    public Button cancelTourButton;

    /**
     * Button for adding a new tour log.
     */
    public Button addLogButton;

    /**
     * Button for editing the selected tour log.
     */
    public Button editLogButton;

    /**
     * Button for deleting the selected tour log.
     */
    public Button deleteLogButton;

    /**
     * Button to cancel adding or editing a tour log.
     */
    public Button cancelLogButton;

    /**
     * Reference to the main model containing business logic and observable data.
     */
    private final MainModel model = new MainModel();

    /**
     * Initializes UI bindings and event listeners.
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
        routeImage.imageProperty().bindBidirectional(model.getFieldTour().routeImageProperty());

        // Bind observable list to model tour list
        // Step 1: Create FilteredList from the full model list
        filteredTours = new FilteredList<>(model.getTours(), t -> true);

        // Step 2: Wrap it in a SortedList (for ordering by popularity)
        SortedList<Tour> sortedTours = new SortedList<>(filteredTours);
        sortedTours.setComparator(Comparator.comparingInt(Tour::getPopularity).reversed());

        // Step 3: Bind to the ListView
        tourList.setItems(sortedTours);
        tourList.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Tour tour, boolean empty) {
                super.updateItem(tour, empty);
                setText(empty || tour == null ? null : tour.getName());
            }
        });

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
                // Show empty list if no tour is selected
                tourLogs.setItems(FXCollections.observableArrayList());
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
        });

        // Populate the combo box with the enum values
        this.transportType.getItems().addAll(TransportType.values());
        this.transportType.getItems().remove(TransportType.DEFAULT);

        // Bind the combo box's value property to the model's transportType property
        this.transportType.valueProperty().bindBidirectional(this.model.getFieldTour().transportTypeProperty());
    }

    /**
     * Enables or disables tour input fields.
     *
     * @param b true to disable, false to enable
     */
    private void disableTourFields(boolean b) {
        tourName.setDisable(b);
        tourDescription.setDisable(b);
        fromLocation.setDisable(b);
        toLocation.setDisable(b);
        transportType.setDisable(b);
    }

    /**
     * Checks if no add/edit actions are active.
     *
     * @return true if no actions are ongoing
     */
    private boolean noCurrentAction(){
        return addTourButton.getText().equals("Add") &&
                editTourButton.getText().equals("Edit") &&
                addLogButton.getText().equals("Add Log") &&
                editLogButton.getText().equals("Edit Log");
    }

    /**
     * Refreshes the tour list
     */
    public void refreshTourList() {
        String textSearch = tourSearchField.getText().toLowerCase();

        filteredTours.setPredicate(tour ->
                textSearch.isBlank() || tour.getName().toLowerCase().contains(textSearch)
        );
    }

    /**
     * Handles recalibration of the popularity of a tour
     *
     * @param selectedTour contains tour object
     * @return true if popularity was successfully set, false otherwise.
     */
    private boolean tourPopularityRecalibration(Tour selectedTour){
        if(selectedTour != null){
            long tourLogCount = tourLogs.getItems().stream()
                    .filter(log -> log.getTourName().equals(selectedTour.getName()))
                    .count();

            return model.setTourPopularity(selectedTour, tourLogCount);
        }
        return false;
    }

    /**
     * Handles recalibration of the child friendliness of a tour
     *
     * @param selectedTour contains tour object
     * @return true if child friendliness was successfully set, false otherwise.
     */
    private boolean tourChildFriendlinessRecalibration(Tour selectedTour){
        if(selectedTour != null){
            var matchingTourLogs = tourLogs.getItems().stream()
                    .filter(log -> log.getTourName().equals(selectedTour.getName()))
                    .toList();

            if (matchingTourLogs.isEmpty()){
                model.setTourChildFriendliness(selectedTour, -1); // unknown child friendliness
                return true;
            }

            // calculating averages
            double avgDifficulty = matchingTourLogs.stream()
                    .mapToInt(TourLog::getParsedDifficulty)
                    .average()
                    .orElse(0);


            double avgTime = matchingTourLogs.stream()
                    .mapToInt(TourLog::getParsedTotalTime)
                    .average()
                    .orElse(0);

            double avgDistance = matchingTourLogs.stream()
                    .mapToInt(TourLog::getParsedTotalDistance)
                    .average()
                    .orElse(0);

            // calculating child friendliness
            double difficultyNorm = (avgDifficulty - 1) / 4.0;
            double distanceNorm = Math.min(avgDistance / 15.0, 1.0);  // everything greater than 15km is max difficulty for children
            double timeNorm = Math.min(avgTime / 300, 1.0);   // everything greater than 5h is max difficulty for children

            double score = (difficultyNorm * 0.5 + distanceNorm * 0.25 + timeNorm * 0.25) * 100;

            int childFriendliness;
            if (score <= 25) childFriendliness = 4;         // very child friendly
            else if (score <= 50) childFriendliness = 3;    // child friendly
            else if (score <= 75) childFriendliness = 2;    // child unfriendly
            else childFriendliness = 1;                     // very child unfriendly

            return model.setTourChildFriendliness(selectedTour, childFriendliness);
        }
        return false;
    }


    /**
     * Handles adding a new tour.
     *
     * @param actionEvent triggered by the Add Tour button
     */
    public void onAddTour(ActionEvent actionEvent) {
        if (noCurrentAction()) {
            // Clear selection and fields
            tourList.getSelectionModel().clearSelection();

            // Disable choosing tours
            tourList.setDisable(true);

            //Disable  text search for tours
            tourSearchField.setDisable(true);
            tourSearchButton.setDisable(true);

            // Enable the fields for input
            disableTourFields(false);

            // Change button label to "Confirm"
            addTourButton.setText("Confirm");

            // Set cancel button as visible
            cancelTourButton.setVisible(true);
        } else if (addTourButton.getText().equals("Confirm")) {
            // Add tour
            if (!model.addTour()) {
                // TODO: show error!
            } else {
                // Yuhu - confirm!

                // Refresh the Tour table
                refreshTourList();

                // Enable choosing tours
                tourList.setDisable(false);

                // Enable text search for tours
                tourSearchField.setDisable(false);
                tourSearchButton.setDisable(false);

                // Disable fields again
                disableTourFields(true);

                // Reset button label back to "Add"
                addTourButton.setText("Add");

                // Set cancel button as not visible
                cancelTourButton.setVisible(false);
            }
        }
    }

    /**
     * Handles editing the selected tour.
     *
     * @param actionEvent triggered by the Edit Tour button
     */
    public void onEditTour(ActionEvent actionEvent) {
        //ein item muss ausgewählt sein damit man edit verwenden kann
        if (noCurrentAction() && tourList.getSelectionModel().getSelectedItem() != null) {
            // Enable the tour fields
            disableTourFields(false);

            // Disable the name field as this should not be changed
            tourName.setDisable(true);

            // Disable choosing tours
            tourList.setDisable(true);

            // Disable text search for tours
            tourSearchField.setDisable(true);
            tourSearchButton.setDisable(true);

            // Change button text to apply
            editTourButton.setText("Apply");

            // Enable the cancel button
            cancelTourButton.setVisible(true);
        } else if (editTourButton.getText().equals("Apply")) {
            // get currently selected (under edit) tour name
            if (!model.editTour(tourList.getSelectionModel().getSelectedItem().getName())) {
                // TODO: Display error message
            } else {
                // Yuhu - confirm!

                // Enable choosing tours
                tourList.setDisable(false);

                // Enable text search for tours
                tourSearchField.setDisable(false);
                tourSearchButton.setDisable(false);

                // Disable tour fields again
                disableTourFields(true);

                // Set button text back to edit
                editTourButton.setText("Edit");

                // Disable cancel tour again
                cancelTourButton.setVisible(false);
            }
        }
    }


    /**
     * Deletes the selected tour.
     *
     * @param actionEvent triggered by the Delete Tour button
     */
    public void onDeleteTour(ActionEvent actionEvent) {
        if (noCurrentAction()) {
            if (!model.deleteTour()) {
                // TODO: Display error message
            }
        }
    }

    /**
     * Placeholder for route calculation logic.
     *
     * @param actionEvent triggered by the Calculate Route button
     */
    public void onCalculateRoute(ActionEvent actionEvent) {
    }

    /**
     * Handles adding a new tour log.
     *
     * @param actionEvent triggered by the Add Log button
     */
    public void onAddLog(ActionEvent actionEvent) {
        // check if no action ongoing and if a tour is selected
        if (noCurrentAction() && tourList.getSelectionModel().getSelectedItem() != null) {
            if (this.model.addTourLogPreCheck()) {
                // Disable choosing tours
                tourList.setDisable(true);

                // Disable text search for tour logs
                logSearchField.setDisable(true);
                logSearchButton.setDisable(true);
                // Disable text search for tours
                tourSearchField.setDisable(true);
                tourSearchButton.setDisable(true);

                // refresh tour log list
                tourLogs.refresh();

                // allow editing of table
                tourLogs.setEditable(true);

                // Change button label to "Confirm"
                addLogButton.setText("Confirm");

                // show cancel button
                cancelLogButton.setVisible(true);
            }
        } else if (addLogButton.getText().equals("Confirm")) {
            // Add tour log
            if (!model.addTourLog() ) {
                // TODO: show error!
            }
            // Recalibrate the popularity of affected tour
            else if (!tourPopularityRecalibration(tourList.getSelectionModel().getSelectedItem())){
                // TODO: show error!
            }
            // Recalibrate the child friendliness of affected tour
            else if (!tourChildFriendlinessRecalibration(tourList.getSelectionModel().getSelectedItem())){
                // TODO: show error!
            }
            else {
                // Yuhu - confirm!

                // Refresh the Tour table
                refreshTourList();

                // Enable choosing tours
                tourList.setDisable(false);

                // Enable text search for tour logs
                logSearchField.setDisable(false);
                logSearchButton.setDisable(false);
                // Enable text search for tours
                tourSearchField.setDisable(false);
                tourSearchButton.setDisable(false);

                tourLogs.setEditable(false);

                // Reset button label back to "Add"
                addLogButton.setText("Add Log");

                // disable chancel button again
                cancelLogButton.setVisible(false);
            }
        } else if (tourList.getSelectionModel().getSelectedItem() != null) {
            System.out.println("No tour selected for log");
            tourList.getFocusModel().focus(0);
        }
    }

    /**
     * Handles editing a tour log.
     *
     * @param actionEvent triggered by the Edit Log button
     */
    public void onEditLog(ActionEvent actionEvent) {
        if (noCurrentAction()){
            // Disable choosing tours
            tourList.setDisable(true);

            // Disable text search for tour logs
            logSearchField.setDisable(true);
            logSearchButton.setDisable(true);
            // Disable text search for tours
            tourSearchField.setDisable(true);
            tourSearchButton.setDisable(true);

            // Enable editing Logs
            tourLogs.setEditable(true);

            // Change button text
            editLogButton.setText("Confirm");

            // Show cancel button
            cancelLogButton.setVisible(true);
        } else if (editLogButton.getText().equals("Confirm")) {
            // Recalibrate the child friendliness of affected tour
            if (!tourChildFriendlinessRecalibration(tourList.getSelectionModel().getSelectedItem())){
                // TODO: show error!
            } else {
                // Enable choosing tours
                tourList.setDisable(false);

                // Enable text search for tour logs
                logSearchField.setDisable(false);
                logSearchButton.setDisable(false);
                // Enable text search for tours
                tourSearchField.setDisable(false);
                tourSearchButton.setDisable(false);

                // Disable editing Logs
                tourLogs.setEditable(false);

                // Change button text back again
                editLogButton.setText("Edit Log");

                // Disable cancel button again
                cancelLogButton.setVisible(false);
            }
        }
    }

    /**
     * Deletes the selected tour log.
     *
     * @param actionEvent triggered by the Delete Log button
     */
    public void onDeleteLog(ActionEvent actionEvent) {
        if (noCurrentAction() && tourLogs.getSelectionModel().getSelectedItem() != null) {
            //nimmt selected
            TourLog selectedTourLog = tourLogs.getSelectionModel().getSelectedItem();

            // called deleteTourLog methode aus MainModel
            if(!model.deleteTourLog(selectedTourLog)) {
                // TODO: show error!
            }
            // Recalibrate the popularity of affected tour
            else if (!tourPopularityRecalibration(tourList.getSelectionModel().getSelectedItem())){
                // TODO: show error!
            }
            // Recalibrate the child friendliness of affected tour
            else if (!tourChildFriendlinessRecalibration(tourList.getSelectionModel().getSelectedItem())){
                // TODO: show error!
            }

            //For Debugging: Print out all remaining tour logs
            //System.out.println("Remaining tour logs:");
            //for (TourLog log : tourLogs.getItems()) {
            //    System.out.println(log);  // You might want to customize this print statement if necessary
            //}

            // Refresh the Tour table
            refreshTourList();

            // Refresh the log table
            tourLogs.refresh();
        }
    }

    /**
     * Imports tour data from a file.
     *
     * @param actionEvent triggered by the Import menu item
     */
    public void onImportFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import File");

        // Set file type filters
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Tourplanner Files", "*.tourplanner"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
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

    /**
     * Exports tour data to a file.
     *
     * @param actionEvent triggered by the Export menu item
     */
    public void onExportFile(ActionEvent actionEvent) {
        Tour selectedTour = tourList.getSelectionModel().getSelectedItem();

        // Check if a tour is selected
        if (selectedTour == null) return;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Tour Data");

        // Set file type filters
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Tourplanner Files", "*.tourplanner"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        // Suggest default file name
        fileChooser.setInitialFileName(selectedTour.getName() + ".tourplanner");

        // Get the current stage
        Stage stage = (Stage) ((MenuItem) actionEvent.getSource()).getParentPopup().getOwnerWindow();

        // Show save file dialog
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                // Set object information
                String content = "Tour Name: " + selectedTour.getName() + "\n" +
                        "Description: " + selectedTour.getDescription() + "\n" +
                        "From: " + selectedTour.getFromLocation() + "\n" +
                        "To: " + selectedTour.getToLocation() + "\n" +
                        "Transport Type: " + selectedTour.getTransportType() + "\n";

                writer.write(content);
                System.out.println("File saved to: " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File save cancelled.");
        }
    }


    /**
     * Handles application exit.
     *
     * @param actionEvent triggered by the Exit menu item
     */
    public void onExitWindow(ActionEvent actionEvent) {
    }

    /**
     * Searches for tours.
     *
     * @param actionEvent triggered by the Tour Search field or button
     */
    public void onTourSearch(ActionEvent actionEvent) {
        refreshTourList();
    }

    /**
     * Searches for tour logs.
     *
     * @param actionEvent triggered by the Log Search field or button
     */
    public void onLogSearch(ActionEvent actionEvent) {
        String search = logSearchField.getText() != null ? logSearchField.getText().toLowerCase() : "";
        Tour selectedTour = tourList.getSelectionModel().getSelectedItem();

        if (selectedTour == null) {
            tourLogs.setItems(FXCollections.observableArrayList());
            return;
        }

        FilteredList<TourLog> result = new FilteredList<>(model.getTourLogs(), log -> {
            boolean belongsToSelectedTour = log.getTourName().equalsIgnoreCase(selectedTour.getName());

            if (search.isBlank()) {
                return belongsToSelectedTour;
            }

            boolean matchesSearch = log.getComment().toLowerCase().contains(search)
                    || log.getTourName().toString().toLowerCase().contains(search);

            return belongsToSelectedTour && matchesSearch;
        });

        tourLogs.setItems(result);
    }

    /**
     * Create tourReport PDF
     *
     * @param actionEvent triggered by the Generate Tour Report menu item
     */
    public void onGenTourReport(ActionEvent actionEvent) {
        Tour selectedTour = tourList.getSelectionModel().getSelectedItem();
        if (selectedTour == null) return;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Generating Tour Report for " + selectedTour.getName() + " ...");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Pdf Files", "*.pdf"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        fileChooser.setInitialFileName(selectedTour.getName() + "_tour_report.pdf");

        Stage stage = (Stage) ((MenuItem) actionEvent.getSource()).getParentPopup().getOwnerWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null){
            Task<Void> task = new Task<>() {
                @Override
                protected Void call(){
                    try{
                        model.exportTourPdf(file, selectedTour);
                    } catch (IOException e) {
                        // Handle error in UI
                        System.err.println("Error while exporting Tour PDF: " + e.getMessage());
                    }
                    return null;
                }
            };
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        }
    }
    /*
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
    */

    /**
     * Create summaryReport PDF
     *
     * @param actionEvent triggered by the Generate Summary Report menu item
     */
    public void onGenSummaryReport(ActionEvent actionEvent) {
        Tour selectedTour = tourList.getSelectionModel().getSelectedItem();
        if (selectedTour == null) return;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Generating Summary Report for " + selectedTour.getName() + " ...");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Pdf Files", "*.pdf"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        fileChooser.setInitialFileName(selectedTour.getName() + "_summary_report.pdf");

        Stage stage = (Stage) ((MenuItem) actionEvent.getSource()).getParentPopup().getOwnerWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null){
            Task<Void> task = new Task<>() {
                @Override
                protected Void call(){
                    try{
                        model.exportSummaryPdf(file, selectedTour);
                    } catch (IOException e) {
                        // Handle error in UI
                        System.err.println("Error while exporting Summary PDF: " + e.getMessage());
                    }
                    return null;
                }
            };
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        }
    }
    /*
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
    */

    /**
     * Cancels tour creation or editing.
     *
     * @param actionEvent triggered by the Cancel Tour button
     */
    public void onCancelTour(ActionEvent actionEvent) {
        // Change button text
        if (addTourButton.getText().equals("Confirm")) {
            addTourButton.setText("Add");
        }

        // Change button text
        if (editTourButton.getText().equals("Apply")) {
            editTourButton.setText("Edit");
        }

        // Disable the fout fields
        disableTourFields(true);

        // Clean error outputs
        model.setErrorField("");

        // Enable choosing tours
        tourList.setDisable(false);

        // Enable text search for tours
        tourSearchField.setDisable(false);
        tourSearchButton.setDisable(false);

        // Hide button
        cancelTourButton.setVisible(false);
    }

    /**
     * Cancels log creation or editing.
     *
     * @param actionEvent triggered by the Cancel Log button
     */
    public void onCancelLog(ActionEvent actionEvent) {
        if (addLogButton.getText().equals("Confirm")) {
            this.model.getTourLogs().remove(this.model.getTourLogs().size() - 1);

            addLogButton.setText("Add Log");
        }
        if (editLogButton.getText().equals("Confirm")) { editLogButton.setText("Edit Log"); }
        // TODO: dis select or clear fields!

        // Disable table editable
        tourLogs.setEditable(false);

        // Clean error outputs
        model.setErrorField("");

        // Enable choosing tours
        tourList.setDisable(false);

        // Enable text search for tour logs
        logSearchField.setDisable(false);
        logSearchButton.setDisable(false);
        // Enable text search for tours
        tourSearchField.setDisable(false);
        tourSearchButton.setDisable(false);

        // Hide button
        cancelLogButton.setVisible(false);
    }
}