package models;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class View {
    private final Controller controller;

    private final Pane viewNode;

    private final String viewNavigationLinkID;

    public View(String viewPath, String viewNavigationLinkID) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(viewPath));

        this.viewNode = loader.load();
        this.controller = loader.getController();
        this.viewNavigationLinkID = viewNavigationLinkID;
    }

    public Controller getController() {
        return controller;
    }

    public Pane getViewNode() {
        return viewNode;
    }

    public String getViewNavigationLinkID() {
        return viewNavigationLinkID;
    }
}
