package at.tw.tourplanner.service;

import at.tw.tourplanner.dto.TourDto;
import at.tw.tourplanner.object.Tour;
import at.tw.tourplanner.object.TransportType;

public class TourService {

    private TourDto toDto(Tour tour) {
        TourDto dto = new TourDto();
        dto.name = tour.getName();
        dto.description = tour.getDescription();
        dto.fromLocation = tour.getFromLocation();
        dto.toLocation = tour.getToLocation();
        return dto;
    }

    private Tour fromDto(TourDto dto) {
        return new Tour(
                // Use defaults for missing UI-related fields
                TransportType.valueOf(dto.transportType),
                null, // no image saved in the BE
                dto.name,
                dto.description,
                dto.fromLocation,
                dto.toLocation,
                0, // default popularity (calculated in FE)
                0  // default child friendliness (calculated in FE)
        );
    }
}
