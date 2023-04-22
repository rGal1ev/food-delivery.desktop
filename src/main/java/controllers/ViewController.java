package controllers;

import javafx.scene.layout.Pane;
import models.View;
import models.data.Cart;

import java.util.ArrayList;
import java.util.Objects;

public class ViewController {
    private final ArrayList<View> viewList = new ArrayList<>();
    private final Pane viewContainer;
    private final NavigationController navigationController;
    public ViewController(Pane rootContainer, NavigationController navigationController, Cart cart, View...views) {
        this.viewContainer = rootContainer;
        this.navigationController = navigationController;

        for (View view : views) {
            view.getController().setCarState(cart);
            view.getController().setViewController(this);

            viewList.add(view);
        }
    }

    public void changeView(String viewID) {
        for (View view : viewList) {
            if (Objects.equals(view.getViewNode().getId(), viewID)) {
                viewContainer.getChildren().clear();
                viewContainer.getChildren().add(view.getViewNode());

                navigationController.changeActiveNavigation(view.getViewNavigationLinkID());
            }
        }
    }
}
