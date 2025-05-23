package at.tw.tourplanner.service;

import at.tw.tourplanner.MainApplication;
import at.tw.tourplanner.logger.ILoggerWrapper;
import at.tw.tourplanner.logger.LoggerFactory;
import at.tw.tourplanner.object.Tour;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Service for generating tour JSON files.
 */
public class JsonGenerationService {
    /** The output file for JSON data. */
    File file;

    /** The logger instance. */
    private static final ILoggerWrapper logger = LoggerFactory.getLogger(MainApplication.class);

    /**
     * Creates a JsonGenerationService for the given file.
     */
    public JsonGenerationService(File file){
        this.file = file;
    }

    /**
     * Writes a list of tours to a JSON file.
     */
    public void generateTourJson(List<Tour> tours) throws IOException {
        logger.debug("entered function: generateTourJson (JsonGenerationService) with parameter: " + tours);
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        writer.writeValue(file, tours);
    }
}