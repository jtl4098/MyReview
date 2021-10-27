package taekyungkil.mohawk.capstoneproject;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import taekyungkil.mohawk.capstoneproject.model.AvailableSeatRequest;
import taekyungkil.mohawk.capstoneproject.model.Restaurant;

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

}