package at.tw.tourplanner;

import at.tw.tourplanner.logger.ILoggerWrapper;
import at.tw.tourplanner.logger.LoggerFactory;
import at.tw.tourplanner.object.RouteData;
import at.tw.tourplanner.object.Tour;
import at.tw.tourplanner.object.TourLog;
import at.tw.tourplanner.object.TransportType;
import at.tw.tourplanner.service.RouteImageService;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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

    @FXML
    private VBox spinnerOverlay;

    @FXML
    private ImageView spinnerGif;

    @FXML
    private StackPane mapStack;

    PauseTransition pt = new PauseTransition();

    // log4j
    private static final ILoggerWrapper logger = LoggerFactory.getLogger(MainApplication.class);

    /**
     * Initializes UI bindings and event listeners.
     */
    @FXML
    public void initialize() {
        spinnerGif.setImage(new Image(Objects.requireNonNull(getClass().getResource("/spinner.gif")).toExternalForm()));
        spinnerOverlay.setVisible(false);
        mapStack.getChildren().removeIf(node -> node instanceof WebView);

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
                tourLogs.setItems(FXCollections.observableArrayList());

                model.getFieldTour().clearProperties();
                model.getCurrentTourLog().setTourName("");
            } else {
                // Update fieldTour and currentTourLog
                model.getFieldTour().setName(newTour.getName());
                model.getFieldTour().setDescription(newTour.getDescription());
                model.getFieldTour().setFromLocation(newTour.getFromLocation());
                model.getFieldTour().setToLocation(newTour.getToLocation());
                model.getFieldTour().setTransportType(newTour.getTransportType());
                model.getFieldTour().setRouteImage(newTour.getRouteImage());
                model.getCurrentTourLog().setTourName(newTour.getName());

                // Reload logs from backend
                model.reloadTourLogs();

                // Update UI table with filtered logs
                tourLogs.setItems(new FilteredList<>(model.getTourLogs(), log -> log.getTourName().equalsIgnoreCase(newTour.getName())));
            }
        });

        tourLogs.getSelectionModel().selectedItemProperty().addListener((obs, oldTourLog, newTourLog) -> {
            if (newTourLog == null) {
                model.getCurrentTourLog().setId(-1);
            } else {
                model.getCurrentTourLog().setId(newTourLog.getId());
            }
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
        logger.debug("Entered function: disableTourFields (MainController) with parameter: " + b);
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
    private boolean noCurrentAction() {
        logger.debug("Entered function: noCurrentAction (MainController)");
        return addTourButton.getText().equals("Add") &&
                editTourButton.getText().equals("Edit") &&
                addLogButton.getText().equals("Add Log") &&
                editLogButton.getText().equals("Edit Log");
    }

    /**
     * Refreshes the tour list
     */
    public void refreshTourList() {
        logger.debug("Entered function: refreshTourList (MainController)");
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
        logger.debug("Entered function: tourPopularityRecalibration (MainController) with parameter: " + selectedTour);
        if(selectedTour != null){
            return model.setTourPopularity(selectedTour);
        }
        logger.warn("No tour selected");
        return false;
    }

    /**
     * Handles recalibration of the child friendliness of a tour
     *
     * @param selectedTour contains tour object
     * @return true if child friendliness was successfully set, false otherwise.
     */
    private boolean tourChildFriendlinessRecalibration(Tour selectedTour){
        logger.debug("Entered function: tourChildFriendlinessRecalibration (MainController) with parameter: " + selectedTour);
        if(selectedTour != null){
            return model.setTourChildFriendliness();
        }
        logger.warn("No tour selected");
        return false;
    }


    /**
     * Handles adding a new tour.
     *
     * @param actionEvent triggered by the Add Tour button
     */
    public void onAddTour(ActionEvent actionEvent) {
        logger.info("User clicked: " + actionEvent.getSource());
        if (noCurrentAction()) {
            logger.debug("Entered if statement: onAddTour (MainController)");
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
            logger.debug("Entered else if statement: onAddTour (MainController)");

            Task<Boolean> task = new Task<>() {
                @Override
                protected Boolean call() {
                    return model.addTour();
                }
            };

            task.setOnSucceeded(event -> {
                if (task.getValue()) {
                    refreshTourList();
                    tourList.setDisable(false);
                    tourSearchField.setDisable(false);
                    tourSearchButton.setDisable(false);
                    disableTourFields(true);
                    addTourButton.setText("Add");
                    cancelTourButton.setVisible(false);
                } else {
                    logger.error("Failed to add tour");
                }
            });

            new Thread(task).start();
        }
    }

    /**
     * Handles editing the selected tour.
     *
     * @param actionEvent triggered by the Edit Tour button
     */
    public void onEditTour(ActionEvent actionEvent) {
        logger.info("User clicked: " + actionEvent.getSource());
        //ein item muss ausgewählt sein damit man edit verwenden kann
        if (noCurrentAction() && tourList.getSelectionModel().getSelectedItem() != null) {
            logger.debug("Entered if statement: onEditTour (MainController)");
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
            logger.debug("Entered else if statement: onEditTour (MainController)");

            String initialName = tourList.getSelectionModel().getSelectedItem().getName();
            Task<Boolean> task = new Task<>() {
                @Override
                protected Boolean call() {
                    return model.editTour(initialName);
                }
            };

            task.setOnSucceeded(e -> {
                if (!task.getValue()) {
                    logger.error("Failed to edit tour");
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
            });

            new Thread(task).start();
        }
    }


    /**
     * Deletes the selected tour.
     *
     * @param actionEvent triggered by the Delete Tour button
     */
    public void onDeleteTour(ActionEvent actionEvent) {
        logger.info("User clicked: " + actionEvent.getSource());
        if (noCurrentAction()) {
            Task<Boolean> task = new Task<>() {
                @Override
                protected Boolean call() {
                    return model.deleteTour();
                }
            };

            task.setOnSucceeded(e -> {
                if (!task.getValue()) {
                    logger.error("Failed to delete tour");
                } else {
                    refreshTourList();
                }
            });

            new Thread(task).start();
        }
    }

    /**
     * Calculates the route and displays it in the map view.
     *
     * @param actionEvent triggered by the Calculate Route button
     */
    public void onCalculateRoute(ActionEvent actionEvent) {
        Task<RouteData> task = new Task<>() {
            @Override
            protected RouteData call() throws Exception {
                return new RouteImageService().getRouteData(
                        model.getFieldTour().getFromLocation(),
                        model.getFieldTour().getToLocation(),
                        model.getFieldTour().getTransportType());
            }
        };

        task.setOnSucceeded(event -> {
            RouteData routeData = task.getValue();
            estimatedTime.setText(String.format("%.2f h", routeData.duration / 60));
            tourDistance.setText(String.format("%.2f km", routeData.distance / 100));

            try {
                JSONObject geoJson = new JSONObject(routeData.getGeoJson());
                WebView newWebView = new RouteImageService().createWebViewWithGeoJson(geoJson);
                mapStack.getChildren().removeIf(node -> node instanceof WebView); // clear old
                mapStack.getChildren().add(0, newWebView); // insert behind spinner
            } catch (Exception e) {
                logger.error("Error displaying route" + e);
            } finally {
                spinnerOverlay.setVisible(false);

                pt.setDuration(Duration.millis(5000));
                File captureFile = new File("cap.png");
                pt.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {

                        System.out.println("Taking screenshot");
                        System.out.println(captureFile.getAbsolutePath());

                        WritableImage wim = mapStack.getChildren().get(0).snapshot(null, null);
                        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(wim, null);
                        try {
                            //System.out.println(bufferedImage);
                            //ImageIO.write(bufferedImage, "png", captureFile);
                            ImageIO.write(bufferedImage, "png", captureFile);
                            model.getFieldTour().setRouteImage(bufferedImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                pt.play();
            }
        });

        task.setOnFailed(event -> {
            logger.error("Route calculation failed" + task.getException());
            spinnerOverlay.setVisible(false);
        });

        spinnerOverlay.setVisible(true);
        new Thread(task).start();
    }

    /**
     * Handles adding a new tour log.
     *
     * @param actionEvent triggered by the Add Log button
     */
    public void onAddLog(ActionEvent actionEvent) {
        logger.info("User clicked: " + actionEvent.getSource());
        // check if no action ongoing and if a tour is selected
        if (noCurrentAction() && tourList.getSelectionModel().getSelectedItem() != null) {
            logger.debug("Entered if statement: onAddLog (MainController)");
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
            logger.debug("Entered first else if statement: onAddLog (MainController)");
            Task<Boolean> task = new Task<>() {
                @Override
                protected Boolean call() {
                    return model.addTourLog();
                }
            };

            task.setOnSucceeded(e -> {
                if (!task.getValue()) {
                    logger.error("Failed to add tour log");
                    return;
                }

                Tour selectedTour = tourList.getSelectionModel().getSelectedItem();

                if (!tourPopularityRecalibration(selectedTour)) {
                    logger.error("Failed to change popularity of affected tour");
                } else if (!tourChildFriendlinessRecalibration(selectedTour)) {
                    logger.error("Failed to change child friendliness of affected tour");
                } else {
                    refreshTourList();
                    tourList.setDisable(false);
                    logSearchField.setDisable(false);
                    logSearchButton.setDisable(false);
                    tourSearchField.setDisable(false);
                    tourSearchButton.setDisable(false);
                    tourLogs.setEditable(false);
                    addLogButton.setText("Add Log");
                    cancelLogButton.setVisible(false);
                }
            });

            new Thread(task).start();

        } else if (tourList.getSelectionModel().getSelectedItem() != null) {
            logger.debug("Entered second else if statement: onAddLog (MainController)");
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
        logger.info("User clicked: " + actionEvent.getSource());
        if (noCurrentAction()){
            logger.debug("Entered if statement: onEditLog (MainController)");
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
            logger.debug("Entered else if statement: onEditLog (MainController)");

            Task<Boolean> task = new Task<>() {
                @Override
                protected Boolean call() {
                    // Recalibrate child friendliness first
                    boolean recalibrated = tourChildFriendlinessRecalibration(tourList.getSelectionModel().getSelectedItem());
                    if (!recalibrated) return false;

                    return model.editTourLog();
                }
            };

            task.setOnSucceeded(e -> {
                if (!task.getValue()) {
                    logger.error("Failed to edit tour log");
                    return;
                }

                // Restore UI
                tourList.setDisable(false);
                logSearchField.setDisable(false);
                logSearchButton.setDisable(false);
                tourSearchField.setDisable(false);
                tourSearchButton.setDisable(false);
                tourLogs.setEditable(false);
                editLogButton.setText("Edit Log");
                cancelLogButton.setVisible(false);
            });

            new Thread(task).start();
        }
    }

    /**
     * Deletes the selected tour log.
     *
     * @param actionEvent triggered by the Delete Log button
     */
    public void onDeleteLog(ActionEvent actionEvent) {
        logger.info("User clicked: " + actionEvent.getSource());
        if (noCurrentAction() && tourLogs.getSelectionModel().getSelectedItem() != null) {
            logger.debug("Entered if statement: onDeleteLog (MainController)");

            Task<Boolean> task = new Task<>() {
                @Override
                protected Boolean call() {
                    return model.deleteTourLog();
                }
            };

            task.setOnSucceeded(e -> {
                if (!task.getValue()) {
                    logger.error("Failed to delete tour log");
                } else {
                    // Recalibrate and refresh
                    if (!tourPopularityRecalibration(tourList.getSelectionModel().getSelectedItem())) {
                        logger.error("Failed to change popularity of affected tour");
                    } else if (!tourChildFriendlinessRecalibration(tourList.getSelectionModel().getSelectedItem())) {
                        logger.error("Failed to change child friendliness of affected tour");
                    }

                    refreshTourList();
                    tourLogs.refresh();
                }
            });

            new Thread(task).start();
        }
    }

    /**
     * Imports tour data from a file.
     *
     * @param actionEvent triggered by the Import menu item
     */
    public void onImportFile(ActionEvent actionEvent) {
        logger.info("User clicked: onImportFile (MainController)");
        if(!noCurrentAction()){
            logger.warn("actions ongoing, aborting import");
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Json Files", "*.json"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        Stage stage = (Stage) ((javafx.scene.control.MenuItem) actionEvent.getSource()).getParentPopup().getOwnerWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null){
            Task<Boolean> task = new Task<>() {
                @Override
                protected Boolean call(){
                    try{
                        model.importTourJson(selectedFile);
                    } catch (IOException e) {
                        logger.error("failed to import tour data, " + e + ", Message: " + e.getMessage());
                        return false;
                    }
                    return true;
                }
            };

            task.setOnSucceeded(event -> {
                if (task.getValue()) {
                    refreshTourList();
                }
            });

            Thread thread = new Thread(task);
            thread.start();
        }
    }

    /**
     * Exports tour data to a file.
     *
     * @param actionEvent triggered by the Export menu item
     */
    public void onExportFile(ActionEvent actionEvent) {
        logger.info("User clicked: onExportFile (MainController)");
        if(!noCurrentAction()){
            logger.warn("actions ongoing, aborting export");
            return;
        }
        if(model.getTours() == null || model.getTours().isEmpty()){
            logger.debug("No tour data to export");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Tour Data");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Json Files", "*.json"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        fileChooser.setInitialFileName("TourData.json");
        Stage stage = (Stage) ((MenuItem) actionEvent.getSource()).getParentPopup().getOwnerWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null){
            Task<Void> task = new Task<>() {
                @Override
                protected Void call(){
                    try{
                        model.exportTourJson(file);
                    } catch (IOException e) {
                        logger.error("failed to export tour data, " + e + ", Message: " + e.getCause());
                    }
                    return null;
                }
            };
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        }
    }


    /**
     * Handles application exit.
     *
     * @param actionEvent triggered by the Exit menu item
     */
    public void onExitWindow(ActionEvent actionEvent) {
        logger.info("User clicked: " + actionEvent.getSource());
        // TODO
    }

    /**
     * Searches for tours.
     *
     * @param actionEvent triggered by the Tour Search field or button
     */
    public void onTourSearch(ActionEvent actionEvent) {
        logger.info("User used: " + actionEvent.getSource());
        refreshTourList();
    }

    /**
     * Searches for tour logs.
     *
     * @param actionEvent triggered by the Log Search field or button
     */
    public void onLogSearch(ActionEvent actionEvent) {
        logger.info("User used: " + actionEvent.getSource());
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
        logger.info("User clicked: " + actionEvent.getSource());
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
                        model.exportTourPdf(file);
                    } catch (IOException e) {
                        logger.error("failed to generate Tour Report, " + e + ", Message: " + e.getCause());
                    }
                    return null;
                }
            };
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        }
    }

    /**
     * Create summaryReport PDF
     *
     * @param actionEvent triggered by the Generate Summary Report menu item
     */
    public void onGenSummaryReport(ActionEvent actionEvent) {
        logger.info("User clicked: " + actionEvent.getSource());
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Generating Summary Report...");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Pdf Files", "*.pdf"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        fileChooser.setInitialFileName("TourPlanner_summary_report.pdf");

        Stage stage = (Stage) ((MenuItem) actionEvent.getSource()).getParentPopup().getOwnerWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null){
            Task<Void> task = new Task<>() {
                @Override
                protected Void call(){
                    try{
                        model.exportSummaryPdf(file);
                    } catch (IOException e) {
                        logger.error("failed to generate Summary Report, " + e + ", Message: " + e.getCause());
                    }
                    return null;
                }
            };
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        }
    }

    /**
     * Cancels tour creation or editing.
     *
     * @param actionEvent triggered by the Cancel Tour button
     */
    public void onCancelTour(ActionEvent actionEvent) {
        logger.info("User clicked: " + actionEvent.getSource());
        // Change button text
        if (addTourButton.getText().equals("Confirm")) {
            addTourButton.setText("Add");
        }

        // Change button text
        if (editTourButton.getText().equals("Apply")) {
            editTourButton.setText("Edit");
        }

        // Disable the tour fields
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
        logger.info("User clicked: " + actionEvent.getSource());
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