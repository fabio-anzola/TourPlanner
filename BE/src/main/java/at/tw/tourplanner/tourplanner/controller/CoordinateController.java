package at.tw.tourplanner.tourplanner.controller;

import at.tw.tourplanner.tourplanner.dto.CoordinateDTO;
import at.tw.tourplanner.tourplanner.service.GeocodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/coordinates")
public class CoordinateController {

    private final GeocodeService geocodeService;

    public CoordinateController(GeocodeService geocodeService) {
        this.geocodeService = geocodeService;
    }

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
