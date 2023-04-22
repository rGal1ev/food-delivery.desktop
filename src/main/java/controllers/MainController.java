package controllers;

import models.Cart;
import helpers.ViewsController;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainController {
    @FXML
    private ImageView closeApp;
    @FXML
    private Pane viewContainer;
    @FXML
    private Pane openCartView;
    @FXML
    private Pane openProfileView;
    @FXML
    private Pane openCatalogView;
    private ViewsController viewsController = null;
    public Cart cart = new Cart();

    @FXML
    private void initialize() throws IOException {
        if (viewsController == null) {
            viewsController = new ViewsController(viewContainer,
                                                  cart,
                                     "/views/catalog-view.fxml",
                                                  "/views/cart-view.fxml");
        }
    }
    public void onCloseAppClick() {
        Stage stage = (Stage) closeApp.getScene().getWindow();
        stage.close();
    }
    public void onOpenCartViewClick() {
        if (!isActive(openCartView)) {
            openCartView.getStyleClass().add(1, "active");
        }

        try {
            openProfileView.getStyleClass().remove(1);
        } catch (Exception ignored) {}

        try {
            openCatalogView.getStyleClass().remove(1);
        } catch (Exception ignored) {}

        viewsController.changeView("cartView");
    }

    public void onOpenCatalogViewClick() {
        if (!isActive(openCatalogView)) {
            openCatalogView.getStyleClass().add(1, "active");
        }

        try {
            openProfileView.getStyleClass().remove(1);
        } catch (Exception ignored) {}

        try {
            openCartView.getStyleClass().remove(1);
        } catch (Exception ignored) {}

        System.out.println(this.cart.foodList);
        viewsController.changeView("catalogView");
    }

    public void onOpenProfileViewClick() {
        if (!isActive(openProfileView)) {
            openProfileView.getStyleClass().add(1, "active");
        }

        try {
            openCatalogView.getStyleClass().remove(1);
        } catch (Exception ignored) {}

        try {
            openCartView.getStyleClass().remove(1);
        } catch (Exception ignored) {}

        viewsController.changeView("profileView");
    }

    boolean isActive(Pane view) {
        boolean isActive = false;

        for (String styleClass : view.getStyleClass()) {
            if (Objects.equals(styleClass, "active")) {
                isActive = true;
            }
        }

        return isActive;
    }
}
