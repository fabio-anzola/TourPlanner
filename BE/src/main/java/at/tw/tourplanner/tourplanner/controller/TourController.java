package at.tw.tourplanner.tourplanner.controller;

import at.tw.tourplanner.tourplanner.model.Tour;
import at.tw.tourplanner.tourplanner.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tour")
public class TourController {

    @Autowired
    TourRepository tourRepository;

    @GetMapping("/")
    public ResponseEntity<List<Tour>> getTour() {
        List<Tour> allTours = this.tourRepository.findAll();
        return new ResponseEntity<>(allTours, allTours.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK );
    }

    @GetMapping("{name}")
    public ResponseEntity<Tour> getTour(@PathVariable String name) {
        Optional<Tour> tour = this.tourRepository.findById(name);
        return new ResponseEntity<>(tour.orElse(null), tour.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Tour> createTour(@RequestBody Tour tour) {
        try {
            Tour _tour = this.tourRepository.save(tour);
            return new ResponseEntity<>(_tour, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Tour> deleteTour(@RequestBody String name) {
        try {
            this.tourRepository.deleteById(name);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{name}")
    public ResponseEntity<Tour> updateTour(@PathVariable String name, @RequestBody Tour updatedTour) {
        Optional<Tour> existingTour = this.tourRepository.findById(name);
        if (existingTour.isPresent()) {
            Tour tour = existingTour.get();
            tour.setDescription(updatedTour.getDescription());
            tour.setFromLocation(updatedTour.getFromLocation());
            tour.setToLocation(updatedTour.getToLocation());
            Tour savedTour = tourRepository.save(tour);
            return new ResponseEntity<>(savedTour, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
