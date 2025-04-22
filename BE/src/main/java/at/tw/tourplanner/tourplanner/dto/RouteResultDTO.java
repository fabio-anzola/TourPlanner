package at.tw.tourplanner.tourplanner.dto;

public class RouteResultDTO {
    private double distance; // in meters
    private double duration; // in seconds

    public RouteResultDTO(double distance, double duration) {
        this.distance = distance;
        this.duration = duration;
    }

    public double getDistance() { return distance; }
    public double getDuration() { return duration; }

    public void setDistance(double distance) { this.distance = distance; }
    public void setDuration(double duration) { this.duration = duration; }
}
