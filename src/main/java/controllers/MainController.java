package controllers;

import models.NavigationLink;
import models.View;
import models.data.Cart;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class MainController {
    @FXML
    private ImageView closeApp;
    @FXML
    private Pane viewContainer;
    @FXML
    private Pane CartViewNavigationLink;
    @FXML
    private Pane CatalogViewNavigationLink;
    @FXML
    private Pane ProfileViewNavigationLink;

    private ViewController viewController;
    public Cart cartState = new Cart();

    @FXML
    private void initialize() throws IOException {
        NavigationLink cartNavigationLink = new NavigationLink(CartViewNavigationLink);
        NavigationLink catalogNavigationLink = new NavigationLink(CatalogViewNavigationLink);
        NavigationLink profileNavigationLink = new NavigationLink(ProfileViewNavigationLink);

        NavigationController navigationController = new NavigationController(cartNavigationLink,
                                                                             catalogNavigationLink,
                                                                             profileNavigationLink);

        View cartView = new View("/views/cart-view.fxml", "CartViewNavigationLink");
        View catalogView = new View("/views/catalog-view.fxml", "CatalogViewNavigationLink");
        View profileView = new View("/views/profile-view.fxml", "ProfileViewNavigationLink");

        viewController = new ViewController(viewContainer,
                                            navigationController,
                                            cartState,
                                            cartView,
                                            catalogView,
                                            profileView);
    }
    public void onCloseAppClick() {
        Stage stage = (Stage) closeApp.getScene().getWindow();
        stage.close();
    }
    public void onOpenCartViewClick() throws SQLException {
        viewController.changeView("cartView");
    }
    public void onOpenCatalogViewClick() throws SQLException {
        viewController.changeView("catalogView");
    }
    public void onOpenProfileViewClick() throws SQLException {
        viewController.changeView("profileView");
    }
}
