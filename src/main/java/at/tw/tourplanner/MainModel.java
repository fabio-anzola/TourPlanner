package at.tw.tourplanner;

import at.tw.tourplanner.object.Tour;
import at.tw.tourplanner.object.TransportType;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MainModel {

    private final ObservableList<Tour> tours = FXCollections.observableArrayList();


    private final Tour fieldTour = new Tour(TransportType.DEFAULT, "", "", "", "");

    public ObservableList<Tour> getTours() {
        // TODO: Implement REST GET
        return tours;
    }

    public boolean addTour() {
        if (fieldTour.getName() == null || fieldTour.getName().isBlank()) {
            return false;
        }
        if (tours.stream().anyMatch(t -> t.getName().equalsIgnoreCase(fieldTour.getName()))) {
            return false;
        }
        boolean msg = tours.add(new Tour(fieldTour.getTransportType() ,fieldTour.getName(), fieldTour.getDescription(), fieldTour.getFromLocation(), fieldTour.getToLocation()));
        fieldTour.clearProperties();
        return msg;
    }

    public boolean deleteTour() {
        String name = fieldTour.getName();
        return tours.removeIf(t -> t.getName().equals(name));
    }

    //falls du diese Methode bevorzugst, nenn sie um zu deleteTour und lösch die alte deleteTour
    /*
    public int deleteTourObject(Tour tour) {
        return tours.remove(tour) ? 0 : -1; // Return 0 if removed, -1 otherwise
    }*/

    public boolean editTour() {
        Tour edited = getFieldTour();
        if (edited == null) {
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
                return true;
            }
        }
        return false;
    }

    public Tour getFieldTour() {
        return fieldTour;
    }
}
