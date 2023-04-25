package controllers;

import models.Controller;

public class ProfileController extends Controller {
    @Override
    public void onMounted() {
        System.out.println("Profile view is mounted");
    }
}
