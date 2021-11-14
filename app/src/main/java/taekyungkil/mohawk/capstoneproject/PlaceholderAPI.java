package taekyungkil.mohawk.capstoneproject;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import taekyungkil.mohawk.capstoneproject.model.AvailableSeatRequest;
import taekyungkil.mohawk.capstoneproject.model.Restaurant;
import taekyungkil.mohawk.capstoneproject.model.User;

public interface PlaceholderAPI {

    @GET("restaurants")
    Call<List<Restaurant>> getRestaurants();

    @GET("restaurant/{id}")
    Call<Restaurant> getRestaurant(
      @Path("id") Integer id
    );

    @Headers("Content-Type: application/json")
    @PUT("availableSeat/{id}")
    Call<Restaurant> putRestaurant(
            @Body AvailableSeatRequest availableSeatRequest,
            @Path("id") Integer id


    );

    @POST("user")
    Call<User> postUser(
      @Body  User user
    );

    @Headers("Content-Type: application/json")
    @GET("user/{email}")
    Call<User> getUser(
      @Path("email") String email
    );

    @POST("restaurant")
    Call<Restaurant> postRestaurant(
            @Body  Restaurant restaurant
    );

    @Headers("Content-Type: application/json")
    @PUT("user/{email}")
    Call<User> putUserRestaurantId(
            @Body Integer restaurantId,
            @Path("email") String email


    );
}