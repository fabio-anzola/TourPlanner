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
 * The Main Class to start the Application
 */
public class MainApplication extends Application {
    // log4j
    private static final ILoggerWrapper logger = LoggerFactory.getLogger(MainApplication.class);

    /**
     * Method to start the app
     *
     * @param stage a stage
     * @throws Exception passed exceptions
     */
    @Override
    public void start(Stage stage) throws Exception{
        showStage(stage);
    }

    /**
     * Method to show initial stage
     *
     * @param stage a stage
     * @return the root stage built form fxml
     * @throws Exception passed exceptions
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
     * Main method
     *
     * @param args args passed from cli
     */
    public static void main(String[] args) {
        try {
            launch();
        } catch (Exception e) {
            logger.fatal("An exception occurred while launching: " + e + ", Message: " + e.getCause());
        }
    }
}
