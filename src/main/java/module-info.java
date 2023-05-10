module main {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires java.sql;
    requires retrofit2;
    requires retrofit2.converter.gson;
    requires com.google.gson;
    requires java.net.http;
    requires okhttp3;

    opens main to javafx.fxml;
    opens controllers to javafx.fxml;
    opens models to javafx.fxml;

    opens models.data;

    exports main;
    exports models;
    exports controllers;
    exports models.data;
}