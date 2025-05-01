package at.tw.tourplanner.service;

import at.tw.tourplanner.config.AppConfig;
import at.tw.tourplanner.dto.TourDto;
import at.tw.tourplanner.dto.TourLogDto;
import at.tw.tourplanner.object.TourLog;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class TourLogService {
    private static final String BASE_URL = AppConfig.getBackendApiUrl() + "/tourlog";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<TourLog> getTourLogsByTourName(String tourName) {
        try {
            String encodedName = UriUtils.encodePathSegment(tourName, StandardCharsets.UTF_8);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/tour/" + encodedName))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            List<TourLogDto> dtos = objectMapper.readValue(response.body(), new TypeReference<>() {});
            List<TourLog> logs = new ArrayList<>();

            for (TourLogDto dto : dtos) {
                logs.add(fromDto(dto));
            }

            return logs;
        } catch (Exception e) {
            System.out.println("Exception while loading tour logs:");
            e.printStackTrace();
            return List.of();
        }
    }

    public void addTourLog(TourLog log) throws IOException, InterruptedException {
        TourLogDto dto = toDto(log);

        String requestBody = objectMapper.writeValueAsString(dto);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void updateTourLog(int id, TourLog log) throws IOException, InterruptedException {
        TourLogDto dto = toDto(log);

        String requestBody = objectMapper.writeValueAsString(dto);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        httpClient.send(request, HttpResponse.BodyHandlers.discarding());
    }

    public void deleteTourLog(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .header("Content-Type", "text/plain")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(String.valueOf(id)))
                .build();

        httpClient.send(request, HttpResponse.BodyHandlers.discarding());
    }

    private TourLogDto toDto(TourLog log) {
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

    private TourLog fromDto(TourLogDto dto) {
        return new TourLog(
                (int)dto.id,
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