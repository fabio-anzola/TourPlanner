package at.tw.tourplanner.tourplanner.model;

/**
 * Enum representing different modes of transport.
 */
public enum TransportMode {
    CAR("driving-car"),
    BICYCLE("cycling-regular"),
    WALK("foot-walking"),
    PUBLIC("wheelchair"); // TODO: OpenRouteService doesn't support public transport?????

    private final String orsProfile;

    /**
     * Constructor for TransportMode enum.
     *
     * @param orsProfile the ORS profile string for the mode
     */
    TransportMode(String orsProfile) {
        this.orsProfile = orsProfile;
    }

    /**
     * Gets the ORS profile string.
     *
     * @return the ORS profile string
     */
    public String getOrsProfile() {
        return orsProfile;
    }

    /**
     * Converts a string value to a corresponding TransportMode.
     *
     * @param value the string representation of the transport mode
     * @return the matching TransportMode
     * @throws IllegalArgumentException if the transport mode is unsupported
     */
    public static TransportMode fromString(String value) {
        return switch (value.toLowerCase()) {
            case "car" -> CAR;
            case "bicycle" -> BICYCLE;
            case "walk" -> WALK;
            case "public_transport" -> PUBLIC;
            default -> throw new IllegalArgumentException("Unsupported transport mode: " + value);
        };
    }
}