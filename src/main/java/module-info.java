module main {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;

    opens main to javafx.fxml;
    opens controllers to javafx.fxml;
    opens models to javafx.fxml;
    opens models.data to javafx.fxml;

    exports main;
    exports models;
    exports controllers;
    exports models.data;
}