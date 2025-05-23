package at.tw.tourplanner.service;

import at.tw.tourplanner.MainApplication;
import at.tw.tourplanner.config.AppConfig;
import at.tw.tourplanner.logger.ILoggerWrapper;
import at.tw.tourplanner.logger.LoggerFactory;
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
import java.util.Arrays;
import java.util.Locale;
import javafx.scene.web.WebView;

import java.io.FileWriter;

/**
 * Service for geocoding, routing, and displaying routes.
 */
public class RouteImageService {
    /** The base backend API URL. */
    private static final String BASE_URL = AppConfig.getBackendApiUrl() + "/api";

    /** The logger instance. */
    private static final ILoggerWrapper logger = LoggerFactory.getLogger(MainApplication.class);

    /**
     * Geocodes an address and returns coordinates.
     */
    public double[] geocode(String address) throws Exception {
        logger.debug("Entered function geocode (RouteImageService) with parameter: " + address);
        String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
        String urlStr = BASE_URL + "/coordinates?address=" + encodedAddress;

        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        if (responseCode != 200) {
            logger.error("Geocoding failed with HTTP code: " + responseCode);
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

    /**
     * Gets route GeoJSON and summary data for a route.
     */
    public String[] getRouteGeoJson(double[] start, double[] end, TransportType transportType) throws Exception {
        logger.debug("Entered function getRouteGeoJson (RouteImageService) with parameter: " + Arrays.toString(start) + ", " + Arrays.toString(end) + ", " + transportType);
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
            logger.error("GeoJSON request failed with HTTP code: " + responseCode);
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

    /**
     * Creates a WebView with a given GeoJSON.
     */
    public WebView createWebViewWithGeoJson(JSONObject geoJson) throws IOException {
        logger.debug("Entered function createWebViewWithGeoJson (RouteImageService) with parameter: " + geoJson);
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

    /**
     * Returns route data for two addresses and transport type.
     */
    public RouteData getRouteData(String startAddress, String endAddress, TransportType transportType) throws Exception {
        logger.debug("Entered function getRouteData (RouteImageService) with parameter: " + startAddress + ", " + endAddress + ", " + transportType);
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