package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Loader extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Food Delivery");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);

        stage.getIcons().add(new Image(getClass().getResource("/assets/app-icon.png").openStream()));

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}