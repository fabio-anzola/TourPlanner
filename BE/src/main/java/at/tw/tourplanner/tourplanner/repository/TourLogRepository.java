package at.tw.tourplanner.tourplanner.repository;

import at.tw.tourplanner.tourplanner.model.TourLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for accessing TourLog entities.
 */
public interface TourLogRepository extends JpaRepository<TourLog, Long> {
    /**
     * Finds all TourLogs associated with a given tour name.
     *
     * @param tourName the name of the tour
     * @return a list of TourLogs belonging to the specified tour
     */
    List<TourLog> findByTourName(String tourName);
}
