package at.tw.tourplanner.tourplanner.service;

import at.tw.tourplanner.tourplanner.dto.RouteResultDTO;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
public class RouteService {

    @Value("${openrouteservice.api-key}")
    private String apiKey;

    public RouteResultDTO getRoute(double startLon, double startLat, double endLon, double endLat) throws Exception {
        URL url = new URL("https://api.openrouteservice.org/v2/directions/driving-car");
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
}