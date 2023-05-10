package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.Controller;
import models.data.AuthResponse;
import network.NetworkService;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileController extends Controller {
    @FXML private TextField loginField;
    @FXML private TextField passwordField;

    @Override
    public void onMounted() {}

    public void onAuthButtonClick(ActionEvent event) {
        String login = loginField.getText();
        String password = passwordField.getText();

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
                        } else {
                            String message = response.body().getMessage();
                            System.out.println(message);
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable throwable) {
                        //...
                    }
                });
    }

}
