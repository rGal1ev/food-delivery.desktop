package controllers;

import models.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class CartController extends Controller {
    public Button openCatalogButton;

    @FXML
    private void initialize() {

    }

    public void onOpenCatalogButton() {
        this.viewController.changeView("catalogView");
    }
}
