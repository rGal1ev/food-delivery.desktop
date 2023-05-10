package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
    @FXML private VBox ordersContainer;

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

    public void fetchOrders() {


        NetworkService
                .getInstance()
                .getRoutes()
                .getOrders(getToken())
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                        List<Order> notFinishedOrderList = new ArrayList<>();

                        for (Order order : response.body()) {
                            if (!order.isFinished()) {
                                notFinishedOrderList.add(order);
                            }
                        }

                        Platform.runLater(() -> {
                            renderNotFinishedOrderList(notFinishedOrderList);
                        });
                    }

                    @Override
                    public void onFailure(Call<List<Order>> call, Throwable throwable) {

                    }
                });
    }

    public void renderNotFinishedOrderList(List<Order> orderList) {
        ordersContainer.getChildren().clear();

        for (Order order : orderList) {
            HBox orderCard = generateNotFinishedOrder(order);
            ordersContainer.getChildren().add(orderCard);
        }
    }

    public HBox generateNotFinishedOrder(Order order) {
        HBox orderContainer = new HBox();
        orderContainer.setSpacing(120);
        orderContainer.setPrefHeight(65);
        orderContainer.setPadding(new Insets(0, 0, 0, 10));
        orderContainer.setAlignment(Pos.CENTER_LEFT);

        VBox orderInfoContainer = new VBox();
        orderInfoContainer.prefWidth(200);
        orderInfoContainer.setPadding(new Insets(10, 0, 0, 0));

        Label orderID = new Label();
        orderID.setText("Заказ № " + order.getId());

        Label orderUserName = new Label();
        orderUserName.setText("Заказчик: " + order.getFirstName());

        Label orderDeliveryAdress = new Label();
        orderDeliveryAdress.setText("Адрес доставки: " + order.getDeliveryAddress());

        orderInfoContainer.getChildren().addAll(orderID, orderUserName, orderDeliveryAdress);

        Button finishOrder = new Button();
        finishOrder.setPrefWidth(129);
        finishOrder.setPrefWidth(33);
        finishOrder.setText("Завершить заказ");

        finishOrder.setOnAction(event -> {
            setOrderIsFinished(order);
        });

        orderContainer.getChildren().addAll(orderInfoContainer, finishOrder);

        return orderContainer;
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
