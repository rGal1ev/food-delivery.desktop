package controllers;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import models.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import models.data.Food;

public class CartController extends Controller {
    public Button openCatalogButton;

    @FXML
    private Pane emptyCart;

    @FXML
    private FlowPane foodCartList;

    @FXML
    private Pane cartInfo;

    @FXML
    private Pane clearCartContainer;

    @Override
    public void onMounted() {
        System.out.println("Cart view is mounted");
        rerenderCartList();
    }

    @FXML
    private void initialize() {
        if (this.cart == null) {
            setCartIsEmpty();
        } else {
            setCartListVisible();
        }
    }

    public void onOpenCatalogButton() {
        this.viewController.changeView("catalogView");
    }

    private FlowPane generateCartCard(Food food) {
        FlowPane foodCartCard = new FlowPane();
        foodCartCard.setPrefHeight(60);
        foodCartCard.setPrefWidth(400);
        foodCartCard.setAlignment(Pos.CENTER_LEFT);
        foodCartCard.setHgap(23);
        foodCartCard.getStyleClass().add("cart-item");
        
        Image foodCartCardImage = new Image(food.getImage_url(), true);
        ImageView foodCartCardImageView = new ImageView(foodCartCardImage);

        foodCartCardImageView.setFitWidth(97);
        foodCartCardImageView.setFitHeight(53);
        foodCartCardImageView.setPreserveRatio(false);

        VBox foodCartCardDescription = new VBox();
        foodCartCardDescription.setAlignment(Pos.TOP_LEFT);
        foodCartCardDescription.setSpacing(5);
        foodCartCardDescription.setPrefWidth(151);
        foodCartCardDescription.setPrefHeight(53);

        Label foodCartCardTitle = new Label();
        foodCartCardTitle.setText(food.getTitle());
        foodCartCardTitle.setWrapText(true);

        Label foodCartCardPrice = new Label();
        foodCartCardPrice.setText("Цена: " + food.getPrice() + "руб.");
        foodCartCardPrice.setWrapText(true);

        foodCartCardDescription.getChildren().addAll(foodCartCardTitle, foodCartCardPrice);

        Button foodCartCardDeleteFromCartButton = new Button();
        foodCartCardDeleteFromCartButton.setText("Убрать");
        foodCartCardDeleteFromCartButton.setPrefHeight(32);
        foodCartCardDeleteFromCartButton.setPrefWidth(73);

        foodCartCardDeleteFromCartButton.setOnAction(e -> deleteFoodFromCart(food));

        foodCartCard.getChildren().addAll(foodCartCardImageView, foodCartCardDescription, foodCartCardDeleteFromCartButton);

        return foodCartCard;
    }

    public void rerenderCartList() {
        if (this.cart.foodList.size() != 0) {
            foodCartList.getChildren().clear();

            for (Food food : this.cart.foodList) {
                FlowPane foodCartCard = generateCartCard(food);
                foodCartList.getChildren().add(foodCartCard);
            }
            setCartListVisible();

        } else {
            setCartIsEmpty();
        }
    }

    public void deleteFoodFromCart(Food foodToDeleteFromCart) {
        try {
            this.cart.foodList.remove(foodToDeleteFromCart);
            foodToDeleteFromCart.setInCart(false);

            rerenderCatalogList();

        } catch (Exception ignored) {}
    }

    public void setCartIsEmpty() {
        emptyCart.setVisible(true);

        foodCartList.setVisible(false);
        cartInfo.setVisible(false);
        clearCartContainer.setVisible(false);
    }

    public void setCartListVisible() {
        emptyCart.setVisible(false);

        foodCartList.setVisible(true);
        cartInfo.setVisible(true);
        clearCartContainer.setVisible(true);
    }

    public void onClearCartButtonClick() {
        for (Food food : this.cart.foodList) {
            food.setInCart(false);
        }

        if (this.cart != null) {
            if (this.cart.foodList.size() != 0) {
                this.cart.foodList.clear();
            }
        }

        rerenderCartList();
        rerenderCatalogList();
    }

    public void rerenderCatalogList() {
        CatalogController catalogController = (CatalogController) this.viewController.getView("catalogView").getController();
        catalogController.renderFoodList();
    }
}
