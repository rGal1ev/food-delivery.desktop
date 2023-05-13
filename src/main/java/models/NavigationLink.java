package models;

import javafx.scene.layout.Pane;
public class NavigationLink {
    private final Pane navigationNode;

    public NavigationLink(Pane navigationNode) {
        this.navigationNode = navigationNode;
    }

    public Pane getNavigationNode() {
        return navigationNode;
    }
}
