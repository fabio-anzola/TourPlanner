package at.tw.tourplanner.service;

import at.tw.tourplanner.MainApplication;
import at.tw.tourplanner.config.AppConfig;
import at.tw.tourplanner.dto.TourDto;
import at.tw.tourplanner.dto.TourLogDto;
import at.tw.tourplanner.logger.ILoggerWrapper;
import at.tw.tourplanner.logger.LoggerFactory;
import at.tw.tourplanner.object.TourLog;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for managing tour logs via backend API.
 */
public class TourLogService {
    /** The base URL for the backend tour log API. */
    private static final String BASE_URL = AppConfig.getBackendApiUrl() + "/tourlog";

    /** The HTTP client. */
    private final HttpClient httpClient = HttpClient.newHttpClient();

    /** The JSON object mapper. */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /** The logger instance. */
    private static final ILoggerWrapper logger = LoggerFactory.getLogger(MainApplication.class);

    /**
     * Loads all tour logs for a given tour name.
     */
    public List<TourLog> getTourLogsByTourName(String tourName) {
        logger.debug("Entered function getTourLogsByTourName (TourLogService) with parameter: " + tourName);
        try {
            String encodedName = UriUtils.encodePathSegment(tourName, StandardCharsets.UTF_8);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/tour/" + encodedName))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 204 || response.body().isBlank()) {
                return List.of();
            }

            List<TourLogDto> dtos = objectMapper.readValue(response.body(), new TypeReference<>() {});
            List<TourLog> logs = new ArrayList<>();

            dtos.forEach(dto -> {logs.add(fromDto(dto));});

            return logs;
        } catch (Exception e) {
            logger.error("Error while loading tour logs: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    /**
     * Adds a tour log via the backend API.
     */
    public boolean addTourLog(TourLog log) {
        logger.debug("Entered function addTourLog (TourLogService) with parameter: " + log);
        try {
            TourLogDto dto = toDto(log);
            String requestBody = objectMapper.writeValueAsString(dto);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return true;
        } catch (Exception e) {
            logger.error("Failed to add tour log: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates a tour log by id via the backend API.
     */
    public boolean updateTourLog(int id, TourLog log) {
        logger.debug("Entered function updateTourLog (TourLogService) with parameter: " + id + " and " + log);
        try {
            TourLogDto dto = toDto(log);
            String requestBody = objectMapper.writeValueAsString(dto);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + id))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            return true;
        } catch (Exception e) {
            logger.error("Failed to update tour log: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a tour log by id via the backend API.
     */
    public boolean deleteTourLog(int id) {
        logger.debug("Entered function deleteTourLog (TourLogService) with parameter: " + id);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + id))
                    .header("Content-Type", "text/plain")
                    .method("DELETE", HttpRequest.BodyPublishers.ofString(String.valueOf(id)))
                    .build();

            httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            return true;
        } catch (Exception e) {
            logger.error("Failed to delete tour log: " + e.getMessage());
            return false;
        }
    }

    /**
     * Converts a TourLog object to a DTO.
     */
    private TourLogDto toDto(TourLog log) {
        logger.debug("Entered function toDto (TourLogService) with parameter: " + log);
        TourLogDto dto = new TourLogDto();
        dto.date = Date.valueOf(log.getDate());
        dto.comment = log.getComment();
        dto.difficulty = log.getParsedDifficulty();
        dto.totalDistance = log.getParsedTotalDistance();
        dto.totalTime = log.getParsedTotalTime();
        dto.rating = log.getParsedRating();

        dto.tour = new TourDto();
        dto.tour.name = log.getTourName();
        return dto;
    }

    /**
     * Converts a TourLogDto object to a TourLog.
     */
    private TourLog fromDto(TourLogDto dto) {
        logger.debug("Entered function fromDto (TourLogService) with parameter: " + dto);
        return new TourLog(
                (int) dto.id,
                dto.date.toString(),
                dto.comment,
                dto.difficulty,
                dto.totalDistance,
                dto.totalTime,
                dto.rating,
                dto.tour != null ? dto.tour.name : ""
        );
    }
}