package at.tw.tourplanner;

import at.tw.tourplanner.object.Tour;
import at.tw.tourplanner.object.TourLog;
import at.tw.tourplanner.object.TransportType;
import at.tw.tourplanner.service.TourLogService;
import at.tw.tourplanner.service.TourService;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.Start;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MainModelTest {

    @Mock
    private TourService tourService;

    @Mock
    private TourLogService tourLogService;

    private MainModel mainModel;

    @BeforeEach
    void setUp() {
        mainModel = new MainModel(tourService, tourLogService);
    }

    @BeforeAll
    static void initToolkit() {
        // Hack: initializes JavaFX toolkit for tests using headless environment
        System.setProperty("java.awt.headless", "true");
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // JavaFX already initialized
        }
    }

    @Test
    void testAddTour_success() {
        // Arrange
        Tour validTour = new Tour(TransportType.CAR, null, "TestTour", "Desc", "From", "To", 0, 0);
        mainModel.getFieldTour().setName("TestTour");
        mainModel.getFieldTour().setDescription("Desc");
        mainModel.getFieldTour().setFromLocation("From");
        mainModel.getFieldTour().setToLocation("To");
        mainModel.getFieldTour().setTransportType(TransportType.CAR);

        when(tourService.addTour(any(Tour.class))).thenReturn(true);

        // Act
        boolean result = mainModel.addTour();

        // Assert
        assertTrue(result);
        verify(tourService).addTour(any(Tour.class));
    }

    @Test
    void testAddTour_invalidTour_missingName() {
        mainModel.getFieldTour().setName("");
        mainModel.getFieldTour().setDescription("Valid");
        mainModel.getFieldTour().setFromLocation("From");
        mainModel.getFieldTour().setToLocation("To");
        mainModel.getFieldTour().setTransportType(TransportType.CAR);

        boolean result = mainModel.addTour();

        assertFalse(result);
        verify(tourService, never()).addTour(any());
    }

    @Test
    void testAddTourLog_success() {
        mainModel.getFieldTour().setName("MyTour");

        TourLog log = new TourLog(-1, LocalDate.now().toString(), "Nice trip", 3, 100, 60, 5, "MyTour");        mainModel.getTourLogs().add(log);
        setTourLogState(mainModel, log);

        when(tourLogService.addTourLog(any())).thenReturn(true);

        boolean result = mainModel.addTourLog();

        assertTrue(result);
        verify(tourLogService).addTourLog(any());
    }

    @Test
    void testDeleteTour_success() {
        mainModel.getFieldTour().setName("Tour1");

        when(tourService.deleteTour("Tour1")).thenReturn(true);

        boolean result = mainModel.deleteTour();

        assertTrue(result);
        verify(tourService).deleteTour("Tour1");
    }

    @Test
    void testDeleteTour_failure() {
        mainModel.getFieldTour().setName("Tour1");

        when(tourService.deleteTour("Tour1")).thenReturn(false);

        boolean result = mainModel.deleteTour();

        assertFalse(result);
    }

    @Test
    void testSetTourPopularity() {
        Tour tour = new Tour();
        tour.setName("Tour1");

        TourLog log = new TourLog(1, "2024-01-01", "Good", 3, 100, 50, 5, "Tour1");
        mainModel.getTourLogs().add(log);

        boolean result = mainModel.setTourPopularity(tour);

        assertTrue(result);
        assertEquals(1, tour.getPopularity());
    }

    @Test
    void testAddTourLogPreCheck_noTourSelected() {
        mainModel.getFieldTour().setName("");
        boolean result = mainModel.addTourLogPreCheck();
        assertFalse(result);
    }

    @Test
    void testAddTourLogPreCheck_success() {
        mainModel.getFieldTour().setName("Tour1");

        boolean result = mainModel.addTourLogPreCheck();

        assertTrue(result);
        assertEquals("Tour1", mainModel.getCurrentTourLog().getTourName());
    }

    // Helper
    private void setTourLogState(MainModel model, TourLog log) {
        try {
            Field field = MainModel.class.getDeclaredField("currentTourLog");
            field.setAccessible(true);
            field.set(model, log);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
