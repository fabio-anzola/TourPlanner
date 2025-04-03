package at.tw.tourplanner.tourplanner.repository;

import at.tw.tourplanner.tourplanner.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourRepository extends JpaRepository<Tour, String> { }
