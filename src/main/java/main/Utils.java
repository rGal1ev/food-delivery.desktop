package main;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.Objects;

public class Utils {
    public static Node findByID(Pane rootContainer, String nodeID) {
        for (Node child : rootContainer.getChildren()) {
            if (Objects.equals(child.getId(), nodeID)) {
                return child;
            }
        }

        return null;
    }
}
