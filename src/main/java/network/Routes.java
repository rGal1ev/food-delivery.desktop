package network;

import models.data.Food;
import models.data.AuthResponse;
import models.data.Order;
import models.data.User;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface Routes {
    @GET("/get/food")
    public Call<List<Food>> getFoodList();

    @POST("/auth/employee")
    public Call<AuthResponse> authEmployee(@Body RequestBody body);

    @GET("/get/order")
    public Call<List<Order>> getOrders(@Header("x-access-token") String USER_TOKEN);

    @GET("/get/user/current")
    public Call<User> getEmployeeInfo(@Header("x-access-token") String USER_TOKEN);

    @GET("/set/finished/order/{orderID}")
    public Call<AuthResponse> setOrderIsFinished(@Header("x-access-token") String USER_TOKEN, @Path("orderID") int orderID);
}
