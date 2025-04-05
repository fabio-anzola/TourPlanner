package at.tw.tourplanner.tourplanner.controller;

import at.tw.tourplanner.tourplanner.model.Tour;
import at.tw.tourplanner.tourplanner.model.TourLog;
import at.tw.tourplanner.tourplanner.repository.TourLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tourlog")
public class TourLogController {

    @Autowired
    TourLogRepository tourLogRepository;

    @GetMapping("/")
    public ResponseEntity<List<TourLog>> getTourLog() {
        List<TourLog> allTourLogs = this.tourLogRepository.findAll();
        return new ResponseEntity<>(allTourLogs, allTourLogs.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK );
    }

    @GetMapping("{tourLogId}")
    public ResponseEntity<TourLog> getTourLog(@PathVariable Long tourLogId) {
        Optional<TourLog> tourLog = this.tourLogRepository.findById(tourLogId);
        return new ResponseEntity<>(tourLog.orElse(null), tourLog.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @GetMapping("/tour/{tourName}")
    public ResponseEntity<List<TourLog>> getTourLog(@PathVariable String tourName) {
        List<TourLog> tourLogsByName = this.tourLogRepository.findByTourName(tourName);
        return new ResponseEntity<>(tourLogsByName, tourLogsByName.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK );
    }

    @PostMapping("/")
    public ResponseEntity<TourLog> createTourLog(@RequestBody TourLog tourLog) {
        try {
            TourLog _tourLog = this.tourLogRepository.save(tourLog);
            return new ResponseEntity<>(_tourLog, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{tourLogId}")
    public ResponseEntity<TourLog> deleteTourLog(@PathVariable Long tourLogId) {
        try {
            this.tourLogRepository.deleteById(tourLogId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{tourLogId}")
    public ResponseEntity<TourLog> updateTourLog(@PathVariable Long tourLogId, @RequestBody TourLog updatedTourLog) {
        Optional<TourLog> existingTourLog = this.tourLogRepository.findById(tourLogId);
        if (existingTourLog.isPresent()) {
            TourLog tourLog = existingTourLog.get();
            tourLog.setComment(updatedTourLog.getComment());
            tourLog.setDifficulty(updatedTourLog.getDifficulty());
            tourLog.setTotalDistance(updatedTourLog.getTotalDistance());
            tourLog.setTotalTime(updatedTourLog.getTotalTime());
            tourLog.setRating(updatedTourLog.getRating());
            tourLog.setDate(updatedTourLog.getDate());
            TourLog savedTourLog = tourLogRepository.save(tourLog);
            return new ResponseEntity<>(savedTourLog, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
