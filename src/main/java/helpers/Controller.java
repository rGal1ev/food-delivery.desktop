package helpers;

import models.Cart;

public class Controller {
    public Cart cart;
    public ViewsController viewsController;
    public void setState(Cart entryCart) {
        cart = entryCart;
    }

    public void setViewController(ViewsController entryViewController) {
        viewsController = entryViewController;
    }
}
