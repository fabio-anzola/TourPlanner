module at.tw.tourplanner {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;


    opens at.tw.tourplanner to javafx.fxml;
    exports at.tw.tourplanner;
}