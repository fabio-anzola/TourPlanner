package at.tw.tourplanner.object;

import lombok.Getter;

@Getter
public class RouteData {
    public double distance;
    public double duration;
    public String geoJson;

    public RouteData(double distance, double duration, String geoJson) {
        this.distance = distance;
        this.duration = duration;
        this.geoJson = geoJson;
    }

    public RouteData(String[] res) {
        this.distance = res[0] != null ? Double.parseDouble(res[0]) : 0;
        this.duration = res[1] != null ? Double.parseDouble(res[1]) : 0;
        this.geoJson = res[2];
    }

    @Override
    public String toString() {
        return "RouteData{" +
                "distance=" + distance +
                ", duration=" + duration +
                '}';
    }
}
