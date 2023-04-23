package controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;
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
import models.View;
import models.data.Food;
import java.util.Arrays;

public class CatalogController extends Controller {
    public Pane catalogView;
    public FlowPane foodListContainer;
    public ObservableList<Food> foodList = FXCollections.observableArrayList();
    @FXML
    private Pane CartMessagePane;
    @FXML
    private void initialize() {
        CartMessagePane.setVisible(false);

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

        Food thirdFood = new Food(3,
                "Классический роллы с горбушей и нори",
                "Описание еды, довольно длинное, почему бы и нет",
                165.0,
                "https://i.pinimg.com/originals/8b/1f/5c/8b1f5c294dd7bb0ca6fb6533d008c8a8.jpg");

        Food fourthFood = new Food(4,
                "Классический роллы с горбушей и нори",
                "Описание еды, довольно длинное, почему бы и нет",
                165.0,
                "https://i.pinimg.com/originals/8b/1f/5c/8b1f5c294dd7bb0ca6fb6533d008c8a8.jpg");

        Food fivthFood = new Food(5,
                "Классический роллы с горбушей и нори",
                "Описание еды, довольно длинное, почему бы и нет",
                165.0,
                "https://i.pinimg.com/originals/8b/1f/5c/8b1f5c294dd7bb0ca6fb6533d008c8a8.jpg");

        Food sixthFood = new Food(6,
                "Классический роллы с горбушей и нори",
                "Описание еды, довольно длинное, почему бы и нет",
                165.0,
                "https://i.pinimg.com/originals/8b/1f/5c/8b1f5c294dd7bb0ca6fb6533d008c8a8.jpg");

        foodList.addAll(Arrays.asList(firstFood, secondFood, thirdFood, fourthFood, fivthFood, sixthFood));

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
        FlowPane foodCardNode = new FlowPane();
        foodCardNode.getStyleClass().add("food-card");
        foodCardNode.setPrefWidth(175);
        foodCardNode.setPrefHeight(220);
        foodCardNode.setAlignment(Pos.TOP_CENTER);
        foodCardNode.setVgap(7);
        foodCardNode.setUserData(food.getId());

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

            foodCardAction.setOnAction(e -> deleteFromCart(food));

        } else {
            foodCardAction.setText("Добавить в корзину");
            foodCardAction.setPrefWidth(160);
            foodCardAction.setPrefHeight(36);

            foodCardAction.setOnAction(e -> sendToCart(food));
        }

        foodCardNode.getChildren().addAll(foodImageView, foodName, foodCardAction);
        return foodCardNode;
    }

    public void sendToCart(Food foodToCart) {
        boolean isFoodInCart = false;

        for (Food foodInCart : this.cart.foodList) {
            if (foodInCart == foodToCart) {
                isFoodInCart = true;
                break;
            }
        }

        if (!isFoodInCart) {
            foodToCart.setInCart(false);
            this.cart.foodList.add(foodToCart);

            for (Node child : foodListContainer.getChildren()) {
                FlowPane foodCardNode = (FlowPane) child;
                Integer foodCardNodeID = (Integer) foodCardNode.getUserData();

                if (foodCardNodeID == foodToCart.getId()) {
                    Button foodCardAction = new Button();
                    foodCardAction.setText("Удалить из корзины");
                    foodCardAction.setPrefWidth(160);
                    foodCardAction.setPrefHeight(36);

                    foodCardAction.setOnAction(e -> deleteFromCart(foodToCart));

                    foodCardNode.getChildren().remove(2);
                    foodCardNode.getChildren().add(foodCardAction);
                }

            }

            CartMessagePane.setVisible(true);

            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.millis(5 * 1000),
                    afterEnd -> CartMessagePane.setVisible(false)
            ));

            timeline.play();
        }

        callCartViewRerender();
    }

    public void deleteFromCart(Food foodToDeleteFromCart) {
        try {
            this.cart.foodList.remove(foodToDeleteFromCart);

            foodToDeleteFromCart.setInCart(false);

            for (Node child : foodListContainer.getChildren()) {
                FlowPane foodCardNode = (FlowPane) child;
                Integer foodCardNodeID = (Integer) foodCardNode.getUserData();

                if (foodCardNodeID == foodToDeleteFromCart.getId()) {
                    Button foodCardAction = new Button();
                    foodCardAction.setText("Добавить в корзину");
                    foodCardAction.setPrefWidth(160);
                    foodCardAction.setPrefHeight(36);

                    foodCardAction.setOnAction(e -> sendToCart(foodToDeleteFromCart));

                    foodCardNode.getChildren().remove(2);
                    foodCardNode.getChildren().add(foodCardAction);
                }

            }

        } catch (Exception ignored) {}

        callCartViewRerender();
    }

    public void onOpenCartViewButtonClick() {
        this.viewController.changeView("cartView");
    }

    public void callCartViewRerender() {
        View cartView = this.viewController.getView("cartView");
        CartController cartViewController = (CartController) cartView.getController();
        cartViewController.rerenderCartList();
    }
}
