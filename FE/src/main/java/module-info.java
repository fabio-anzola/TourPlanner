module at.tw.tourplanner {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires java.desktop;
    requires jdk.jdi;
    requires org.json;
    requires org.kordamp.ikonli.javafx;
    requires io;
    requires kernel;
    requires layout;


    opens at.tw.tourplanner to javafx.fxml;
    opens at.tw.tourplanner.object to javafx.base;
    exports at.tw.tourplanner;

    exports at.tw.tourplanner.object;
}