package at.tw.tourplanner.tourplanner.service;

import at.tw.tourplanner.tourplanner.dto.CoordinateDTO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Service responsible for geocoding addresses using the OpenRouteService API.
 */
@Service
public class GeocodeService {

    @Value("${openrouteservice.api-key}")
    private String apiKey;

    /**
     * Retrieves geographic coordinates (longitude and latitude) for a given address.
     *
     * @param address the address to geocode
     * @return a CoordinateDTO containing longitude and latitude, or null if no result found
     * @throws Exception if the HTTP request fails or response is invalid
     */
    public CoordinateDTO geocode(String address) throws Exception {
        String encoded = URLEncoder.encode(address, StandardCharsets.UTF_8);
        URL url = new URL("https://api.openrouteservice.org/geocode/search?api_key=" + apiKey + "&text=" + encoded);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        if (con.getResponseCode() != 200) {
            throw new RuntimeException("Geocoding failed: HTTP " + con.getResponseCode());
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) response.append(line);
        }

        JSONObject json = new JSONObject(response.toString());
        JSONArray features = json.getJSONArray("features");
        if (features.isEmpty()) return null;

        JSONArray coords = features.getJSONObject(0).getJSONObject("geometry").getJSONArray("coordinates");
        return new CoordinateDTO(coords.getDouble(0), coords.getDouble(1));
    }
}
