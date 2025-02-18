module at.tw.tourplanner {
    requires javafx.controls;
    requires javafx.fxml;


    opens at.tw.tourplanner to javafx.fxml;
    exports at.tw.tourplanner;
}