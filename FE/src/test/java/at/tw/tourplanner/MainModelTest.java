package at.tw.tourplanner;

import at.tw.tourplanner.object.Tour;
import at.tw.tourplanner.object.TourLog;
import at.tw.tourplanner.object.TransportType;
import at.tw.tourplanner.service.TourLogService;
import at.tw.tourplanner.service.TourService;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;

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
    void testAddTour_missingName_failure() {
        mainModel.getFieldTour().setName("");
        mainModel.getFieldTour().setDescription("Invalid");
        mainModel.getFieldTour().setFromLocation("From");
        mainModel.getFieldTour().setToLocation("To");
        mainModel.getFieldTour().setTransportType(TransportType.CAR);

        boolean result = mainModel.addTour();

        assertFalse(result);
        verify(tourService, never()).addTour(any());
    }

    @Test
    void testAddTour_duplicateTour_failure() {
        mainModel.getFieldTour().setName("Duplicate");
        mainModel.getFieldTour().setDescription("Invalid");
        mainModel.getFieldTour().setFromLocation("From");
        mainModel.getFieldTour().setToLocation("To");
        mainModel.getFieldTour().setTransportType(TransportType.CAR);

        mainModel.getTours().add(new Tour(TransportType.CAR, null, "Duplicate", "Invalid", "From", "To", 0, 0));

        boolean result = mainModel.addTour();

        assertFalse(result);
        verify(tourService, never()).addTour(any());
    }

    @Test
    void testAddTourLog_success() {
        mainModel.getFieldTour().setName("MyTour");

        TourLog log = new TourLog(1, LocalDate.now().toString(), "Nice trip", 3, 100, 60, 5, "MyTour");
        mainModel.getTourLogs().add(log);
        setTourLogState(mainModel, log);

        when(tourLogService.addTourLog(any())).thenReturn(true);

        boolean result = mainModel.addTourLog();

        assertTrue(result);
        verify(tourLogService).addTourLog(any());
    }

    @Test
    void testAddTourLog_missingComment_failure() {
        mainModel.getFieldTour().setName("MyTour");

        TourLog log = new TourLog(1, LocalDate.now().toString(), "", 3, 100, 60, 5, "MyTour");
        mainModel.getTourLogs().add(log);
        setTourLogState(mainModel, log);

        boolean result = mainModel.addTourLog();

        assertFalse(result);
        verify(tourLogService, never()).addTourLog(any());
    }

    @Test
    void testEditTour_success() {
        mainModel.getFieldTour().setName("TestTour");
        mainModel.getFieldTour().setDescription("A good tour");
        mainModel.getFieldTour().setFromLocation("A");
        mainModel.getFieldTour().setToLocation("B");
        mainModel.getFieldTour().setTransportType(TransportType.CAR);

        when(tourService.updateTour("TestTour", mainModel.getFieldTour())).thenReturn(true);

        boolean result = mainModel.editTour("TestTour");

        assertTrue(result);
        verify(tourService).updateTour("TestTour", mainModel.getFieldTour());
    }

    @Test
    void testEditTour_invalidTour_failure() {
        mainModel.getFieldTour().setName("NewName");
        mainModel.getFieldTour().setDescription("");
        mainModel.getFieldTour().setFromLocation("A");
        mainModel.getFieldTour().setToLocation("B");
        mainModel.getFieldTour().setTransportType(TransportType.CAR);

        boolean result = mainModel.editTour("NewName");

        assertFalse(result);
        verify(tourService, never()).updateTour(any(), any());
    }

    @Test
    void testEditTourLog_success() {
        TourLog log1 = new TourLog(1, "2024-01-01", "Ok", 3, 100, 60, 3, "Tour1");
        TourLog log2 = new TourLog(2, "2024-01-01", "Ok", 2, 50, 30, 4, "Tour1");
        mainModel.getTourLogs().add(log1);
        mainModel.getTourLogs().add(log2);

        when(tourLogService.updateTourLog(1, log1)).thenReturn(true);
        when(tourLogService.updateTourLog(2, log2)).thenReturn(true);

        boolean result = mainModel.editTourLog();

        assertTrue(result);
        verify(tourLogService).updateTourLog(1, log1);
        verify(tourLogService).updateTourLog(2, log2);
    }

    @Test
    void testEditTourLog_failure() {
        TourLog log1 = new TourLog(1, "2024-01-01", "Bad", 3, 100, 60, 3, "Tour1");
        TourLog log2 = new TourLog(2, "2024-01-01", "Bad", 2, 50, 30, 4, "Tour1");
        mainModel.getTourLogs().addAll(log1, log2);

        when(tourLogService.updateTourLog(1, log1)).thenReturn(false); // failure

        boolean result = mainModel.editTourLog();

        assertFalse(result);
        verify(tourLogService).updateTourLog(1, log1);
        verify(tourLogService, never()).updateTourLog(2, log2);
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
    void testDeleteTourLog_success() {
        TourLog log = new TourLog(1, "2024-01-01", "Comment", 3, 100, 50, 5, "Tour1");
        mainModel.getTourLogs().add(log);

        setTourLogState(mainModel, log);

        when(tourLogService.deleteTourLog(1)).thenReturn(true);

        boolean result = mainModel.deleteTourLog();

        assertTrue(result);
        verify(tourLogService).deleteTourLog(1);
    }

    @Test
    void testDeleteTourLog_failure() {
        TourLog log = new TourLog(1, "2024-01-01", "Comment", 3, 100, 50, 5, "Tour1");
        mainModel.getTourLogs().add(log);

        setTourLogState(mainModel, log);

        when(tourLogService.deleteTourLog(1)).thenReturn(false);

        boolean result = mainModel.deleteTourLog();

        assertFalse(result);
        verify(tourLogService).deleteTourLog(1);
    }

    @Test
    void testSetTourPopularity_success() {
        Tour tour = new Tour();
        tour.setName("Tour1");

        TourLog log = new TourLog(1, "2024-01-01", "Good", 3, 100, 50, 5, "Tour1");
        mainModel.getTourLogs().add(log);

        boolean result = mainModel.setTourPopularity(tour);

        assertTrue(result);
        assertEquals(1, tour.getPopularity());
    }

    @Test
    void testSetTourChildFriendliness_success(){
        Tour tour = new Tour();
        tour.setName("TestTour");
        mainModel.getFieldTour().setName("TestTour");

        TourLog log1 = new TourLog(1, "2024-01-01", "Good", 3, 100, 50, 5, "TestTour");
        TourLog log2 = new TourLog(2, "2024-01-01", "Bad", 5, 50, 80, 2, "TestTour");
        mainModel.getTourLogs().add(log1);
        mainModel.getTourLogs().add(log2);

        boolean result = mainModel.setTourChildFriendliness();

        assertTrue(result);
        assertEquals(2, mainModel.getFieldTour().getChildFriendliness());
    }

    @Test
    void testSetTourChildFriendliness_noLogs_failure() {
        Tour tour = new Tour();
        tour.setName("TestTour");
        mainModel.getFieldTour().setName("TestTour");

        boolean result = mainModel.setTourChildFriendliness();

        assertTrue(result);
        assertEquals(-1, mainModel.getFieldTour().getChildFriendliness());
    }

    @Test
    void testAddTourLogPreCheck_noTourSelected_failure() {
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

    @Test
    void testImportTourJson_success() throws Exception {
        File importFile = File.createTempFile("tour", ".json");
        try (FileWriter writer = new FileWriter(importFile)) {
            writer.write("""
                [
                  {
                    "name": "Test Tour",
                    "description": "test",
                    "fromLocation": "a",
                    "toLocation": "b",
                    "transportType": "CAR"
                  },
                  {
                    "name": "Test Tour 2",
                    "description": "test 2",
                    "fromLocation": "b",
                    "toLocation": "a",
                    "transportType": "BICYCLE"
                  },
                  {
                    "name": "Test Tour 3",
                    "description": "test 3",
                    "fromLocation": "a",
                    "toLocation": "z",
                    "transportType": "WALK"
                  }
                ]"""
            );
        }

        mainModel.getTours().add(new Tour(TransportType.BICYCLE, null, "Test Tour 2", "test 2", "b", "a", 0, 0));

        mainModel.importTourJson(importFile);

        verify(tourService).addTour(argThat(t -> t.getName().equals("Test Tour")));
        verify(tourService, never()).addTour(argThat(t -> t.getName().equals("Test Tour 2")));
        verify(tourService).addTour(argThat(t -> t.getName().equals("Test Tour 3")));
    }

    @Test
    void testImportTourJson_invalidContent_failure() throws Exception {
        File importFile = File.createTempFile("tour", ".json");
        try (FileWriter writer = new FileWriter(importFile)) {
            writer.write("Some invalid content");
        }

        assertThrows(IOException.class, () -> mainModel.importTourJson(importFile));

        verify(tourService, never()).addTour(any());
    }

    @Test
    void testImportTourJson_emptyJson_failure() throws Exception {
        File importFile = File.createTempFile("tour", ".json");
        try (FileWriter writer = new FileWriter(importFile)) {
            writer.write("[]");
        }

        mainModel.importTourJson(importFile);

        verify(tourService, never()).addTour(any());
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
