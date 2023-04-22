package controllers;

import models.NavigationLink;
import models.View;
import models.data.Cart;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    private ImageView closeApp;
    @FXML
    private Pane viewContainer;
    @FXML
    private Pane CartViewNavigationLink;
    @FXML
    private Pane CatalogViewNavigationLink;
    private ViewController viewController;
    public Cart cartState = new Cart();

    @FXML
    private void initialize() throws IOException {
        NavigationLink cartNavigationLink = new NavigationLink(CartViewNavigationLink);
        NavigationLink catalogNavigationLink = new NavigationLink(CatalogViewNavigationLink);

        NavigationController navigationController = new NavigationController(cartNavigationLink,
                                                                             catalogNavigationLink);

        View cartView = new View("/views/cart-view.fxml", "CartViewNavigationLink");
        View catalogView = new View("/views/catalog-view.fxml", "CatalogViewNavigationLink");

        viewController = new ViewController(viewContainer,
                                            navigationController,
                                            cartState,
                                            cartView,
                                            catalogView);
    }
    public void onCloseAppClick() {
        Stage stage = (Stage) closeApp.getScene().getWindow();
        stage.close();
    }
    public void onOpenCartViewClick() {
        viewController.changeView("cartView");
    }
    public void onOpenCatalogViewClick() {
        viewController.changeView("catalogView");
    }
    public void onOpenProfileViewClick() {
        viewController.changeView("profileView");
    }
}
