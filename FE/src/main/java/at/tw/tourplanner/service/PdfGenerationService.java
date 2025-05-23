package at.tw.tourplanner.service;

import at.tw.tourplanner.MainApplication;
import at.tw.tourplanner.logger.ILoggerWrapper;
import at.tw.tourplanner.logger.LoggerFactory;
import at.tw.tourplanner.object.Tour;
import at.tw.tourplanner.object.TourLog;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.element.Image;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Service for generating PDF reports.
 */
public class PdfGenerationService {
    /** The output file for PDF data. */
    File file;

    /** PDF writer. */
    PdfWriter writer;

    /** PDF document. */
    PdfDocument pdf;

    /** iText document. */
    Document document;

    /** The logger instance. */
    private static final ILoggerWrapper logger = LoggerFactory.getLogger(MainApplication.class);

    /**
     * Creates a PdfGenerationService for the given file.
     */
    public PdfGenerationService(File file) {
        this.file = file;
        try{
            this.writer = new PdfWriter(file);
            this.pdf = new PdfDocument(writer);
            this.document = new Document(pdf);
        } catch (IOException e) {
            logger.error("Error in constructor for pdfGenerationService: " + e + ", Message: " + e.getMessage());
        }
    }

    /**
     * Writes a tour report PDF file.
     */
    public void generateTourPdf(Tour tour, List<TourLog> tourLogs) throws IOException {
        logger.debug("entered function: generateTourPdf (PdfGenerationService) with parameter: " + tour + " and " + tourLogs);
        Paragraph title = new Paragraph("Tour Report")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(18)
                .setFontColor(ColorConstants.BLUE);
        document.add(title);

        ImageData imageData = ImageDataFactory.create(tour.getRouteImage(), Color.BLACK);
        document.add(new Image(imageData));

        document.add(new Paragraph("Tour Name: " + tour.getName()));
        document.add(new Paragraph("Description: " + tour.getDescription()));
        document.add(new Paragraph("From: " + tour.getFromLocation()));
        document.add(new Paragraph("To: " + tour.getToLocation()));
        document.add(new Paragraph("Transport Type: " + tour.getTransportType()));
        document.add(new Paragraph("----------------------------------"));

        if (tourLogs.isEmpty()) {
            document.add(new Paragraph("No logs available."));
        } else {
            int i = 1;
            for (TourLog log: tourLogs) {
                Paragraph header = new Paragraph("Log " + i)
                        .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                        .setFontSize(14)
                        .setFontColor(ColorConstants.RED);
                document.add(header);
                document.add(new Paragraph("Date: " + log.getDate()));
                document.add(new Paragraph("Comment: " + log.getComment()));
                document.add(new Paragraph("Difficulty: " + log.getDifficulty()));
                document.add(new Paragraph("Distance: " + log.getTotalDistance()));
                document.add(new Paragraph("Time: " + log.getTotalTime()));
                document.add(new Paragraph("Rating: " + log.getRating()));
                document.add(new Paragraph("----------------------------------"));
                i++;
            }
        }
        document.close();
    }

    /**
     * Writes a summary PDF file for all tours.
     */
    public void generateSummaryPdf(List<Tour> tours, List<TourLog> tourLogs) throws IOException {
        logger.debug("entered function: generateSummaryPdf (PdfGenerationService) with parameter: " + tours + " and " + tourLogs);
        if (tours.isEmpty()) {
            document.add(new Paragraph("no tours found"));
        } else {
            for (Tour tour : tours) {
                List<TourLog> logsForTour = tourLogs.stream()
                        .filter(log -> log.getTourName().equalsIgnoreCase(tour.getName()))
                        .toList();

                double avgTime = logsForTour.stream().mapToInt(TourLog::getParsedTotalTime).average().orElse(0);
                double avgDistance = logsForTour.stream().mapToInt(TourLog::getParsedTotalDistance).average().orElse(0);
                double avgRating = logsForTour.stream().mapToInt(TourLog::getParsedRating).average().orElse(0);

                Paragraph header = new Paragraph("Tour: " + tour.getName())
                        .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                        .setFontSize(14)
                        .setFontColor(ColorConstants.RED);
                document.add(header);
                document.add(new Paragraph("  Avg Time: " + avgTime));
                document.add(new Paragraph("  Avg Distance: " + avgDistance));
                document.add(new Paragraph("  Avg Rating: " + avgRating));
                document.add(new Paragraph("--------------------------------------"));
            }
        }
        document.close();
    }
}