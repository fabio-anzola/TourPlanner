package at.tw.tourplanner;

import at.tw.tourplanner.object.Tour;
import at.tw.tourplanner.object.TourLog;
import at.tw.tourplanner.object.TransportType;
import com.sun.jdi.connect.Transport;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.image.Image;
import lombok.Getter;

import java.security.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.Date;

public class MainModel {

    @Getter
    private final ObservableList<Tour> tours = FXCollections.observableArrayList();
    @Getter
    private final ObservableList<TourLog> tourLogs = FXCollections.observableArrayList();

    private final StringProperty errorField = new SimpleStringProperty();

    @Getter
    private final Tour fieldTour = new Tour(TransportType.DEFAULT, null, "", "", "", "");

    //Ugly Hard code attack
    public  MainModel() {
        tours.add(new Tour(
                TransportType.WALK,
                new Image(Objects.requireNonNull(getClass().getResource("/routeImages/placeholder_map.png")).toExternalForm()),
                "Hiking Tour #1",
                "Sunday Family Hiking Tour",
                "Wien",
                "Burgenland"
        ));
        tourLogs.add(new TourLog(LocalDate.now(), "tolle tour!", 5, 10, 1900, 1, "Hiking Tour #1"));
    }

    public boolean addTour() {
        if (!validateField(getFieldTour())) {
            return false;
        }

        boolean msg = tours.add(new Tour(fieldTour.getTransportType(), fieldTour.getRouteImage() ,fieldTour.getName(), fieldTour.getDescription(), fieldTour.getFromLocation(), fieldTour.getToLocation()));

        // Clean up
        fieldTour.clearProperties();
        setErrorField("");

        return msg;
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
        if (!validateField(edited, initialName)) {
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

    private boolean validateField(Tour tour, String... excludedTourName) {
        if (tour.getName() == null || tour.getName().isBlank()) {
            setErrorField("Please enter a valid tour name");
            return false;
        }
        if (tours.stream().anyMatch(t -> t.getName().equalsIgnoreCase(tour.getName()))) {
            if (excludedTourName.length > 0) { // exclude provided
                if(!tour.getName().equalsIgnoreCase(excludedTourName[0])) { // check if match is equal to excluded - in not then enter
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
                (tour.getTransportType() instanceof TransportType && ((TransportType)tour.getTransportType()).equals(TransportType.DEFAULT))
        ) {
            setErrorField("Please enter a valid transportType");
            return false;
        }
        return true;
    }
}
