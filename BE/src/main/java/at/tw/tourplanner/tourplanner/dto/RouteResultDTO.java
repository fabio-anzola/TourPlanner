package at.tw.tourplanner.tourplanner.dto;

/**
 * Data Transfer Object representing the result of a route query.
 * Contains distance and duration of the route.
 */
public class RouteResultDTO {
    private double distance; // in meters
    private double duration; // in seconds

    /**
     * Constructs a RouteResultDTO with specified distance and duration.
     *
     * @param distance the total distance in meters
     * @param duration the total duration in seconds
     */
    public RouteResultDTO(double distance, double duration) {
        this.distance = distance;
        this.duration = duration;
    }

    /**
     * Gets the distance.
     *
     * @return the distance in meters
     */
    public double getDistance() { return distance; }
    /**
     * Gets the duration.
     *
     * @return the duration in seconds
     */
    public double getDuration() { return duration; }

    /**
     * Sets the distance.
     *
     * @param distance the distance in meters to set
     */
    public void setDistance(double distance) { this.distance = distance; }
    /**
     * Sets the duration.
     *
     * @param duration the duration in seconds to set
     */
    public void setDuration(double duration) { this.duration = duration; }
}
