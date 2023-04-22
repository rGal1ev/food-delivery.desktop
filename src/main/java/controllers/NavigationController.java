package controllers;

import javafx.scene.layout.Pane;
import models.NavigationLink;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class NavigationController {
    private final ArrayList<NavigationLink> navigationsList = new ArrayList<>();
    public NavigationController(NavigationLink... navigationLinks) {
        navigationsList.addAll(Arrays.asList(navigationLinks));
    }
    public void changeActiveNavigation(String navigationLinkID) {
        for (NavigationLink navigationLink : navigationsList) {
            Pane navigationNode = navigationLink.getNavigationNode();

            if (Objects.equals(navigationLink.getNavigationNode().getId(), navigationLinkID)) {
                try {
                    navigationNode.getStyleClass().remove(1);
                } catch (Exception ignored) {}

                navigationNode.getStyleClass().add(1, "active");

            } else {
                try {
                    navigationNode.getStyleClass().remove(1);
                } catch (Exception ignored) {}
            }
        }
    }
}
