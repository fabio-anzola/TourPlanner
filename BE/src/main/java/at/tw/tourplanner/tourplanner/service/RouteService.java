package at.tw.tourplanner.tourplanner.service;

import at.tw.tourplanner.tourplanner.dto.RouteResultDTO;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Service responsible for fetching routing information from the OpenRouteService API.
 */
@Service
public class RouteService {

    @Value("${openrouteservice.api-key}")
    private String apiKey;

    /**
     * Retrieves route summary (distance and duration) between two coordinates.
     *
     * @param startLon start longitude
     * @param startLat start latitude
     * @param endLon   end longitude
     * @param endLat   end latitude
     * @param profile  transport mode profile for OpenRouteService
     * @return a RouteResultDTO containing distance (in meters) and duration (in seconds)
     * @throws Exception if the HTTP request or JSON parsing fails
     */
    public RouteResultDTO getRoute(double startLon, double startLat, double endLon, double endLat, String profile) throws Exception {
        URL url = new URL("https://api.openrouteservice.org/v2/directions/" + profile);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", apiKey);
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        String jsonInput = String.format(Locale.US, """
                {
                  "coordinates": [
                    [%f, %f],
                    [%f, %f]
                  ]
                }
                """, startLon, startLat, endLon, endLat);

        try (OutputStream os = con.getOutputStream()) {
            os.write(jsonInput.getBytes(StandardCharsets.UTF_8));
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) response.append(line);
        }

        JSONObject json = new JSONObject(response.toString());
        JSONObject summary = json.getJSONArray("routes").getJSONObject(0).getJSONObject("summary");

        return new RouteResultDTO(summary.getDouble("distance"), summary.getDouble("duration"));
    }

    /**
     * Retrieves the GeoJSON route data between two coordinates.
     *
     * @param startLon start longitude
     * @param startLat start latitude
     * @param endLon   end longitude
     * @param endLat   end latitude
     * @param profile  transport mode profile for OpenRouteService
     * @return a GeoJSON-formatted string of the route
     * @throws Exception if the HTTP request fails
     */
    public String getRouteGeoJson(double startLon, double startLat, double endLon, double endLat, String profile) throws Exception {
        URL url = new URL("https://api.openrouteservice.org/v2/directions/" + profile + "/geojson");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", apiKey);
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        String jsonInput = String.format(Locale.US, """
        {
          "coordinates": [
            [%f, %f],
            [%f, %f]
          ]
        }
        """, startLon, startLat, endLon, endLat);

        try (OutputStream os = con.getOutputStream()) {
            os.write(jsonInput.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = con.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("GeoJSON route request failed: HTTP " + responseCode);
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
        }

        return response.toString();
    }
}