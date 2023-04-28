package controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import main.DataBase;
import main.Utils;
import models.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import models.data.Food;
import models.data.Order;

import java.sql.SQLException;

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
    @FXML
    private ChoiceBox<String> paymentTypeChoice;
    @FXML
    private Pane orderContainer;
    @FXML
    private Pane orderInfoContainer;

    @Override
    public void onMounted() {
        rerenderCartList();
    }

    @FXML
    private void initialize() {
        if (this.cart == null) {
            setCartIsEmpty();
        } else {
            setCartListVisible();
        }

        orderContainer.setVisible(false);

        ObservableList<String> paymentTypeList = FXCollections.observableArrayList("Наличными после доставки", "Картой после доставки");
        paymentTypeChoice.setItems(paymentTypeList);
        paymentTypeChoice.setValue("Наличными после доставки");
    }

    public void onOpenOrderMakeButtonClick(ActionEvent event) {
        orderContainer.setVisible(true);
    }

    public void onCloseOrderCOntainerButtonClick(ActionEvent event) {
        orderContainer.setVisible(false);
    }

    public void onOpenCatalogButton() throws SQLException {
        this.viewController.changeView("catalogView");
    }

    public boolean onSendOrderButtonClick() throws SQLException {
        TextField phoneNumberTF = (TextField) Utils.findByID(orderContainer, "orderPhone");
        TextField orderEmailTF = (TextField) Utils.findByID(orderContainer, "orderEmail");
        TextField orderUserNameTF = (TextField) Utils.findByID(orderContainer, "orderUserName");
        TextField orderDeliveryAddressTF = (TextField) Utils.findByID(orderContainer, "orderDeliveryAddress");
        TextField orderApartmentEntranceTF = (TextField) Utils.findByID(orderContainer, "orderApartmentEntrance");
        TextField orderApartmentNumberTF = (TextField) Utils.findByID(orderContainer, "orderApartmentNumber");

        if (!validateFields(phoneNumberTF, orderEmailTF, orderUserNameTF, orderDeliveryAddressTF, orderApartmentEntranceTF, orderApartmentNumberTF)) {
            return false;
        }

        String phoneNumber = phoneNumberTF.getText();
        String orderEmail = orderEmailTF.getText();
        String orderUserName = orderUserNameTF.getText();
        String orderDeliveryAddress = orderDeliveryAddressTF.getText();
        int orderApartmentEntrance = Integer.valueOf(orderApartmentEntranceTF.getText());
        int orderApartmentNumber = Integer.valueOf(orderApartmentNumberTF.getText());
        String paymentTypeChoiceValue = paymentTypeChoice.getValue();

        System.out.println(phoneNumber + " " + orderEmail + " " + orderUserName + " " + orderDeliveryAddress + " " + orderApartmentEntrance + " " + orderApartmentNumber);

        Order sendingOrder = new Order(this.cart.foodList,
                                       phoneNumber,
                                       orderEmail,
                                       orderUserName,
                                       orderDeliveryAddress,
                                       orderApartmentEntrance,
                                       orderApartmentNumber,
                                       paymentTypeChoiceValue);

        boolean resultIsSuccessfully = DataBase.sendOrder(sendingOrder);

        if (resultIsSuccessfully) {
            phoneNumberTF.setText("");
            orderEmailTF.setText("");
            orderUserNameTF.setText("");
            orderDeliveryAddressTF.setText("");
            orderApartmentEntranceTF.setText("");
            orderApartmentNumberTF.setText("");

            orderContainer.setVisible(false);

            orderInfoContainer.setVisible(true);

            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.millis(5 * 1000),
                    afterEnd -> orderInfoContainer.setVisible(false)
            ));

            timeline.play();

            this.cart.foodList.clear();
            rerenderCatalogList();
            rerenderCartList();
        } else {
            System.out.println("Неуспешно!");
        }

        return false;
    }

    private boolean validateFields(TextField ...fields) {
        String nonPassedValidationField = "-fx-border-color: red;" +
                "-fx-border-radius: 4px;";
        String passedValidationField = "-fx-border-color: #cecece;" +
                "-fx-border-radius: 4px;";

        for (TextField field : fields) {
            if (field.getText().isEmpty()) {
                field.setStyle(nonPassedValidationField);
                return false;
            } else {
                field.setStyle(passedValidationField);
            }
        }

        return true;
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

            double totalFoodPrice = 0;
            int totalFoodCount = 0;

            for (Food food : this.cart.foodList) {
                totalFoodCount++;
                totalFoodPrice = totalFoodPrice + food.getPrice();

                FlowPane foodCartCard = generateCartCard(food);
                foodCartList.getChildren().add(foodCartCard);
            }

            Label cartFoodCountInfo = (Label) Utils.findByID(cartInfo, "cartFoodCountInfo");
            Label cartFoodTotalPrice = (Label) Utils.findByID(cartInfo, "cartFoodTotalPrice");

            cartFoodCountInfo.setText("Всего товаров: " + totalFoodCount);
            cartFoodTotalPrice.setText("Общая цена: " + totalFoodPrice);

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

        rerenderCartList();
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
