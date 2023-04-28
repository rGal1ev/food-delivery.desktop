package models.data;

import java.util.ArrayList;

public class Order {
    private ArrayList<Food> foodList;
    private String phone;
    private String email;
    private String name;
    private String deliveryAddress;
    private int apartmentEntrance;
    private int apartmentNumber;
    private String paymentType;

    public Order(ArrayList<Food> foodList, String phone, String email, String name, String deliveryAddress, int apartmentEntrance, int apartmentNumber, String paymentType) {
        this.foodList = foodList;
        this.phone = phone;
        this.email = email;
        this.name = name;
        this.deliveryAddress = deliveryAddress;
        this.apartmentEntrance = apartmentEntrance;
        this.apartmentNumber = apartmentNumber;
        this.paymentType = paymentType;
    }

    public void setFoodList(ArrayList<Food> foodList) {
        this.foodList = foodList;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public void setApartmentEntrance(int apartmentEntrance) {
        this.apartmentEntrance = apartmentEntrance;
    }

    public void setApartmentNumber(int apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public ArrayList<Food> getFoodList() {
        return foodList;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public int getApartmentEntrance() {
        return apartmentEntrance;
    }

    public int getApartmentNumber() {
        return apartmentNumber;
    }

    public String getPaymentType() {
        return paymentType;
    }
}
