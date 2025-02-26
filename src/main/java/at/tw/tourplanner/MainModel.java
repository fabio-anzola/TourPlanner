package at.tw.tourplanner;

import at.tw.tourplanner.object.Tour;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainModel {

    private final ObservableList<Tour> tours = FXCollections.observableArrayList();

    public ObservableList<Tour> getTours() {
        // TODO: Implement REST GET
        return tours;
    }

    public int addTour(Tour tour) {
        // TODO: Implement REST PUT
        tours.add(tour);
        return 0; // Error code
    }

    public int deleteTour(String name) {
        // TODO: Implement REST DELETE
        boolean removed = tours.removeIf(t -> t.getName().equals(name));
        return removed ? 0 : 1; // Error code
    }

    public int editTour(Tour updatedTour) {
        return tours.stream()
                .filter(t -> t.getName().equals(updatedTour.getName()))
                .findFirst()
                .map(t -> {
                    t.setDescription(updatedTour.getDescription());
                    t.setFromLocation(updatedTour.getFromLocation());
                    t.setToLocation(updatedTour.getToLocation());
                    return 0;
                })
                .orElse(1);
    }
}
