package at.tw.tourplanner.service;

import at.tw.tourplanner.object.RouteData;
import at.tw.tourplanner.object.TransportType;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;
import javafx.scene.web.WebView;
import org.json.JSONObject;

import java.io.FileWriter;

public class RouteImageService {
    private static final String BASE_URL = "http://localhost:8080/api";

    public double[] geocode(String address) throws Exception {
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

    public String[] getRouteGeoJson(double[] start, double[] end, TransportType transportType) throws Exception {
        String urlStr = String.format(
                Locale.US,
                BASE_URL + "/route?startLon=%f&startLat=%f&endLon=%f&endLat=%f&mode=%s",
                (start[0]), start[1], end[0], end[1], transportType.name()
        );

        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");

        int responseCode = con.getResponseCode();
        if (responseCode != 200) {
            System.err.println("GeoJSON request failed with HTTP code: " + responseCode);
            return null;
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

        return new String[]{String.valueOf(duration / 60), String.valueOf(distance / 1000), response.toString()};
    }

    public WebView createWebViewWithGeoJson(JSONObject geoJson) throws IOException {
        String htmlTemplate = Files.readString(Paths.get("src/main/resources/map_template.html"));
        String htmlContent = htmlTemplate.replace("__GEOJSON__", geoJson.toString());

        File tempHtml = File.createTempFile("map", ".html");
        try (FileWriter writer = new FileWriter(tempHtml)) {
            writer.write(htmlContent);
        }

        WebView webView = new WebView();
        webView.getEngine().load(tempHtml.toURI().toString());

        return webView;
    }

    public RouteData getRouteData(String startAddress, String endAddress, TransportType transportType) throws Exception {
        RouteImageService restClient = new RouteImageService();
        double[] startCoords = restClient.geocode(startAddress);
        double[] endCoords = restClient.geocode(endAddress);

        if (startCoords == null || endCoords == null) {
            throw new Exception("Failed to geocode one or both addresses.");
        }

        String[] result = restClient.getRouteGeoJson(startCoords, endCoords, transportType);
        RouteData routeData = new RouteData(result);

        return routeData;
    }
}
