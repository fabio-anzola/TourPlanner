package at.tw.tourplanner.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.beans.property.*;
import java.awt.Image;

/**
 * Model object for a tour.
 */
public class Tour {
    /** Name of the tour. */
    private final StringProperty name;

    /** Description of the tour. */
    private final StringProperty description;

    /** Starting location of the tour. */
    private final StringProperty fromLocation;

    /** End location of the tour. */
    private final StringProperty toLocation;

    /** Transport type for the tour. */
    private final SimpleObjectProperty<TransportType> transportType;

    /** Route image for the tour. */
    @JsonIgnore
    private final SimpleObjectProperty<Image> routeImage;

    /** Popularity of the tour. */
    @JsonIgnore
    private final SimpleIntegerProperty popularity;

    /** Child friendliness of the tour. */
    @JsonIgnore
    private final SimpleIntegerProperty childFriendliness;

    /**
     * Creates a Tour with all properties set.
     */
    public Tour(TransportType transportType, Image routeImage, String name, String description, String fromLocation, String toLocation, Integer popularity, Integer childFriendliness) {
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.fromLocation = new SimpleStringProperty(fromLocation);
        this.toLocation = new SimpleStringProperty(toLocation);
        this.transportType = new SimpleObjectProperty<TransportType>(transportType);
        this.routeImage = new SimpleObjectProperty<Image>(routeImage);
        this.popularity = new SimpleIntegerProperty(popularity);
        this.childFriendliness = new SimpleIntegerProperty(childFriendliness);
    }

    /**
     * Creates a Tour with default values.
     */
    public Tour() {
        this.name = new SimpleStringProperty("");
        this.description = new SimpleStringProperty("");
        this.fromLocation = new SimpleStringProperty("");
        this.toLocation = new SimpleStringProperty("");
        this.transportType = new SimpleObjectProperty<TransportType>(TransportType.DEFAULT);
        this.routeImage = new SimpleObjectProperty<Image>(null);
        this.popularity = new SimpleIntegerProperty(0);
        this.childFriendliness = new SimpleIntegerProperty(0);
    }

    /**
     * Resets all properties to default values.
     */
    public void clearProperties() {
        setName("");
        setDescription("");
        setFromLocation("");
        setToLocation("");
        setTransportType(TransportType.DEFAULT);
        setRouteImage(null);
        setPopularity(0);
        setChildFriendliness(0);
    }

    /** Returns the name. */
    public String getName() {
        return name.get();
    }

    /** Returns the name property. */
    public StringProperty nameProperty() {
        return name;
    }

    /** Sets the name. */
    public void setName(String value) {
        name.set(value);
    }

    /** Returns the description. */
    public String getDescription() {
        return description.get();
    }

    /** Returns the description property. */
    public StringProperty descriptionProperty() {
        return description;
    }

    /** Sets the description. */
    public void setDescription(String value) {
        description.set(value);
    }

    /** Returns the starting location. */
    public String getFromLocation() {
        return fromLocation.get();
    }

    /** Returns the from location property. */
    public StringProperty fromLocationProperty() {
        return fromLocation;
    }

    /** Sets the starting location. */
    public void setFromLocation(String value) {
        fromLocation.set(value);
    }

    /** Returns the end location. */
    public String getToLocation() {
        return toLocation.get();
    }

    /** Returns the to location property. */
    public StringProperty toLocationProperty() {
        return toLocation;
    }

    /** Sets the end location. */
    public void setToLocation(String value) {
        toLocation.set(value);
    }

    /** Returns the transport type. */
    public TransportType getTransportType() {
        return this.transportType.get();
    }

    /** Returns the transport type property. */
    public Property<TransportType> transportTypeProperty() {
        return transportType;
    }

    /** Sets the transport type. */
    public void setTransportType(TransportType transportType) {
        this.transportType.set(transportType);
    }

    /** Returns the route image. */
    public Image getRouteImage() {
        return this.routeImage.get();
    }

    /** Returns the route image property. */
    public Property<Image> routeImageProperty() {
        return routeImage;
    }

    /** Sets the route image. */
    public void setRouteImage(Image routeImage) { this.routeImage.set(routeImage); }

    /** Returns the popularity value. */
    public Integer getPopularity() { return this.popularity.get(); }

    /** Returns the popularity property. */
    public SimpleIntegerProperty popularityProperty() { return popularity; }

    /** Sets the popularity value. */
    public void setPopularity(Integer value) { this.popularity.set(value); }

    /** Returns the child friendliness value. */
    public Integer getChildFriendliness() { return this.childFriendliness.get(); }

    /** Returns the child friendliness property. */
    public SimpleIntegerProperty childFriendlinessProperty() { return childFriendliness; }

    /** Sets the child friendliness value. */
    public void setChildFriendliness(Integer value) { this.childFriendliness.set(value); }
}