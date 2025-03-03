package at.tw.tourplanner.object;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Tour {
    private final StringProperty name;
    private final StringProperty description;
    private final StringProperty fromLocation;
    private final StringProperty toLocation;

    public Tour(String name, String description, String fromLocation, String toLocation) {
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.fromLocation = new SimpleStringProperty(fromLocation);
        this.toLocation = new SimpleStringProperty(toLocation);
    }

    public void clearProperties() {
        setName("");
        setDescription("");
        setFromLocation("");
        setToLocation("");
    }

    // Getters and setters for each property
    public String getName() {
        return name.get();
    }

    public void setName(String value) {
        name.set(value);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String value) {
        description.set(value);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public String getFromLocation() {
        return fromLocation.get();
    }

    public void setFromLocation(String value) {
        fromLocation.set(value);
    }

    public StringProperty fromLocationProperty() {
        return fromLocation;
    }

    public String getToLocation() {
        return toLocation.get();
    }

    public void setToLocation(String value) {
        toLocation.set(value);
    }

    public StringProperty toLocationProperty() {
        return toLocation;
    }
}