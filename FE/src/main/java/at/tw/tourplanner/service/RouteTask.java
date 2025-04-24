package at.tw.tourplanner.service;

import at.tw.tourplanner.object.RouteData;
import at.tw.tourplanner.object.TransportType;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import lombok.Getter;

public class RouteTask extends Task<Void> {
    private final String startAddress;
    private final String endAddress;
    private final TransportType transportType;
    private final Label durationLabel;
    private final Label distanceLabel;
    private final RouteImageService restClient;

    public RouteTask(String startAddress, String endAddress, TransportType transportType, Label durationLabel, Label distanceLabel) {
        this.startAddress = startAddress;
        this.endAddress = endAddress;
        this.transportType = transportType;
        this.durationLabel = durationLabel;
        this.distanceLabel = distanceLabel;
        this.restClient = new RouteImageService();
    }

    @Override
    protected Void call() throws Exception {
        double[] startCoords = restClient.geocode(startAddress);
        double[] endCoords = restClient.geocode(endAddress);

        if (startCoords == null || endCoords == null) {
            throw new Exception("Failed to geocode one or both addresses.");
        }

        RouteData routeData = new RouteData(restClient.getRouteGeoJson(startCoords, endCoords, transportType));

        // Update UI
        javafx.application.Platform.runLater(() -> {
            durationLabel.setText(String.format("%.2f min", routeData.duration / 60));
            distanceLabel.setText(String.format("%.2f km", routeData.distance / 1000));
        });

        return null;
    }
}
