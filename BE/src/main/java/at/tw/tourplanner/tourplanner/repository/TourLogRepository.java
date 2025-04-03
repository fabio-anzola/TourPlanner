package at.tw.tourplanner.tourplanner.repository;

import at.tw.tourplanner.tourplanner.model.TourLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourLogRepository extends JpaRepository<TourLog, Long> { }
