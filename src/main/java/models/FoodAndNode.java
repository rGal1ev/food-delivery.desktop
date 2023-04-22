package models;

import javafx.scene.layout.Pane;
import models.data.Food;

public class FoodAndNode {
    public Food food;
    public Pane foodCard;

    public FoodAndNode(Food food, Pane foodCard) {
        this.food = food;
        this.foodCard = foodCard;
    }
}
