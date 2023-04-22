module main {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;

    opens main to javafx.fxml;
    opens controllers to javafx.fxml;
    opens helpers to javafx.fxml;

    exports main;
    exports helpers;
    exports models;
    exports controllers;
}