package at.tw.tourplanner;

import at.tw.tourplanner.object.Tour;
import at.tw.tourplanner.object.TransportType;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.Start;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Class for Main Model
 */
class MainModelTest {
    private Parent root = null;

    /**
     * Initializes JavaFX before running any tests.
     */
    @BeforeAll
    static void initJavaFX() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);  // Initialize JavaFX
        latch.await();  // Wait until JavaFX is fully initialized
    }

    /**
     * Will be called before each test method.
     *
     * @param Stage - Will be injected by the test runner.
     */
    @Start
    private void start(Stage Stage) throws Exception{
        root = MainApplication.showStage(Stage);
    }

    /**
     * Test adding a tour - should work
     */
    @Test
    void test_add_tour(){
        MainModel mainModel = new MainModel();
        var initialSize = mainModel.getTours().size();
        Tour testTour = new Tour(
                TransportType.WALK,
                new Image(Objects.requireNonNull(getClass().getResource("/routeImages/placeholder_map.png")).toExternalForm()),
                "Test Tour",
                "Meet Fabio and Niki at the Museum",
                "Start",
                "End",
                0,
                0
        );

        mainModel.getFieldTour().setTransportType(testTour.getTransportType());
        mainModel.getFieldTour().setRouteImage(testTour.getRouteImage());
        mainModel.getFieldTour().setName(testTour.getName());
        mainModel.getFieldTour().setDescription(testTour.getDescription());
        mainModel.getFieldTour().setFromLocation(testTour.getFromLocation());
        mainModel.getFieldTour().setToLocation(testTour.getToLocation());

        boolean result = mainModel.addTour();
        var newSize = mainModel.getTours().size();

        assertTrue(result,"Tour should be added successfully");
        assertEquals(initialSize + 1, newSize, "Tour list size should increase by 1");
    }

    /**
     * Test adding a Tour with an existing name - should fail
     */
    @Test
    void test_add_tour_fail_for_name_already_exists(){
        MainModel mainModel = new MainModel();
        var initialSize = mainModel.getTours().size();
        Tour testTour = new Tour(
                TransportType.WALK,
                new Image(Objects.requireNonNull(getClass().getResource("/routeImages/placeholder_map.png")).toExternalForm()),
                "Hiking Tour #1",
                "Meet Fabio and Niki at the Museum",
                "Start",
                "End",
                0,
                0
        );

        mainModel.getFieldTour().setTransportType(testTour.getTransportType());
        mainModel.getFieldTour().setRouteImage(testTour.getRouteImage());
        mainModel.getFieldTour().setName(testTour.getName());
        mainModel.getFieldTour().setDescription(testTour.getDescription());
        mainModel.getFieldTour().setFromLocation(testTour.getFromLocation());
        mainModel.getFieldTour().setToLocation(testTour.getToLocation());

        boolean result = mainModel.addTour();
        var newSize = mainModel.getTours().size();

        assertFalse(result,"Tour should not be added successfully (name already exists)");
        assertEquals(initialSize, newSize, "Tour list size should stay the same");
    }

    /**
     * Test delete tour - should work
     */
    @Test
    void test_delete_tour(){
        MainModel mainModel = new MainModel();
        var initialSize = mainModel.getTours().size();
        Tour testTour = new Tour(
                TransportType.WALK,
                new Image(Objects.requireNonNull(getClass().getResource("/routeImages/placeholder_map.png")).toExternalForm()),
                "Hiking Tour #1",
                "Sunday Family Hiking Tour",
                "Wien",
                "Burgenland",
                0,
                0
        );

        mainModel.getFieldTour().setTransportType(testTour.getTransportType());
        mainModel.getFieldTour().setRouteImage(testTour.getRouteImage());
        mainModel.getFieldTour().setName(testTour.getName());
        mainModel.getFieldTour().setDescription(testTour.getDescription());
        mainModel.getFieldTour().setFromLocation(testTour.getFromLocation());
        mainModel.getFieldTour().setToLocation(testTour.getToLocation());

        boolean result = mainModel.deleteTour();
        var newSize = mainModel.getTours().size();

        assertTrue(result,"Tour should be deleted");
        assertEquals(initialSize - 1, newSize, "Tour list size should decrease by 1");
    }

    /**
     * Test edit a tour - should work
     */
    @Test
    void test_edit_tour(){
        MainModel mainModel = new MainModel();
        Tour testTour = new Tour(
                TransportType.CAR,
                new Image(Objects.requireNonNull(getClass().getResource("/routeImages/placeholder_map.png")).toExternalForm()),
                "Hiking Tour #1",
                "Monday Family Hiking Tour",
                "Burgenland",
                "Wien",
                0,
                0
        );

        mainModel.getFieldTour().setTransportType(testTour.getTransportType());
        mainModel.getFieldTour().setRouteImage(testTour.getRouteImage());
        mainModel.getFieldTour().setName(testTour.getName());
        mainModel.getFieldTour().setDescription(testTour.getDescription());
        mainModel.getFieldTour().setFromLocation(testTour.getFromLocation());
        mainModel.getFieldTour().setToLocation(testTour.getToLocation());

        boolean result = mainModel.editTour(testTour.getName());
        var tourListSize = mainModel.getTours().size();

        assertTrue(result, "Tour should be edited successfully");
        assertEquals("Burgenland", mainModel.getTours().get(tourListSize - 1).getFromLocation(),  "From location should be equal to Burgenland");
        assertEquals("Wien", mainModel.getTours().get(tourListSize - 1).getToLocation(),  "To location should be equal to Wien");
        assertEquals("Monday Family Hiking Tour", mainModel.getTours().get(tourListSize - 1).getDescription(),  "Description should be equal to Monday Family Hiking Tour");
        assertEquals(TransportType.CAR, mainModel.getTours().get(tourListSize - 1).getTransportType(),  "TransportType should be equal to Car");
    }
}
