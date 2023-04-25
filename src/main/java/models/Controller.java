package models;

import controllers.ViewController;
import models.data.Cart;

public class Controller {
    public Cart cart;

    public ViewController viewController;

    public void setCarState(Cart cart) {
        this.cart = cart;
    }

    public void setViewController(ViewController viewController) {
        this.viewController = viewController;
    }

    public void onMounted() {}
}
