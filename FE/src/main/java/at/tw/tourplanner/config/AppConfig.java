package at.tw.tourplanner.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads and provides access to application properties.
 */
public class AppConfig {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                properties.load(input);
            } else {
                throw new RuntimeException("application.properties not found in classpath");
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load application.properties", ex);
        }
    }

    /**
     * Returns the value for the specified property key.
     *
     * @param key the property key
     * @return the property value, or null if not found
     */
    public static String get(String key) {
        return properties.getProperty(key);
    }

    /**
     * Returns the backend API URL from the properties.
     *
     * @return the backend API URL, or null if not set
     */
    public static String getBackendApiUrl() {
        return get("backend.api.url");
    }
}