package controllers;

import javafx.scene.layout.Pane;
import models.Token;
import models.View;

import java.util.ArrayList;
import java.util.Objects;

public class ViewController {
    private final ArrayList<View> viewList = new ArrayList<>();
    private final Pane viewContainer;
    private final NavigationController navigationController;
    public ViewController(Pane rootContainer, NavigationController navigationController, View...views) {
        this.viewContainer = rootContainer;
        this.navigationController = navigationController;

        Token USER_TOKEN_STATE = new Token();

        for (View view : views) {
            view.getController().setViewController(this);
            view.getController().setTokenState(USER_TOKEN_STATE);
            viewList.add(view);
        }
    }

    public void changeView(String viewID) {
        for (View view : viewList) {
            if (Objects.equals(view.getViewNode().getId(), viewID)) {
                viewContainer.getChildren().clear();
                viewContainer.getChildren().add(view.getViewNode());

                view.getController().onMounted();

                navigationController.changeActiveNavigation(view.getViewNavigationLinkID());
                break;
            }
        }
    }
}
