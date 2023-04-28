package main;

import models.data.Food;
import models.data.Order;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class DataBase {
    private static String DATABASE_URL = Constants.DATABASE_URL;
    private static String DATABASE_USER = Constants.DATABASE_USER;
    private static String DATABASE_PASSWORD = Constants.DATABASE_PASSWORD;

    public DataBase() {}

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
    }

    public static ArrayList<Food> getFoods() throws SQLException {
        String SQLQuery = "SELECT * FROM Foods";
        ArrayList<Food> foodList = new ArrayList<>();

        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQLQuery)) {

            while (resultSet.next()) {
                Food food = new Food(resultSet.getInt("id"),
                                     resultSet.getString("title"),
                                     resultSet.getString("description"),
                                     resultSet.getDouble("price"),
                                     resultSet.getString("imageurl"));

                foodList.add(food);
            }

            return foodList;


        } catch (SQLException SQLError) {
            System.out.println(SQLError.getMessage());
        }

        return null;
    }

    public static boolean sendOrder(Order order) throws SQLException {
        String OrderSQLQuery = "INSERT INTO Orders (OrderPublicId, Name, Phone, Email, deliveryAddress, apartmentEntrance, apartmentNumber, paymentType) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String CartSQLQuery = "INSERT INTO OrderCarts (OrderPublicId, FoodID) VALUES (?, ?)";

        String localDate = String.valueOf(LocalDate.now());
        String randomDigits = String.valueOf(Math.random()).substring(2, 5);
        String publicOrderID = localDate + "-" + randomDigits;

        try  {
            Connection connection = connect();
            PreparedStatement orderPreparedStatement = connection.prepareStatement(OrderSQLQuery);
            PreparedStatement cartPreparedStatement = connection.prepareStatement(CartSQLQuery);

            orderPreparedStatement.setString(1, publicOrderID);
            orderPreparedStatement.setString(2, order.getName());
            orderPreparedStatement.setString(3, order.getPhone());
            orderPreparedStatement.setString(4, order.getEmail());
            orderPreparedStatement.setString(5, order.getDeliveryAddress());
            orderPreparedStatement.setInt(6, order.getApartmentEntrance());
            orderPreparedStatement.setInt(7, order.getApartmentNumber());
            orderPreparedStatement.setString(8, order.getPaymentType());

            orderPreparedStatement.executeUpdate();

            for (Food food : order.getFoodList()) {
                cartPreparedStatement.setString(1, publicOrderID);
                cartPreparedStatement.setInt(2, food.getId());

                cartPreparedStatement.executeUpdate();
            }

            return true;

        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }

        return false;
    }
}
