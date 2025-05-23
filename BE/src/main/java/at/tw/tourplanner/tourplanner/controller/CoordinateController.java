package at.tw.tourplanner.tourplanner.controller;

import at.tw.tourplanner.tourplanner.dto.CoordinateDTO;
import at.tw.tourplanner.tourplanner.service.GeocodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for geocoding addresses to geographic coordinates.
 */
@RestController
@RequestMapping("/api/coordinates")
public class CoordinateController {

    private final GeocodeService geocodeService;

    /**
     * Constructor that initializes the GeocodeService dependency.
     *
     * @param geocodeService the service used for geocoding operations
     */
    public CoordinateController(GeocodeService geocodeService) {
        this.geocodeService = geocodeService;
    }

    /**
     * Endpoint for geocoding a given address into latitude and longitude.
     *
     * @param address the address to geocode
     * @return ResponseEntity containing CoordinateDTO if successful, or appropriate error response
     */
    @GetMapping
    public ResponseEntity<CoordinateDTO> geocode(@RequestParam String address) {
        try {
            CoordinateDTO coordinates = geocodeService.geocode(address);
            return ResponseEntity.ok(coordinates);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
