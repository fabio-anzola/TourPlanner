module at.tw.tourplanner {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires jdk.jdi;
    requires org.json;
    requires org.kordamp.ikonli.javafx;
    requires io;
    requires kernel;
    requires layout;
    requires javafx.web;
    requires org.apache.logging.log4j;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires spring.web;
    requires org.slf4j;



    opens at.tw.tourplanner to javafx.fxml;
    opens at.tw.tourplanner.object to javafx.base;
    exports at.tw.tourplanner;
    exports at.tw.tourplanner.dto to com.fasterxml.jackson.databind;

    exports at.tw.tourplanner.object;
}