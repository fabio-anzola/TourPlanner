package at.tw.tourplanner.tourplanner.repository;

import at.tw.tourplanner.tourplanner.model.TourLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TourLogRepository extends JpaRepository<TourLog, Long> {
    List<TourLog> findByTourName(String tourName);
}
