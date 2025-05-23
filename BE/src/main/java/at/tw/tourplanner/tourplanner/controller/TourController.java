package at.tw.tourplanner.tourplanner.controller;

import at.tw.tourplanner.tourplanner.model.Tour;
import at.tw.tourplanner.tourplanner.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Tour entities.
 */
@RestController
@RequestMapping("/tour")
public class TourController {

    @Autowired
    TourRepository tourRepository;

    /**
     * Retrieves all tours.
     *
     * @return ResponseEntity containing the list of all tours or no content status
     */
    @GetMapping("/")
    public ResponseEntity<List<Tour>> getTour() {
        List<Tour> allTours = this.tourRepository.findAll();
        return new ResponseEntity<>(allTours, allTours.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK );
    }

    /**
     * Retrieves a specific tour by name.
     *
     * @param name the name of the tour
     * @return ResponseEntity with the Tour if found, or not found status otherwise
     */
    @GetMapping("{name}")
    public ResponseEntity<Tour> getTour(@PathVariable String name) {
        Optional<Tour> tour = this.tourRepository.findById(name);
        return new ResponseEntity<>(tour.orElse(null), tour.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    /**
     * Creates a new tour.
     *
     * @param tour the tour to create
     * @return ResponseEntity with the created Tour or internal server error status
     */
    @PostMapping("/")
    public ResponseEntity<Tour> createTour(@RequestBody Tour tour) {
        try {
            Tour _tour = this.tourRepository.save(tour);
            return new ResponseEntity<>(_tour, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a tour by name.
     *
     * @param name the name of the tour to delete
     * @return ResponseEntity with no content status if deleted, or internal server error otherwise
     */
    @DeleteMapping("/{name}")
    public ResponseEntity<Tour> deleteTour(@PathVariable String name) {
        try {
            this.tourRepository.deleteById(name);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates an existing tour.
     *
     * @param name the name of the tour to update
     * @param updatedTour the tour object containing updated values
     * @return ResponseEntity with the updated tour or not found status
     */
    @PutMapping("/{name}")
    public ResponseEntity<Tour> updateTour(@PathVariable String name, @RequestBody Tour updatedTour) {
        Optional<Tour> existingTour = this.tourRepository.findById(name);
        if (existingTour.isPresent()) {
            Tour tour = existingTour.get();
            tour.setDescription(updatedTour.getDescription());
            tour.setFromLocation(updatedTour.getFromLocation());
            tour.setToLocation(updatedTour.getToLocation());
            tour.setTransportType(updatedTour.getTransportType());
            Tour savedTour = tourRepository.save(tour);
            return new ResponseEntity<>(savedTour, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
