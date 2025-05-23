package at.tw.tourplanner.object;

import lombok.Getter;

/**
 * Holds route data such as distance, duration, and GeoJSON.
 */
@Getter
public class RouteData {
    /** The distance of the route. */
    public double distance;

    /** The duration of the route. */
    public double duration;

    /** The GeoJSON data for the route. */
    public String geoJson;

    /**
     * Creates RouteData from distance, duration, and geoJson.
     *
     * @param distance the route distance
     * @param duration the route duration
     * @param geoJson the GeoJSON data
     */
    public RouteData(double distance, double duration, String geoJson) {
        this.distance = distance;
        this.duration = duration;
        this.geoJson = geoJson;
    }

    /**
     * Creates RouteData from a String array.
     *
     * @param res the array containing distance, duration, and geoJson
     */
    public RouteData(String[] res) {
        this.distance = res[0] != null ? Double.parseDouble(res[0]) : 0;
        this.duration = res[1] != null ? Double.parseDouble(res[1]) : 0;
        this.geoJson = res[2];
    }

    /**
     * Returns a string representation of the route data.
     *
     * @return a string with distance and duration
     */
    @Override
    public String toString() {
        return "RouteData{" +
                "distance=" + distance +
                ", duration=" + duration +
                '}';
    }
}