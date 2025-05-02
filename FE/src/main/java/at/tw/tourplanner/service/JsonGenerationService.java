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

public class JsonGenerationService {
    File file;
    // log4j
    private static final ILoggerWrapper logger = LoggerFactory.getLogger(MainApplication.class);

    public JsonGenerationService(File file){
        this.file = file;
    }

    public void generateTourJson(List<Tour> tours) throws IOException {
        logger.debug("entered function: generateTourJson (JsonGenerationService) with parameter: " + tours);
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        writer.writeValue(file, tours);
    }
}
