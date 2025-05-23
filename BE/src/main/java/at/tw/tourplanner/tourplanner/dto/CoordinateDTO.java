package at.tw.tourplanner.tourplanner.dto;

/**
 * Data Transfer Object representing geographical coordinates.
 */
public class CoordinateDTO {
    private double lon;
    private double lat;

    /**
     * Constructs a CoordinateDTO with specified longitude and latitude.
     *
     * @param lon longitude of the coordinate
     * @param lat latitude of the coordinate
     */
    public CoordinateDTO(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    /**
     * Gets the longitude.
     *
     * @return the longitude
     */
    public double getLon() { return lon; }
    /**
     * Gets the latitude.
     *
     * @return the latitude
     */
    public double getLat() { return lat; }

    /**
     * Sets the longitude.
     *
     * @param lon the longitude to set
     */
    public void setLon(double lon) { this.lon = lon; }
    /**
     * Sets the latitude.
     *
     * @param lat the latitude to set
     */
    public void setLat(double lat) { this.lat = lat; }
}
