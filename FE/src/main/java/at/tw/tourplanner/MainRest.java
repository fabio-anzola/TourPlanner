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
    private static final String BASE_URL = "http://localhost:8080/api";

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

        System.out.println("Requesting full GeoJSON route...");
        getRouteGeoJson(startCoords, endCoords);
    }

    public static double[] geocode(String address) throws Exception {
        String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
        String urlStr = BASE_URL + "/coordinates?address=" + encodedAddress;

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
        return new double[]{json.getDouble("lon"), json.getDouble("lat")};
    }

    public static void getRouteGeoJson(double[] start, double[] end) throws Exception {
        String urlStr = String.format(
                Locale.US,
                BASE_URL + "/route?startLon=%f&startLat=%f&endLon=%f&endLat=%f&mode=car",
                (start[0]), start[1], end[0], end[1]
        );

        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");

        int responseCode = con.getResponseCode();
        if (responseCode != 200) {
            System.err.println("GeoJSON request failed with HTTP code: " + responseCode);
            return;
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
        }

        JSONObject json = new JSONObject(response.toString());
        JSONObject summary = json.getJSONArray("features").getJSONObject(0).getJSONObject("properties").getJSONObject("summary");

        double duration = summary.getDouble("duration");
        double distance = summary.getDouble("distance");
        System.out.printf("Duration: %.2f minutes%n", duration / 60);
        System.out.printf("Distance: %.2f km%n", distance / 1000);

        System.out.println("GeoJSON route received:");
        System.out.println(response);
    }
}