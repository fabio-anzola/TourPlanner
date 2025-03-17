package at.tw.tourplanner;

import at.tw.tourplanner.object.Tour;
import at.tw.tourplanner.object.TransportType;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.*;

class MainModelTest {
    private Parent root = null;
    /**
     * Will be called with {@code @Before} semantics, i.e. before each test method.
     *
     * @param Stage - Will be injected by the test runner.
     */
    @Start
    private void start(Stage Stage) throws Exception{
        root = MainApplication.showStage(Stage);
    }
    //hab den teil von professor kopiert, weiß nicht was es genau tut außer dass die app gestartet wird

    @Test
    void test_add_tour(){
        MainModel mainModel = new MainModel();
        var initialSize = mainModel.getTours().size();
        Tour testTour = new Tour(TransportType.WALK, "Test Tour", "Meet Fabio and Niki at the Museum", "Start", "End");

        mainModel.getFieldTour().setTransportType(testTour.getTransportType());
        mainModel.getFieldTour().setName(testTour.getName());
        mainModel.getFieldTour().setDescription(testTour.getDescription());
        mainModel.getFieldTour().setFromLocation(testTour.getFromLocation());
        mainModel.getFieldTour().setToLocation(testTour.getToLocation());

        boolean result = mainModel.addTour();
        var newSize = mainModel.getTours().size();

        assertTrue(result,"Tour should be added successfully");
        assertEquals(initialSize + 1, newSize, "Tour list size should increase by 1");
    }

    @Test
    void test_add_tour_fail_for_name_already_exists(){
        MainModel mainModel = new MainModel();
        var initialSize = mainModel.getTours().size();
        Tour testTour = new Tour(TransportType.WALK, "Gym House", "Meet Fabio and Niki at the Museum", "Start", "End");

        mainModel.getFieldTour().setTransportType(testTour.getTransportType());
        mainModel.getFieldTour().setName(testTour.getName());
        mainModel.getFieldTour().setDescription(testTour.getDescription());
        mainModel.getFieldTour().setFromLocation(testTour.getFromLocation());
        mainModel.getFieldTour().setToLocation(testTour.getToLocation());

        boolean result = mainModel.addTour();
        var newSize = mainModel.getTours().size();

        assertFalse(result,"Tour should not be added successfully (name already exists)");
        assertEquals(initialSize, newSize, "Tour list size should stay the same");
    }

    @Test
    void test_delete_tour(){
        MainModel mainModel = new MainModel();
        var initialSize = mainModel.getTours().size();
        Tour testTour = new Tour(TransportType.WALK, "Gym House", "Meet us at the Bicep Bunker", "Zero", "Swole");

        mainModel.getFieldTour().setTransportType(testTour.getTransportType());
        mainModel.getFieldTour().setName(testTour.getName());
        mainModel.getFieldTour().setDescription(testTour.getDescription());
        mainModel.getFieldTour().setFromLocation(testTour.getFromLocation());
        mainModel.getFieldTour().setToLocation(testTour.getToLocation());

        boolean result = mainModel.deleteTour();
        var newSize = mainModel.getTours().size();

        assertTrue(result,"Tour should be deleted");
        assertEquals(initialSize - 1, newSize, "Tour list size should decrease by 1");
    }

    @Test
    void test_edit_tour(){
        MainModel mainModel = new MainModel();
        Tour testTour = new Tour(TransportType.CAR, "Gym House", "Fabio and Niki are not at the Gym House anymore", "Swole", "Zero");

        mainModel.getFieldTour().setTransportType(testTour.getTransportType());
        mainModel.getFieldTour().setName(testTour.getName());
        mainModel.getFieldTour().setDescription(testTour.getDescription());
        mainModel.getFieldTour().setFromLocation(testTour.getFromLocation());
        mainModel.getFieldTour().setToLocation(testTour.getToLocation());

        boolean result = mainModel.editTour(initialName);
        var tourListSize = mainModel.getTours().size();

        assertTrue(result, "Tour should be edited successfully");
        assertEquals("Swole", mainModel.getTours().get(tourListSize - 1).getFromLocation(),  "From location should be equal to Swole");
        assertEquals("Zero", mainModel.getTours().get(tourListSize - 1).getToLocation(),  "To location should be equal to Zero");
        assertEquals("Fabio and Niki are not at the Gym House anymore", mainModel.getTours().get(tourListSize - 1).getDescription(),  "Description should be equal to Fabio and Niki are not at the Gym House anymore");
        assertEquals(TransportType.CAR, mainModel.getTours().get(tourListSize - 1).getTransportType(),  "TransportType should be equal to Car");
    }
}
