package controllers;

import models.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import models.FoodAndNode;
import models.data.Food;

import java.util.ArrayList;

public class CatalogController extends Controller {
    public Pane catalogView;
    public FlowPane foodListContainer;
    public ArrayList<FoodAndNode> foodCardList = new ArrayList<>();
    public ObservableList<Food> foodList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        Food firstFood = new Food(1,
                "Классический роллы Дневная Филладельфия",
                "Описание еды, довольно длинное, почему бы и нет",
                165.0,
                "https://sun9-75.userapi.com/impg/XhwtldvYdmaah2Dl_pXx-t3KoUyqHfJkUEhWdA/qji9mOFxndc.jpg?size=1400x933&quality=96&sign=2788006e4dd8f934e265c68a22a9b80d&c_uniq_tag=gGHWQgG9R2Mh5f1fo1ex3SJdeNL7m32WqNfCjFnSNJI&type=album");

        Food secondFood = new Food(2,
                "Классический роллы с горбушей и нори",
                "Описание еды, довольно длинное, почему бы и нет",
                165.0,
                "https://i.pinimg.com/originals/8b/1f/5c/8b1f5c294dd7bb0ca6fb6533d008c8a8.jpg");

        foodList.add(firstFood);
        foodList.add(secondFood);

        renderFoodList(foodList);
    }

    private void renderFoodList(ObservableList<Food> foodArrayList) {
        foodListContainer.getChildren().clear();

        for (Food food : foodArrayList) {
            FlowPane foodCard = generateFoodCard(food);
            foodListContainer.getChildren().add(foodCard);
        }
    }

    private FlowPane generateFoodCard(Food food) {
        FlowPane foodCardContainer = new FlowPane();
        foodCardContainer.getStyleClass().add("food-card");
        foodCardContainer.setPrefWidth(175);
        foodCardContainer.setPrefHeight(220);
        foodCardContainer.setAlignment(Pos.TOP_CENTER);
        foodCardContainer.setVgap(7);

        Image foodImage = new Image(food.getImage_url());
        ImageView foodImageView = new ImageView(foodImage);

        foodImageView.setFitWidth(160);
        foodImageView.setFitHeight(117);

        Label foodName = new Label();
        foodName.setText(food.getTitle());
        foodName.setWrapText(true);
        foodName.setPrefWidth(160);
        foodName.setPrefHeight(36);

        Button foodCardAction = new Button();

        if (food.inCart()) {
            foodCardAction.setText("Удалить из корзины");
            foodCardAction.setPrefWidth(160);
            foodCardAction.setPrefHeight(36);

            foodCardContainer.getChildren().addAll(foodImageView, foodName, foodCardAction);
            FoodAndNode foodAndNode = new FoodAndNode(food, foodCardContainer);

            foodCardList.add(foodAndNode);

            foodCardAction.setOnAction(e -> deleteFromCart(foodAndNode));

        } else {
            foodCardAction.setText("Добавить в корзину");
            foodCardAction.setPrefWidth(160);
            foodCardAction.setPrefHeight(36);

            foodCardContainer.getChildren().addAll(foodImageView, foodName, foodCardAction);
            FoodAndNode foodAndNode = new FoodAndNode(food, foodCardContainer);

            foodCardList.add(foodAndNode);

            foodCardAction.setOnAction(e -> sendToCart(foodAndNode));
        }

        return foodCardContainer;
    }

    public void sendToCart(FoodAndNode sendingFood) {
        boolean isFoodInCart = false;

        for (Food foodInCart : this.cart.foodList) {
            if (foodInCart == sendingFood.food) {
                isFoodInCart = true;
                break;
            }
        }

        if (!isFoodInCart) {
            Button foodCardAction = new Button();
            foodCardAction.setText("Удалить из корзины");
            foodCardAction.setPrefWidth(160);
            foodCardAction.setPrefHeight(36);

            sendingFood.foodCard.getChildren().remove(2);
            sendingFood.foodCard.getChildren().add(foodCardAction);

            foodCardAction.setOnAction(e -> deleteFromCart(sendingFood));

            this.cart.foodList.add(sendingFood.food);
            sendingFood.food.setInCart(true);
        }
    }

    public void deleteFromCart(FoodAndNode sendingFood) {
        boolean isFoodInCart = false;

        for (Food foodInCart : this.cart.foodList) {
            if (foodInCart == sendingFood.food) {
                isFoodInCart = true;
                break;
            }
        }

        if (isFoodInCart) {
            this.cart.foodList.remove(sendingFood.food);
            sendingFood.food.setInCart(false);

            Button foodCardAction = new Button();
            foodCardAction.setText("Добавить в корзину");
            foodCardAction.setPrefWidth(160);
            foodCardAction.setPrefHeight(36);

            sendingFood.foodCard.getChildren().remove(2);
            sendingFood.foodCard.getChildren().add(foodCardAction);

            foodCardAction.setOnAction(e -> sendToCart(sendingFood));
        }
    }
}
