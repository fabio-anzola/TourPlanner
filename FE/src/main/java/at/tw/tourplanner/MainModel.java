package at.tw.tourplanner;

import at.tw.tourplanner.logger.ILoggerWrapper;
import at.tw.tourplanner.logger.LoggerFactory;
import at.tw.tourplanner.object.Tour;
import at.tw.tourplanner.object.TourLog;
import at.tw.tourplanner.object.TransportType;
import at.tw.tourplanner.service.PdfGenerationService;
import at.tw.tourplanner.service.TourLogService;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class MainModel {

    private final TourLogService tourLogService = new TourLogService();

    /**
     * Observable list holding all tours in the application.
     */
    @Getter
    private final ObservableList<Tour> tours = FXCollections.observableArrayList();

    /**
     * Observable list holding all tour logs.
     */
    @Getter
    private final ObservableList<TourLog> tourLogs = FXCollections.observableArrayList();

    /**
     * Property used for displaying validation or application error messages.
     */
    private final StringProperty errorField = new SimpleStringProperty();

    /**
     * Tour object used for data binding in the UI form.
     */
    @Getter
    private final Tour fieldTour = new Tour(TransportType.DEFAULT, null, "", "", "", "", 0, 0);

    /**
     * Currently edited or added tour log.
     */
    @Getter
    private TourLog currentTourLog = new TourLog(LocalDate.now().toString(), "", 0, 0, 0, 0, "");

    // log4j
    private static final ILoggerWrapper logger = LoggerFactory.getLogger(MainApplication.class);

    /**
     * Constructs a MainModel and adds demo data for initial use.
     */
    public MainModel() {
        // Dummy Tour
        tours.add(new Tour(
                TransportType.WALK,
                new Image(Objects.requireNonNull(getClass().getResource("/routeImages/placeholder_map.png")).toExternalForm()),
                "Hiking Tour",
                "Sunday Family Hiking Tour",
                "Wien",
                "Burgenland",
                1,
                1
        ));
        // Dummy Tour Log
        tourLogs.add(new TourLog(LocalDate.now().toString(), "tolle tour!", 5, 10, 1900, 1, "Hiking Tour"));
    }

    /**
     * Adds the current fieldTour to the list of tours after validation.
     *
     * @return true if the tour was successfully added; false otherwise
     */
    public boolean addTour() {
        logger.debug("Entered function: addTour (MainModel)");
        if (!validateTourField(getFieldTour())) {
            logger.warn("Failed to validate tour " + getFieldTour());
            return false;
        }

        boolean msg = tours.add(new Tour(fieldTour.getTransportType(), fieldTour.getRouteImage(), fieldTour.getName(), fieldTour.getDescription(), fieldTour.getFromLocation(), fieldTour.getToLocation(), 0, -1));

        // Clean up
        fieldTour.clearProperties();
        setErrorField("");

        return msg;
    }

    /**
     * Validates the currentTourLog and resets the log after success.
     *
     * @return true if validation passed; false otherwise
     */
    public boolean addTourLog() {
        logger.debug("Entered function: addTourLog (MainModel)");
        if (!validateTourLog()) {
            logger.warn("Failed to validate tour log " + getCurrentTourLog());
            return false;
        }

        try {
            // Send to backend
           tourLogService.addTourLog(currentTourLog);

            // Reload logs from backend to ensure full sync
            reloadTourLogs();

            // Clear current input
            currentTourLog = new TourLog(LocalDate.now().toString(), "", 0, 0, 0, 0, fieldTour.getName());
            setErrorField("");

            return true;
        } catch (Exception e) {
            setErrorField("Failed to save Tour Log");
            logger.error("Failed to save Tour Log to server: " + e + ", Message: " + e.getCause());
            return false;
        }
    }

    public void reloadTourLogs() {
        logger.debug("Entered function: reloadTourLogs (MainModel)");
        if (fieldTour.getName() == null || fieldTour.getName().isBlank()) {
            logger.warn("no tour selected");
            return;
        }

        try {
            List<TourLog> updatedLogs = tourLogService.getTourLogsByTourName(fieldTour.getName());
            tourLogs.setAll(updatedLogs); // Replaces entire list
            setErrorField("");
        } catch (Exception e) {
            setErrorField("Could not reload logs");
            logger.error("Could not reload logs: " + e + ", Message: " + e.getCause());
        }
    }

    /**
     * Deletes the currently selected tour from the list.
     *
     * @return true if the tour was successfully removed
     */
    public boolean deleteTour() {
        logger.debug("Entered function: deleteTour (MainModel)");
        String name = fieldTour.getName();
        return tours.removeIf(t -> t.getName().equals(name));
    }

    /**
     * Updates an existing tour's details with the values from fieldTour.
     *
     * @param initialName the original name of the tour before editing
     * @return true if the update was successful; false otherwise
     */
    public boolean editTour(String initialName) {
        logger.debug("Entered function: editTour (MainModel) with parameter: " + initialName);
        Tour edited = getFieldTour();
        if (edited == null) {
            logger.warn("could not edit tour");
            return false;
        }
        if (!validateTourField(edited, initialName)) {
            logger.warn("Failed to validate edited tour");
            return false;
        }

        // Find a tour with the same name in the list
        for (Tour t : tours) {
            if (t.getName().equalsIgnoreCase(edited.getName())) {
                // Update fields. If Tour uses properties binding will update the UI.
                t.setDescription(edited.getDescription());
                t.setFromLocation(edited.getFromLocation());
                t.setToLocation(edited.getToLocation());
                t.setTransportType(edited.getTransportType());

                // Clean up
                setErrorField("");

                return true;
            }
        }
        return false;
    }

    /**
     * Provides the property used to bind error messages.
     *
     * @return the errorField property
     */
    public StringProperty errorFieldProperty() {
        logger.debug("Entered function: errorFieldProperty (MainModel)");
        return errorField;
    }

    /**
     * Updates the error message shown to the user.
     *
     * @param errorField the new error message to set
     */
    public void setErrorField(String errorField) {
        logger.debug("Entered function: setErrorField (MainModel) with parameter: " + errorField);
        this.errorField.set(errorField);
    }


    /**
     * Validates the given tour object, including checks for required fields
     * and name uniqueness. Optionally excludes a specific tour name from name checks.
     *
     * @param tour the Tour to validate
     * @param excludedTourName optional original tour name to skip in duplicate check
     * @return true if valid, false otherwise
     */
    private boolean validateTourField(Tour tour, String... excludedTourName) {
        logger.debug("Entered function: validateTourField (MainModel) with parameter: " + tour + " and " + Arrays.toString(excludedTourName));
        if (tour.getName() == null || tour.getName().isBlank()) {
            setErrorField("Please enter a valid tour name");
            logger.warn("User selected invalid tour name: " + tour.getName());
            return false;
        }
        if (tours.stream().anyMatch(t -> t.getName().equalsIgnoreCase(tour.getName()))) {
            if (excludedTourName.length > 0) { // exclude provided
                if (!tour.getName().equalsIgnoreCase(excludedTourName[0])) { // check if match is equal to excluded - in not then enter
                    setErrorField("Tour name already exists");
                    logger.warn("User selected already taken tour name: " + tour.getName());
                    return false;
                }
            } else { // no exclude provided
                setErrorField("Tour name already exists");
                logger.warn("User selected already taken tour name: " + tour.getName());
                return false;
            }
        }
        if (tour.getDescription() == null || tour.getDescription().isBlank()) {
            setErrorField("Please enter a valid tour description");
            logger.warn("User selected invalid tour description: " + tour.getDescription());
            return false;
        }
        if (tour.getFromLocation() == null || tour.getFromLocation().isBlank()) {
            setErrorField("Please enter a valid fromLocation");
            logger.warn("User selected invalid fromLocation: " + tour.getFromLocation());
            return false;
        }
        if (tour.getToLocation() == null || tour.getToLocation().isBlank()) {
            setErrorField("Please enter a valid toLocation");
            logger.warn("User selected invalid toLocation: " + tour.getToLocation());
            return false;
        }
        if (tour.getTransportType() == null ||
                (tour.getTransportType() instanceof TransportType && ((TransportType) tour.getTransportType()).equals(TransportType.DEFAULT))
        ) {
            setErrorField("Please enter a valid transportType");
            logger.warn("User selected invalid transportType: " + tour.getTransportType());
            return false;
        }
        return true;
    }

    /**
     * Validates the currentTourLog
     *
     * @return true if all fields are valid; false otherwise
     */
    private boolean validateTourLog() {
        logger.debug("Entered function: validateTourLog (MainModel)");
        // Check date
        if (getCurrentTourLog().getDate() == null || getCurrentTourLog().getDate().isBlank()) {
            setErrorField("Please enter a tour date");
            logger.warn("User selected empty tour date: " + getCurrentTourLog().getDate());
            return false;
        }
        try {
            LocalDate ld = LocalDate.parse(getCurrentTourLog().getDate());
        } catch (DateTimeParseException e) {
            setErrorField("Please enter a valid tour date (YYYY-MM-DD)");
            logger.warn("User selected invalid tour date: " + getCurrentTourLog().getDate());
            return false;
        }

        // Check Comment
        if (getCurrentTourLog().getComment() == null || getCurrentTourLog().getComment().isBlank()) {
            setErrorField("Please enter a comment");
            logger.warn("User selected empty comment: " + getCurrentTourLog().getComment());
            return false;
        }

        // Check difficulty
        if (getCurrentTourLog().getDifficulty() == null || getCurrentTourLog().getDifficulty().isBlank()) {
            setErrorField("Please enter a difficulty");
            logger.warn("User selected empty difficulty: " + getCurrentTourLog().getDifficulty());
            return false;
        }
        try {
            if (getCurrentTourLog().getParsedDifficulty() < 0 || getCurrentTourLog().getParsedDifficulty() > 5) {
                setErrorField("Please enter a valid difficulty (range from 0 to 5 where 5 is the most difficult)");
                logger.warn("User selected invalid difficulty range: " + getCurrentTourLog().getDifficulty());
                return false;
            }
        } catch (NumberFormatException e) {
            setErrorField("Difficulty must be an integer");
            logger.warn("User selected invalid difficulty data type: " + getCurrentTourLog().getDifficulty());
            return false;
        }

        // Check Total Distance
        if (getCurrentTourLog().getTotalDistance() == null || getCurrentTourLog().getTotalDistance().isBlank()) {
            setErrorField("Please enter a total distance in meters");
            logger.warn("User selected empty total distance");
            return false;
        }
        try {
            if (getCurrentTourLog().getParsedTotalDistance() < 0) {
                setErrorField("Please enter a valid total distance (> 0m)");
                logger.warn("User selected invalid total distance range: " + getCurrentTourLog().getTotalDistance());
                return false;
            }
        } catch (NumberFormatException e) {
            setErrorField("Total distance must be an integer");
            logger.warn("User selected invalid total distance data type: " + getCurrentTourLog().getTotalDistance());
            return false;
        }

        // Check Total Time
        if (getCurrentTourLog().getTotalTime() == null || getCurrentTourLog().getTotalTime().isBlank()) {
            setErrorField("Please enter a total time in minutes");
            logger.warn("User selected empty total time");
            return false;
        }
        try {
            if (getCurrentTourLog().getParsedTotalTime() < 0) {
                setErrorField("Please enter a valid total time (> 0 minutes)");
                logger.warn("User selected invalid total time range: " + getCurrentTourLog().getTotalTime());
                return false;
            }
        } catch (NumberFormatException e) {
            setErrorField("Total Time must be an integer");
            logger.warn("User selected invalid total time data type: " + getCurrentTourLog().getTotalTime());
            return false;
        }

        // Check Rating
        if (getCurrentTourLog().getRating() == null || getCurrentTourLog().getRating().isBlank()) {
            setErrorField("Please enter a difficulty");
            logger.warn("User selected empty difficulty: " + getCurrentTourLog().getDifficulty());
            return false;
        }
        try {
            if (getCurrentTourLog().getParsedRating() < 0 || getCurrentTourLog().getParsedRating() > 5) {
                setErrorField("Please enter a valid difficulty (range from 0 to 5 where 5 is the most difficult)");
                logger.warn("User selected invalid difficulty range: " + getCurrentTourLog().getDifficulty());
                return false;
            }
        } catch (NumberFormatException e) {
            setErrorField("Rating must be an integer");
            logger.warn("User selected invalid difficulty data type: " + getCurrentTourLog().getDifficulty());
            return false;
        }
        return true;
    }

    /**
     * Validates and prepares the currentTourLog for editing.
     *
     * @return true if preparation succeeded (e.g. a tour is selected)
     */
    public boolean addTourLogPreCheck() {
        logger.debug("Entered function: addTourLogPreCheck (MainModel)");
        // TODO: check if a tour is selected
        if (fieldTour.getName() == null || fieldTour.getName().isBlank()) {
            logger.warn("no tour selected");
            return false;
        } else {
            this.currentTourLog = new TourLog(LocalDate.now().toString(), "", 0, 0, 0, 0, fieldTour.getName());
            this.tourLogs.add(this.currentTourLog);
            return true;
        }
    }

    /**
     * Deletes the given tour log from the observable list.
     *
     * @param tourLog the log to delete
     * @return true if the log was removed; false if not found or null
     */
    public boolean deleteTourLog(TourLog tourLog) {
        logger.debug("Entered function: deleteTourLog (MainModel) with parameter: " + tourLog);
        if (tourLog != null) {
            return tourLogs.remove(tourLog);  // Removes the specified TourLog from the list
        }
        logger.warn("no tour log selected to delete");
        return false;
    }

    /**
     * Sets tour popularity
     *
     * @param tour the tour object
     * @return true if the popularity was set; otherwise false
     */
    public boolean setTourPopularity(Tour tour) {
        logger.debug("Entered function: setTourPopularity (MainModel) with parameter: " + tour);
        long tourLogCount = tourLogs.stream().filter(log -> log.getTourName().equals(tour.getName())).count();
        if(tourLogCount < 0 || tour == null) {
            logger.warn("invalid log count or no tour selected: " + tourLogCount);
            return false;
        } else {
            tour.setPopularity((int)tourLogCount);
            return true;
        }
    }

    /**
     * Sets tour popularity
     *
     * @return true if the child friendliness was set; otherwise false
     */
    public boolean setTourChildFriendliness(){
        logger.debug("Entered function: setTourChildFriendliness (MainModel)");
        if(fieldTour == null) {
            logger.warn("no tour selected");
            return false;
        }

        List<TourLog> matchingTourLogs = tourLogs.stream().filter(log -> log.getTourName().equals(fieldTour.getName())).toList();

        if(matchingTourLogs.isEmpty()) {
            fieldTour.setChildFriendliness(-1);
            System.out.println(-1);
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

        fieldTour.setChildFriendliness(childFriendliness);
        return true;
    }

    /**
     * Creates a Tour report
     *
     * @param file the file to be written to
     */
    public void exportTourPdf(File file) throws IOException {
        logger.debug("Entered function: exportTourPdf (MainModel) with parameter: " + file);
        new PdfGenerationService(file).generateTourPdf(fieldTour, tourLogs.stream().filter(log -> log.getTourName().equals(fieldTour.getName())).toList());
    }

    /**
     * Creates a Summary report
     *
     * @param file the file to be written to
     */
    public void exportSummaryPdf(File file) throws IOException {
        logger.debug("Entered function: exportSummaryPdf (MainModel) with parameter: " + file);
        new PdfGenerationService(file).generateSummaryPdf(tours, tourLogs);
    }
}
