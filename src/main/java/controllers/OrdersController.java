package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import models.Controller;
import models.data.AuthResponse;
import models.data.Food;
import models.data.Order;
import network.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class OrdersController extends Controller {
    @FXML private Pane nonAuthMessage;
    @FXML private Pane ordersPanel;
    @FXML private VBox notFinishedOrdersContainer;
    @FXML private VBox ordersContainer;

    @FXML Pane emptyNewOrdersMessage;
    @FXML Pane orderFoodCard;

    @FXML Label orderCardID;
    @FXML Label orderCardUserName;
    @FXML Label orderCardDeliveryAddress;
    @FXML Label orderCardUserPhone;
    @FXML Label orderCardTotalPrice;

    @FXML VBox orderCartContainer;
    @FXML Button closeOrderCardContainer;

    @Override
    public void onMounted() {
        if (getToken().isEmpty()) {
            nonAuthMessage.setVisible(true);
            ordersPanel.setVisible(false);

        } else {
            nonAuthMessage.setVisible(false);
            ordersPanel.setVisible(true);

            fetchOrders();
        }
    }

    public void onUpdateButtonClick() {
        fetchOrders();
    }

    public void fetchOrders() {
        NetworkService
                .getInstance()
                .getRoutes()
                .getOrders(getToken())
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                        List<Order> notFinishedOrderList = new ArrayList<>();
                        List<Order> finishedOrderList = new ArrayList<>();

                        for (Order order : response.body()) {
                            if (!order.isFinished()) {
                                notFinishedOrderList.add(order);
                            } else {
                                finishedOrderList.add(order);
                            }
                        }

                        Platform.runLater(() -> {
                            renderNotFinishedOrderList(notFinishedOrderList);
                            renderFinishedOrderList(finishedOrderList);
                        });
                    }

                    @Override
                    public void onFailure(Call<List<Order>> call, Throwable throwable) {

                    }
                });
    }

    public void renderFinishedOrderList(List<Order> orderList) {
        ordersContainer.getChildren().clear();

        if (orderList.size() != 0) {
            for (Order order : orderList) {
                HBox orderCard = generateOrder(order);
                ordersContainer.getChildren().add(orderCard);
            }
        }
    }

    public void renderNotFinishedOrderList(List<Order> orderList) {
        notFinishedOrdersContainer.getChildren().clear();

        if (orderList.size() == 0) {
            emptyNewOrdersMessage.setVisible(true);
        } else {
            emptyNewOrdersMessage.setVisible(false);

            for (Order order : orderList) {
                HBox orderCard = generateNotFinishedOrder(order);
                notFinishedOrdersContainer.getChildren().add(orderCard);
            }
        }
    }

    public HBox generateOrder(Order order) {
        HBox orderContainer = new HBox();
        orderContainer.setSpacing(15);
        orderContainer.setPrefHeight(74);
        orderContainer.setPrefWidth(Region.USE_COMPUTED_SIZE);
        orderContainer.setPadding(new Insets(0, 0, 0, 10));
        orderContainer.setAlignment(Pos.CENTER_LEFT);
        orderContainer.getStyleClass().add("order-card");

        VBox orderInfoContainer = new VBox();
        orderInfoContainer.setPrefWidth(230);
        orderInfoContainer.setPadding(new Insets(10, 0, 0, 0));

        Label orderID = new Label();
        orderID.setText("Заказ № " + order.getId());

        Label orderUserName = new Label();
        orderUserName.setText("Заказчик: " + order.getFirstName());

        Label orderDeliveryAdress = new Label();
        orderDeliveryAdress.setText("Адрес доставки: " + order.getDeliveryAddress());

        orderInfoContainer.getChildren().addAll(orderID, orderUserName, orderDeliveryAdress);

        orderContainer.setOnMouseClicked(event -> {
            orderFoodCard.setVisible(true);
            updateOrderFoodCardInfo(order);
        });

        orderContainer.getChildren().addAll(orderInfoContainer);
        return orderContainer;
    }

    public HBox generateNotFinishedOrder(Order order) {
        HBox orderContainer = new HBox();
        orderContainer.setSpacing(15);
        orderContainer.setPrefHeight(74);
        orderContainer.setPrefWidth(Region.USE_COMPUTED_SIZE);
        orderContainer.setPadding(new Insets(0, 0, 0, 10));
        orderContainer.setAlignment(Pos.CENTER_LEFT);
        orderContainer.getStyleClass().add("order-card");

        VBox orderInfoContainer = new VBox();
        orderInfoContainer.setPrefWidth(230);
        orderInfoContainer.setPadding(new Insets(10, 0, 0, 0));

        Label orderID = new Label();
        orderID.setText("Заказ № " + order.getId());

        Label orderUserName = new Label();
        orderUserName.setText("Заказчик: " + order.getFirstName());

        Label orderDeliveryAdress = new Label();
        orderDeliveryAdress.setText("Адрес доставки: " + order.getDeliveryAddress());

        orderInfoContainer.getChildren().addAll(orderID, orderUserName, orderDeliveryAdress);

        Button finishOrder = new Button();
        finishOrder.setPrefWidth(100);
        finishOrder.setPrefHeight(33);
        finishOrder.setText("Завершить");

        orderContainer.setOnMouseClicked(event -> {
            orderFoodCard.setVisible(true);
            updateOrderFoodCardInfo(order);
        });

        finishOrder.setOnAction(event -> {
            setOrderIsFinished(order);
        });

        orderContainer.getChildren().addAll(orderInfoContainer, finishOrder);
        return orderContainer;
    }

    private void updateOrderFoodCardInfo(Order order) {
        orderCardID.setText("Заказ № " + order.getId());

        orderCardUserName.setText("Заказчик: " + order.getFirstName());
        orderCardDeliveryAddress.setText("Адрес доставки: " + order.getDeliveryAddress());
        orderCardUserPhone.setText("Номер телефона: " + order.getPhone());

        double totalOrderPrice = 0;

        for (Food food : order.getCart()) {
            totalOrderPrice = totalOrderPrice + food.getPrice() * food.getCount();
        }

        orderCardTotalPrice.setText(Math.round(totalOrderPrice) + "₽");

        generateOrderFoodCart(order.getCart());

        closeOrderCardContainer.setOnAction(e -> {
            orderFoodCard.setVisible(false);
        });
    }

    private void generateOrderFoodCart(List<Food> foodList) {
        orderCartContainer.getChildren().clear();

        for (Food food : foodList) {
            HBox generatedFoodCard = generateFoodCard(food);
            orderCartContainer.getChildren().add(generatedFoodCard);
        }
    }

    private HBox generateFoodCard(Food food) {
        HBox foodCard = new HBox();
        foodCard.setSpacing(20);

        Image foodImage = new Image(food.getImageURL(), true);
        ImageView foodImageView = new ImageView(foodImage);
        foodImageView.setFitWidth(100);
        foodImageView.setFitHeight(100);

        VBox foodCardInfo = new VBox();
        foodCardInfo.setPrefHeight(56);
        foodCardInfo.setPadding(new Insets(5, 0, 0, 0));

        Label foodTitle = new Label(food.getTitle());
        foodTitle.getStyleClass().add("food-card-title");

        Label foodDescription = new Label(food.getDescription());
        foodDescription.setWrapText(true);

        Label foodCount = new Label("Количество: " + food.getCount());
        Label foodTotalPrice = new Label("Цена: " + Math.round(food.getPrice() * food.getCount()) + "₽");

        foodCardInfo.getChildren().addAll(foodTitle, foodDescription, foodCount, foodTotalPrice);

        foodCard.getChildren().addAll(foodImageView, foodCardInfo);

        return foodCard;
    }

    private void setOrderIsFinished(Order order) {
        NetworkService
                .getInstance()
                .getRoutes()
                .setOrderIsFinished(getToken(), order.getId())
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        fetchOrders();
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable throwable) {

                    }
                });
    }
}
