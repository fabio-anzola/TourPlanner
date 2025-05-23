package at.tw.tourplanner.tourplanner.controller;

import at.tw.tourplanner.tourplanner.dto.RouteResultDTO;
import at.tw.tourplanner.tourplanner.model.TransportMode;
import at.tw.tourplanner.tourplanner.service.RouteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling route-related requests.
 */
@RestController
@RequestMapping("/api/route")
public class RouteController {

    private final RouteService routeService;

    /**
     * Constructs the RouteController with the given RouteService.
     *
     * @param routeService the service used to fetch route data
     */
    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    /**
     * Returns a route summary including distance and duration for the specified start and end coordinates.
     *
     * @param startLon the longitude of the start point
     * @param startLat the latitude of the start point
     * @param endLon the longitude of the end point
     * @param endLat the latitude of the end point
     * @param mode the transport mode (car, bicycle, walk, etc.)
     * @return ResponseEntity containing RouteResultDTO or error status
     */
    @GetMapping("/summary")
    public ResponseEntity<RouteResultDTO> getRouteSummary(
            @RequestParam double startLon,
            @RequestParam double startLat,
            @RequestParam double endLon,
            @RequestParam double endLat,
            @RequestParam(defaultValue = "car") String mode
    ) {
        try {
            String profile = TransportMode.fromString(mode).getOrsProfile();
            RouteResultDTO route = routeService.getRoute(startLon, startLat, endLon, endLat, profile);
            return ResponseEntity.ok(route);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Returns a GeoJSON representation of the route between two points.
     *
     * @param startLon the longitude of the start point
     * @param startLat the latitude of the start point
     * @param endLon the longitude of the end point
     * @param endLat the latitude of the end point
     * @param mode the transport mode (car, bicycle, walk, etc.)
     * @return ResponseEntity containing the GeoJSON string or error message
     */
    @GetMapping
    public ResponseEntity<String> getRoute(
            @RequestParam double startLon,
            @RequestParam double startLat,
            @RequestParam double endLon,
            @RequestParam double endLat,
            @RequestParam(defaultValue = "car") String mode
    ) {
        try {
            String profile = TransportMode.fromString(mode).getOrsProfile();
            String geoJson = routeService.getRouteGeoJson(startLon, startLat, endLon, endLat, profile);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(geoJson);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\": \"Invalid transport mode. Use car, bicycle, walk, or public.\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Failed to fetch GeoJSON route\"}");
        }
    }
}
