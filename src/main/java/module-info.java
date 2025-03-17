module at.tw.tourplanner {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires java.desktop;
A    requires jdk.jdi;


    opens at.tw.tourplanner to javafx.fxml;
    opens at.tw.tourplanner.object to javafx.base;
    exports at.tw.tourplanner;
}