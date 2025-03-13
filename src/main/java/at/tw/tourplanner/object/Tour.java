package at.tw.tourplanner.object;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Tour {
    private final StringProperty name;
    private final StringProperty description;
    private final StringProperty fromLocation;
    private final StringProperty toLocation;
    private final ObjectProperty<TransportType> transportType;

    private TransportType defaultTransportType;

    public Tour(TransportType defaultTransportType, String name, String description, String fromLocation, String toLocation, String transportType) {
        this.defaultTransportType = defaultTransportType;
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.fromLocation = new SimpleStringProperty(fromLocation);
        this.toLocation = new SimpleStringProperty(toLocation);
        this.transportType = new SimpleObjectProperty<>(TransportType.valueOf(transportType));
    }

    public void clearProperties() {
        setName("");
        setDescription("");
        setFromLocation("");
        setToLocation("");
        setTransportType(String.valueOf(defaultTransportType));
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

    public String getTransportType() {
        return transportType.get().name();
    }

    public ObjectProperty<TransportType> transportTypeProperty() {
        return transportType;
    }

    public void setTransportType(String value) {
        transportType.set(TransportType.valueOf(value));
    }
}