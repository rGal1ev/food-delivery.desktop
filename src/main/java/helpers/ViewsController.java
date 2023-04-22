package helpers;

import main.Loader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import models.Cart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ViewsController {
    private ArrayList<Node> viewList = new ArrayList<>();
    private Pane viewContainer = null;
    private String currentViewID = "";

    public ViewsController(Pane container, Cart cart, String ...EntryViewList) throws IOException {
        viewContainer = container;

        for (String viewPath : EntryViewList) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(viewPath));
            Pane cartView = fxmlLoader.load();
            Controller controller = fxmlLoader.getController();

            controller.setState(cart);
            controller.setViewController(this);

            viewList.add(cartView);
        }
    }

    public void changeView(String viewID) {
        for (Node node : viewList) {
            if (Objects.equals(node.getId(), viewID)) {
                viewContainer.getChildren().clear();
                viewContainer.getChildren().add(node);

                currentViewID = node.getId();
            }
        }
    }

    public void addView(String viewPath) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Loader.class.getResource(viewPath));;
        Pane cartView = fxmlLoader.load();

        viewList.add(cartView);
    }
}
