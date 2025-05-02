package at.tw.tourplanner.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.beans.property.*;
import javafx.scene.image.Image;

/**
 * The model object for the Tour
 */
public class Tour {
    /**
     * Contains the name of the tour
     */
    private final StringProperty name;

    /**
     * Contains the description of the tour
     */
    private final StringProperty description;

    /**
     * Contains the starting location of the tour
     */
    private final StringProperty fromLocation;

    /**
     * Contains the end location of the tour
     */
    private final StringProperty toLocation;

    /**
     * Contains the transport type for the tour (enum!)
     */
    private final SimpleObjectProperty<TransportType> transportType;

    /**
     * Contains the route image for the tour
     */
    @JsonIgnore
    private final SimpleObjectProperty<Image> routeImage;

    /**
     * Contains the popularity of the tour
     */
    @JsonIgnore
    private final SimpleIntegerProperty popularity; // number of logs

    /**
     * Contains the child friendliness of the tour
     */
    @JsonIgnore
    private final SimpleIntegerProperty childFriendliness; // 1-4 (4 = most child-friendly)

    /**
     * The constructor for the Tour object
     *
     * @param transportType     the Enum type
     * @param routeImage        the Image
     * @param name              Tour name
     * @param description       tour description
     * @param fromLocation      tour start location
     * @param toLocation        tour end location
     * @param popularity        tour popularity
     * @param childFriendliness tour child friendliness
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
     * The Tour default constructor
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
     * A method to clean up a Tour object and set its default properties
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

    /**
     * Name getter
     *
     * @return the name
     */
    public String getName() {
        return name.get();
    }

    /**
     * Name property getter
     *
     * @return the name property
     */
    public StringProperty nameProperty() {
        return name;
    }

    /**
     * Name property setter
     *
     * @param value the name
     */
    public void setName(String value) {
        name.set(value);
    }

    /**
     * Description getter
     *
     * @return the description
     */
    public String getDescription() {
        return description.get();
    }

    /**
     * Description property getter
     *
     * @return the description property
     */
    public StringProperty descriptionProperty() {
        return description;
    }

    /**
     * Description property setter
     *
     * @param value the name
     */
    public void setDescription(String value) {
        description.set(value);
    }

    /**
     * From Location getter
     *
     * @return the from location
     */
    public String getFromLocation() {
        return fromLocation.get();
    }

    /**
     * From location property getter
     *
     * @return the from location property
     */
    public StringProperty fromLocationProperty() {
        return fromLocation;
    }

    /**
     * From location property setter
     *
     * @param value the from location
     */
    public void setFromLocation(String value) {
        fromLocation.set(value);
    }

    /**
     * To Location getter
     *
     * @return the to location
     */
    public String getToLocation() {
        return toLocation.get();
    }

    /**
     * To location property getter
     *
     * @return the to location property
     */
    public StringProperty toLocationProperty() {
        return toLocation;
    }

    /**
     * To location property setter
     *
     * @param value the to location
     */
    public void setToLocation(String value) {
        toLocation.set(value);
    }

    /**
     * Transport type getter
     *
     * @return the transport type
     */
    public TransportType getTransportType() {
        return this.transportType.get();
    }

    /**
     * Transport type property getter
     *
     * @return the transport type property
     */
    public Property<TransportType> transportTypeProperty() {
        return transportType;
    }

    /**
     * Transport type property setter
     *
     * @param transportType the transport type
     */
    public void setTransportType(TransportType transportType) {
        this.transportType.set(transportType);
    }

    /**
     * Route Image getter
     *
     * @return the route image
     */
    public Image getRouteImage() {
        return this.routeImage.get();
    }

    /**
     * Route image property getter
     *
     * @return the route image property
     */
    public Property<Image> routeImageProperty() {
        return routeImage;
    }

    /**
     * Route image property setter
     *
     * @param routeImage the Route Image
     */
    public void setRouteImage(Image routeImage) { this.routeImage.set(routeImage); }

    /**
     * Value getter
     *
     * @return the value
     */
    public Integer getPopularity() { return this.popularity.get(); }

    /**
     * Value property getter
     *
     * @return the value property
     */
    public SimpleIntegerProperty popularityProperty() { return popularity; }

    /**
     * Value property setter
     *
     * @param value the popularity
     */
    public void setPopularity(Integer value) { this.popularity.set(value); }

    /**
     * Child friendliness getter
     *
     * @return the child friendliness
     */
    public Integer getChildFriendliness() { return this.childFriendliness.get(); }

    /**
     * Child friendliness property getter
     *
     * @return the child friendliness property
     */
    public SimpleIntegerProperty childFriendlinessProperty() { return childFriendliness; }

    /**
     * Child friendliness property setter
     *
     * @param value the child friendliness
     */
    public void setChildFriendliness(Integer value) { this.childFriendliness.set(value); }
}