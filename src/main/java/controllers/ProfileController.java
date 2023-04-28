package controllers;

import javafx.event.ActionEvent;
import models.Controller;

public class ProfileController extends Controller {
    @Override
    public void onMounted() {

    }

    public void onAuthButtonClick(ActionEvent event) {
        System.out.println("Авторизация!");
    }

}
