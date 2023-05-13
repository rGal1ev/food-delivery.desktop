package controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import models.Controller;
import models.data.AuthResponse;
import models.data.Order;
import models.data.User;
import network.NetworkService;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class ProfileController extends Controller {
    @FXML private TextField loginField;
    @FXML private TextField passwordField;
    @FXML private Label errorMessageText;

    @FXML private Pane loginContainer;
    @FXML private Pane profileContainer;

    @FXML private Label userNameText;
    @FXML private Label userPhoneText;
    @FXML private Label userEmailText;
    @FXML private Label orderNotFinishedCountText;

    @FXML private Button logoutButton;
    @FXML private Button openOrdersView;
    @FXML private Button listenNewOrders;

    Timeline newOrdersWatchers;
    User currentUser = null;
    List<Order> notFinishedOrders = new ArrayList<>();

    @Override
    public void onMounted() {
        rerenderProfileLoginView();

        errorMessageText.setVisible(false);

        logoutButton.setOnAction(event -> {
            logOutUser();
            rerenderProfileLoginView();
        });

        openOrdersView.setOnAction(event -> {
            viewController.changeView("ordersView");
        });

        if (!getToken().isEmpty()) {
            updateOrdersWatcher();
        }
    }

    public void updateOrdersWatcher() {
        if (newOrdersWatchers == null) {
            newOrdersWatchers = new Timeline();
            newOrdersWatchers.setCycleCount(Animation.INDEFINITE);

            newOrdersWatchers.getKeyFrames().add(new KeyFrame(Duration.millis(5000), actionEvent -> fetchNewOrders()));

            newOrdersWatchers.play();
        }
    }

    private void fetchCurrentUser() {
        NetworkService
                .getInstance()
                .getRoutes()
                .getEmployeeInfo(getToken())
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        currentUser = response.body();
                        Platform.runLater(() -> renderUserInfo());
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable throwable) {}
                });
    }

    private void renderUserInfo() {
        updateOrdersWatcher();

        if (currentUser != null) {
            userNameText.setText(currentUser.getFirstName() + " " + currentUser.getLastName());

            if (currentUser.getEmail() != null) {
                userEmailText.setText(currentUser.getEmail());
            } else {
                userEmailText.setText("Не указано");
            }

            userPhoneText.setText(currentUser.getPhone());
        }
    }

    public void fetchNewOrders() {
        notFinishedOrders.clear();

        NetworkService
                .getInstance()
                .getRoutes()
                .getOrders(getToken())
                .enqueue(new Callback<List<Order>>() {
                    @Override
                    public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                        for (Order order : response.body()) {
                            if (!order.isFinished()) {
                                notFinishedOrders.add(order);
                            }
                        }

                        Platform.runLater(() -> {
                            if (notFinishedOrders.size() != 0) {
                                orderNotFinishedCountText.setText(String.valueOf(notFinishedOrders.size()));
                            } else {
                                orderNotFinishedCountText.setText("Нету");
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<List<Order>> call, Throwable throwable) {}
                });
    }

    private void rerenderProfileLoginView() {
        if (getToken().isEmpty()) {
            loginContainer.setVisible(true);
            profileContainer.setVisible(false);
        } else {
            loginContainer.setVisible(false);
            profileContainer.setVisible(true);

            fetchCurrentUser();
        }
    }

    private void logOutUser() {
        setToken("");
        currentUser = null;

        newOrdersWatchers.getKeyFrames().clear();
        newOrdersWatchers.stop();
        newOrdersWatchers = null;
    }

    public void onAuthButtonClick(ActionEvent event) {
        String login = loginField.getText();
        String password = passwordField.getText();

        if (!login.isEmpty() || !password.isEmpty()) {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("login", login)
                    .addFormDataPart("password", password)
                    .build();

            NetworkService
                    .getInstance()
                    .getRoutes()
                    .authEmployee(requestBody)
                    .enqueue(new Callback<>() {
                        @Override
                        public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                            String USER_TOKEN = response.body().getTOKEN();

                            if (USER_TOKEN != null) {
                                setToken(USER_TOKEN);
                                Platform.runLater(() -> {
                                    rerenderProfileLoginView();
                                    errorMessageText.setVisible(false);

                                    loginField.setText("");
                                    passwordField.setText("");
                                });

                            } else {
                                String message = response.body().getMessage();
                                Platform.runLater(() -> {
                                    errorMessageText.setVisible(true);
                                    errorMessageText.setText(message);
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<AuthResponse> call, Throwable throwable) {
                            //...
                        }
                    });

        } else {
            errorMessageText.setVisible(true);
            errorMessageText.setText("Заполните все поля для входа");
        }
    }

}
