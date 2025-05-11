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

@RestController
@RequestMapping("/api/route")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

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
                    // Annotation (minor for the lectures):
                    // I would not list possible options here ... it supports tampering
                    // and can be confusing when you enter new transport types (scooter) into the system but you
                    // are not adapting the message here
                    .body("{\"error\": \"Invalid transport mode. Use car, bicycle, walk, or public.\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Failed to fetch GeoJSON route\"}");
        }
    }
}
