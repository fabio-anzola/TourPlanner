package at.tw.tourplanner.tourplanner.dto;

public class CoordinateDTO {
    private double lon;
    private double lat;

    public CoordinateDTO(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public double getLon() { return lon; }
    public double getLat() { return lat; }

    public void setLon(double lon) { this.lon = lon; }
    public void setLat(double lat) { this.lat = lat; }
}
