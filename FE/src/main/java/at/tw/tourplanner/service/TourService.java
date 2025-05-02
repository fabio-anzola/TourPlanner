package at.tw.tourplanner.service;

import at.tw.tourplanner.config.AppConfig;
import at.tw.tourplanner.dto.TourDto;
import at.tw.tourplanner.object.Tour;
import at.tw.tourplanner.object.TransportType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TourService {
    private static final String BASE_URL = AppConfig.getBackendApiUrl() + "/tour";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Tour> getAllTours() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 204 || response.body().isBlank()) return List.of();

            List<TourDto> dtos = objectMapper.readValue(response.body(), new TypeReference<>() {});
            List<Tour> tours = new ArrayList<>();
            for (TourDto dto : dtos) {
                tours.add(fromDto(dto));
            }

            return tours;
        } catch (Exception e) {
            System.err.println("Error while loading tours: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    public boolean addTour(Tour tour) {
        try {
            TourDto dto = toDto(tour);
            String json = objectMapper.writeValueAsString(dto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return true;
        } catch (Exception e) {
            System.err.println("Failed to add tour: " + e.getMessage());
            return false;
        }
    }

    public boolean updateTour(String name, Tour tour) {
        try {
            String encodedName = UriUtils.encodePathSegment(name, StandardCharsets.UTF_8);
            TourDto dto = toDto(tour);
            String json = objectMapper.writeValueAsString(dto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + encodedName))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return true;
        } catch (Exception e) {
            System.err.println("Failed to update tour: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteTour(String name) {
        try {
            String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + encodedName))
                    .header("Content-Type", "text/plain")
                    .method("DELETE", HttpRequest.BodyPublishers.ofString(name))
                    .build();

            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return true;
        } catch (Exception e) {
            System.err.println("Failed to delete tour: " + e.getMessage());
            return false;
        }
    }

    private TourDto toDto(Tour tour) {
        TourDto dto = new TourDto();
        dto.name = tour.getName();
        dto.description = tour.getDescription();
        dto.fromLocation = tour.getFromLocation();
        dto.toLocation = tour.getToLocation();
        dto.transportType = tour.getTransportType().name();
        return dto;
    }

    private Tour fromDto(TourDto dto) {
        return new Tour(
                TransportType.valueOf(dto.transportType),
                null,
                dto.name,
                dto.description,
                dto.fromLocation,
                dto.toLocation,
                0,
                0
        );
    }
}