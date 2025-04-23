package at.tw.tourplanner.service;

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
import com.itextpdf.layout.properties.UnitValue;

import java.io.File;
import java.io.IOException;

import java.util.List;

public class pdfGenerationService {

    File file;
    PdfWriter writer;
    PdfDocument pdf;
    Document document;

    public pdfGenerationService(File file) {
        this.file = file;
        try{
            this.writer = new PdfWriter(file);
            this.pdf = new PdfDocument(writer);
            this.document = new Document(pdf);
        } catch (IOException e) {
            System.err.println("Error in constructor for pdfGenerationService: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void generateTourPdf(Tour tour, List<TourLog> tourLogs) throws IOException {
        Paragraph title = new Paragraph("Tour Report")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(18)
                .setFontColor(ColorConstants.BLUE);
        document.add(title);

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

        System.out.println("Tour PDF generated at: " + file.getAbsolutePath());
    }

    public void generateSummaryPdf(Tour tour, List<TourLog> tourLogs) throws IOException {
        Paragraph title = new Paragraph("Generate Summary Pdf")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(18)
                .setFontColor(ColorConstants.BLUE);
        document.add(title);

        document.add(new Paragraph("Tour Name: " + tour.getName()));
        document.add(new Paragraph("Description: " + tour.getDescription()));
        document.add(new Paragraph("From: " + tour.getFromLocation()));
        document.add(new Paragraph("To: " + tour.getToLocation()));
        document.add(new Paragraph("Transport Type: " + tour.getTransportType()));

        document.close();

        System.out.println("Summary PDF generated at: " + file.getAbsolutePath());
    }
}
