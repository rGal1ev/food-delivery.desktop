package models.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("is_finished")
    @Expose
    private boolean isFinished;

    @SerializedName("cart")
    @Expose
    private List<Food> cart;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("firstname")
    @Expose
    private String firstName;

    @SerializedName("delivery_address")
    @Expose
    private String deliveryAddress;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public void setCart(List<Food> cart) {
        this.cart = cart;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public int getId() {
        return id;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public List<Food> getCart() {
        return cart;
    }

    public String getPhone() {
        return phone;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }
}
