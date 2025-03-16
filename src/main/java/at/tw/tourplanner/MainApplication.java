package at.tw.tourplanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception{
        showStage(stage);
    }

    public static Parent showStage(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("main-view.fxml")));
        stage.setTitle("Tour Planner");
        stage.setScene(new Scene(root, 700, 600));
        stage.setMinWidth(700);
        stage.setMinHeight(600);
        stage.show();

        return root;
    }

    public static void main(String[] args) { launch(); }
}
