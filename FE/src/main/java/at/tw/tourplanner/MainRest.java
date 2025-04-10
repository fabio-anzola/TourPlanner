package at.tw.tourplanner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class MainRest {
    private static final String API_KEY = "";

    public static void main(String[] args) throws Exception {
        String startAddress = "Brunn am Gebirge, Bahnhofplatz";
        String endAddress = "Wien, Höchstädtplatz";

        System.out.println("Geocoding start...");
        double[] startCoords = geocode(startAddress);
        double[] endCoords = geocode(endAddress);

        if (startCoords == null || endCoords == null) {
            System.err.println("Failed to geocode one or both addresses.");
            return;
        }

        System.out.printf("Start coordinates: %.5f, %.5f%n", startCoords[0], startCoords[1]);
        System.out.printf("End coordinates:   %.5f, %.5f%n", endCoords[0], endCoords[1]);

        System.out.println("Requesting route...");
        getRoute(startCoords, endCoords);
    }

    public static double[] geocode(String address) throws Exception {
        String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
        String urlStr = "https://api.openrouteservice.org/geocode/search?api_key=" + API_KEY + "&text=" + encodedAddress;

        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        if (responseCode != 200) {
            System.err.println("Geocoding failed with HTTP code: " + responseCode);
            return null;
        }

        StringBuilder response;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
        }

        JSONObject json = new JSONObject(response.toString());
        JSONArray features = json.getJSONArray("features");
        if (features.isEmpty()) {
            return null;
        }

        JSONArray coords = features.getJSONObject(0).getJSONObject("geometry").getJSONArray("coordinates");
        return new double[]{coords.getDouble(0), coords.getDouble(1)};
    }

    public static void getRoute(double[] start, double[] end) throws Exception {
        URL url = new URL("https://api.openrouteservice.org/v2/directions/driving-car");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", API_KEY);
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        String jsonInput = String.format(Locale.US, """
                {
                  "coordinates": [
                    [%f, %f],
                    [%f, %f]
                  ]
                }
                """, start[0], start[1], end[0], end[1]);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = con.getResponseCode();
        if (responseCode != 200) {
            System.err.println("Route request failed with HTTP code: " + responseCode);
            return;
        }

        StringBuilder response;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
        }

        JSONObject json = new JSONObject(response.toString());
        JSONObject summary = json.getJSONArray("routes").getJSONObject(0).getJSONObject("summary");

        double duration = summary.getDouble("duration");
        double distance = summary.getDouble("distance");

        System.out.printf("Duration: %.2f minutes%n", duration / 60);
        System.out.printf("Distance: %.2f km%n", distance / 1000);
    }
}