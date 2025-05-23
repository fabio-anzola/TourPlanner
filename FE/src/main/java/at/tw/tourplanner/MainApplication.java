package at.tw.tourplanner;

import at.tw.tourplanner.logger.ILoggerWrapper;
import at.tw.tourplanner.logger.LoggerFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Main class to start the application.
 */
public class MainApplication extends Application {
    /** Logger instance. */
    private static final ILoggerWrapper logger = LoggerFactory.getLogger(MainApplication.class);

    /**
     * Starts the application.
     */
    @Override
    public void start(Stage stage) throws Exception{
        showStage(stage);
    }

    /**
     * Shows the initial stage.
     */
    public static Parent showStage(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("main-view.fxml")));
        stage.setTitle("Tour Planner");
        stage.setScene(new Scene(root, 1200, 800));
        stage.setMinWidth(700);
        stage.setMinHeight(700);
        stage.show();
        return root;
    }

    /**
     * Main entry point.
     */
    public static void main(String[] args) {
        try {
            launch();
        } catch (Exception e) {
            logger.fatal("An exception occurred while launching: " + e + ", Message: " + e.getCause());
        }
    }
}