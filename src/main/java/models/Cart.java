package models;

import java.util.ArrayList;

public class Cart {
    public ArrayList<Food> foodList = new ArrayList<>();

    public Cart() {}

    public Cart(ArrayList<Food> foodList) {
        this.foodList = foodList;
    }
}
