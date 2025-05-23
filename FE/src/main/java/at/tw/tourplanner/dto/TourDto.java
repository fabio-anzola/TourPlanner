package at.tw.tourplanner.dto;

/**
 * Data transfer object for a tour.
 */
public class TourDto {
    /** The name of the tour. */
    public String name;

    /** The description of the tour. */
    public String description;

    /** The starting location. */
    public String fromLocation;

    /** The destination location. */
    public String toLocation;

    /** The type of transport used. */
    public String transportType;
}