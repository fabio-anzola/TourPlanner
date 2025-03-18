package at.tw.tourplanner;

import at.tw.tourplanner.object.Tour;
import at.tw.tourplanner.object.TourLog;
import at.tw.tourplanner.object.TransportType;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class MainModel {

    @Getter
    private final ObservableList<Tour> tours = FXCollections.observableArrayList();

    @Getter
    private final ObservableList<TourLog> tourLogs = FXCollections.observableArrayList();

    private final StringProperty errorField = new SimpleStringProperty();

    @Getter
    private final Tour fieldTour = new Tour(TransportType.DEFAULT, null, "", "", "", "");

    @Getter
    private TourLog currentTourLog = new TourLog(LocalDate.now().toString(), "", 0, 0, 0, 0, "");

    public MainModel() {
        tours.add(new Tour(
                TransportType.WALK,
                new Image(Objects.requireNonNull(getClass().getResource("/routeImages/placeholder_map.png")).toExternalForm()),
                "Hiking Tour #1",
                "Sunday Family Hiking Tour",
                "Wien",
                "Burgenland"
        ));
        tourLogs.add(new TourLog(LocalDate.now().toString(), "tolle tour!", 5, 10, 1900, 1, "Hiking Tour #1"));
    }

    public boolean addTour() {
        if (!validateTourField(getFieldTour())) {
            return false;
        }

        boolean msg = tours.add(new Tour(fieldTour.getTransportType(), fieldTour.getRouteImage(), fieldTour.getName(), fieldTour.getDescription(), fieldTour.getFromLocation(), fieldTour.getToLocation()));

        // Clean up
        fieldTour.clearProperties();
        setErrorField("");

        return msg;
    }

    public boolean addTourLog() {
        if (!validateTourLog()) {
            return false;
        }

        currentTourLog = new TourLog(LocalDate.now().toString(), "", 0, 0, 0, 0, "");
        setErrorField("");

        return true;
    }

    public boolean deleteTour() {
        String name = fieldTour.getName();
        return tours.removeIf(t -> t.getName().equals(name));
    }

    public boolean editTour(String initialName) {
        Tour edited = getFieldTour();
        if (edited == null) {
            return false;
        }
        if (!validateTourField(edited, initialName)) {
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

    public StringProperty errorFieldProperty() {
        return errorField;
    }

    public void setErrorField(String errorField) {
        this.errorField.set(errorField);
    }

    private boolean validateTourField(Tour tour, String... excludedTourName) {
        if (tour.getName() == null || tour.getName().isBlank()) {
            setErrorField("Please enter a valid tour name");
            return false;
        }
        if (tours.stream().anyMatch(t -> t.getName().equalsIgnoreCase(tour.getName()))) {
            if (excludedTourName.length > 0) { // exclude provided
                if (!tour.getName().equalsIgnoreCase(excludedTourName[0])) { // check if match is equal to excluded - in not then enter
                    setErrorField("Tour name already exists");
                    return false;
                }
            } else { // no exclude provided
                setErrorField("Tour name already exists");
                return false;
            }
        }
        if (tour.getDescription() == null || tour.getDescription().isBlank()) {
            setErrorField("Please enter a valid tour description");
            return false;
        }
        if (tour.getFromLocation() == null || tour.getFromLocation().isBlank()) {
            setErrorField("Please enter a valid fromLocation");
            return false;
        }
        if (tour.getToLocation() == null || tour.getToLocation().isBlank()) {
            setErrorField("Please enter a valid toLocation");
            return false;
        }
        if (tour.getTransportType() == null ||
                (tour.getTransportType() instanceof TransportType && ((TransportType) tour.getTransportType()).equals(TransportType.DEFAULT))
        ) {
            setErrorField("Please enter a valid transportType");
            return false;
        }
        return true;
    }

    private boolean validateTourLog() {
        // Check date
        if (getCurrentTourLog().getDate() == null || getCurrentTourLog().getDate().isBlank()) {
            setErrorField("Please enter a tour date");
            return false;
        }
        try {
            LocalDate ld = LocalDate.parse(getCurrentTourLog().getDate());
        } catch (DateTimeParseException e) {
            setErrorField("Please enter a valid tour date (YYYY-MM-DD)");
            return false;
        }

        // Check Comment
        if (getCurrentTourLog().getComment() == null || getCurrentTourLog().getComment().isBlank()) {
            setErrorField("Please enter a comment");
            return false;
        }

        // Check difficulty
        if (getCurrentTourLog().getDifficulty() == null || getCurrentTourLog().getDifficulty().isBlank()) {
            setErrorField("Please enter a difficulty");
            return false;
        }
        try {
            if (getCurrentTourLog().getParsedDifficulty() < 0 || getCurrentTourLog().getParsedDifficulty() > 5) {
                setErrorField("Please enter a valid difficulty (range from 0 to 5 where 5 is the most difficult)");
                return false;
            }
        } catch (NumberFormatException e) {
            setErrorField("Difficulty must be an integer");
            return false;
        }

        // Check Total Distance
        if (getCurrentTourLog().getTotalDistance() == null || getCurrentTourLog().getTotalDistance().isBlank()) {
            setErrorField("Please enter a total distance in meters");
            return false;
        }
        try {
            if (getCurrentTourLog().getParsedTotalDistance() < 0) {
                setErrorField("Please enter a valid total distance (> 0m)");
                return false;
            }
        } catch (NumberFormatException e) {
            setErrorField("Total distance must be an integer");
            return false;
        }

        // Check Total Time
        if (getCurrentTourLog().getTotalTime() == null || getCurrentTourLog().getTotalTime().isBlank()) {
            setErrorField("Please enter a total time in minutes");
            return false;
        }
        try {
            if (getCurrentTourLog().getParsedTotalTime() < 0) {
                setErrorField("Please enter a valid total time (> 0 minutes)");
                return false;
            }
        } catch (NumberFormatException e) {
            setErrorField("Total Time must be an integer");
            return false;
        }

        // Check Rating
        if (getCurrentTourLog().getRating() == null || getCurrentTourLog().getRating().isBlank()) {
            setErrorField("Please enter a difficulty");
            return false;
        }
        try {
            if (getCurrentTourLog().getParsedRating() < 0 || getCurrentTourLog().getParsedRating() > 5) {
                setErrorField("Please enter a valid difficulty (range from 0 to 5 where 5 is the most difficult)");
                return false;
            }
        } catch (NumberFormatException e) {
            setErrorField("Rating must be an integer");
            return false;
        }


        return true;
    }
}
