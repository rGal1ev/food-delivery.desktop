package models.data;

public class Food {
    private int id;
    private String title;
    private String description;
    private double price;
    private boolean inCart = false;
    private String image_url;

    public Food(int id, String title, String description, double price, String image_url) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.image_url = image_url;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public boolean inCart() {
        return inCart;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setInCart(boolean inCart) {
        this.inCart = inCart;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
