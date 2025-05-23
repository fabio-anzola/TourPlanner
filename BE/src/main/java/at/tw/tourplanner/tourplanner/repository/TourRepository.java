package at.tw.tourplanner.tourplanner.repository;

import at.tw.tourplanner.tourplanner.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for accessing Tour entities.
 */
public interface TourRepository extends JpaRepository<Tour, String> { }
