package controllers;

import models.NavigationLink;
import models.View;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML private ImageView closeApp;
    @FXML private Pane viewContainer;

    @FXML private Pane ProfileViewNavigationLink;
    @FXML private Pane OrdersViewNavigationLink;

    private ViewController viewController;

    @FXML
    private void initialize() throws IOException {
        NavigationLink profileNavigationLink = new NavigationLink(ProfileViewNavigationLink);
        NavigationLink ordersNavigationLink = new NavigationLink(OrdersViewNavigationLink);

        NavigationController navigationController = new NavigationController(profileNavigationLink, ordersNavigationLink);

        View profileView = new View("/views/profile-view.fxml", "ProfileViewNavigationLink");
        View ordersView = new View("/views/orders-view.fxml", "OrdersViewNavigationLink");

        viewController = new ViewController(viewContainer,
                                            navigationController,
                                            profileView,
                                            ordersView);
    }
    public void onCloseAppClick() {
        Stage stage = (Stage) closeApp.getScene().getWindow();
        stage.close();
    }

    public void onOpenProfileViewClick() {
        viewController.changeView("profileView");
    }

    public void onOpenOrdersViewClick() {
        viewController.changeView("ordersView");
    }
}
