package controllers;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;
import main.DataBase;
import models.Controller;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import models.data.Food;

import java.net.HttpURLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class CatalogController extends Controller {
    public Pane catalogView;
    public FlowPane foodListContainer;
    public ArrayList<Food> foodList = new ArrayList<>();
    private boolean isFirstMounting = true;

    @FXML
    private Pane CartMessagePane;
    @FXML
    private Pane foodInfoContainer;

    @Override
    public void onMounted() throws SQLException {
        if (isFirstMounting) {
            isFirstMounting = !isFirstMounting;

            ArrayList<Food> foodList = DataBase.getFoods();

            if (foodList == null || foodList.size() == 0) {
                System.out.println("База данных пуста");
            } else {
                this.foodList.addAll(foodList);
                renderFoodList();
            }
        }
    }

    @FXML
    private void initialize() {
        CartMessagePane.setVisible(false);
        foodListContainer.setVisible(true);
        foodInfoContainer.setVisible(false);
    }

    public void openFoodInfo(Food food, Image savedFoodImage) {
        foodInfoContainer.setVisible(true);

        Button closeFoodInfoButton = (Button) findByID(foodInfoContainer, "closeFoodInfoButton");

        ImageView foodInfoImageView = (ImageView) findByID(foodInfoContainer, "foodInfoImageView");
        foodInfoImageView.setImage(savedFoodImage);

        Label foodInfoTitle = (Label) findByID(foodInfoContainer, "foodInfoTitle");
        Label foodInfoDesc = (Label) findByID(foodInfoContainer, "foodInfoDesc");

        Label foodInfoPrice = (Label) findByID(foodInfoContainer, "foodInfoPrice");

        Button foodInfoActionButton = (Button) findByID(foodInfoContainer, "foodInfoActionButton");

        foodInfoTitle.setText(food.getTitle());
        foodInfoDesc.setText(food.getDescription());
        foodInfoPrice.setText(String.valueOf(food.getPrice()));

        if (food.getInCart() == true) {
            foodInfoActionButton.setOnAction(event -> deleteFromCart(food));
            foodInfoActionButton.setText("Удалить из корзины");
        } else {
            foodInfoActionButton.setOnAction(event -> sendToCart(food));
            foodInfoActionButton.setText("Добавить в корзину");
        }

        closeFoodInfoButton.setOnAction(event -> {
            FadeTransition showInfo = new FadeTransition();
            showInfo.setDuration(Duration.millis(500));
            showInfo.setFromValue(1);
            showInfo.setToValue(0);
            showInfo.setNode(foodInfoContainer);

            showInfo.play();

            showInfo.setOnFinished(e -> foodInfoContainer.setVisible(false));
        });

        FadeTransition showInfo = new FadeTransition();
        showInfo.setDuration(Duration.millis(500));
        showInfo.setFromValue(0);
        showInfo.setToValue(1);

        showInfo.setNode(foodInfoContainer);

        foodInfoContainer.setOpacity(0);

        showInfo.play();
    }

    private Node findByID(Pane rootContainer, String nodeID) {
        for (Node child : rootContainer.getChildren()) {
            if (Objects.equals(child.getId(), nodeID)) {
                return child;
            }
        }

        return null;
    }

    public void renderFoodList() {
        foodListContainer.getChildren().clear();

        for (Food food : this.foodList) {
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

        Image foodImage = new Image(food.getImage_url(), true);
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

        foodCardNode.setOnMouseClicked(event -> {
            openFoodInfo(food, foodImageView.getImage());
        });

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
            foodToCart.setInCart(true);
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
    }

    public void onOpenCartViewButtonClick() throws SQLException {
        this.viewController.changeView("cartView");
    }
}
