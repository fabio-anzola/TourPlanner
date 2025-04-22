package at.tw.tourplanner.tourplanner.model;

public enum TransportMode {
    CAR("driving-car"),
    BICYCLE("cycling-regular"),
    WALK("foot-walking"),
    PUBLIC("wheelchair"); // TODO: OpenRouteService doesn't support public transport?????

    private final String orsProfile;

    TransportMode(String orsProfile) {
        this.orsProfile = orsProfile;
    }

    public String getOrsProfile() {
        return orsProfile;
    }

    public static TransportMode fromString(String value) {
        return switch (value.toLowerCase()) {
            case "car" -> CAR;
            case "bicycle" -> BICYCLE;
            case "walk" -> WALK;
            case "public" -> PUBLIC;
            default -> throw new IllegalArgumentException("Unsupported transport mode: " + value);
        };
    }
}