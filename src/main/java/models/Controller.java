package models;

import controllers.ViewController;

import java.io.IOException;

public class Controller {
    public ViewController viewController;
    private Token token;

    public void setViewController(ViewController viewController) {
        this.viewController = viewController;
    }

    public void onMounted() {}

    public void setTokenState(Token token) {
        this.token = token;
    }

    public String getToken() {
        return token.getBody();
    }

    public void setToken(String tokenString) {
        this.token.setBody(tokenString);
    }
}
