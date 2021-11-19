package taekyungkil.mohawk.capstoneproject;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import taekyungkil.mohawk.capstoneproject.model.Appointment;
import taekyungkil.mohawk.capstoneproject.model.AvailableSeatRequest;
import taekyungkil.mohawk.capstoneproject.model.Restaurant;
import taekyungkil.mohawk.capstoneproject.model.Review;
import taekyungkil.mohawk.capstoneproject.model.User;

public interface PlaceholderAPI {

    @POST("review")
    Call<Review> postReview(
      @Body Review review
    );
    @GET("reviews")
    Call<List<Review>> getReviews();

    @GET("myreviews/{id}")
    Call<List<Review>> getMyReviews(
      @Path("id") Integer id
    );
    @GET("reviewsbyid/{id}")
    Call<List<Review>> getReviewsById(
            @Path("id") Integer id
    );

    @DELETE("appointment/{id}")
    Call<Appointment> deleteAppointment(
      @Path("id") Integer id
    );

    @GET("restaurants")
    Call<List<Restaurant>> getRestaurants();

    @GET("restaurant/{id}")
    Call<Restaurant> getRestaurant(
      @Path("id") Integer id
    );

    @GET("restaurantbyuser/{owner_id}")
    Call<Restaurant> getRestaurantByUser(
            @Path("owner_id") Integer id
    );

    @Headers("Content-Type: application/json")
    @PUT("availableSeat/{id}")
    Call<Restaurant> putRestaurant(
            @Body AvailableSeatRequest availableSeatRequest,
            @Path("id") Integer id


    );

    @PUT("checkout/{id}")
    Call<Restaurant> putCheckout(
            @Path("id") Integer id
    );

    @PUT("checkin/{id}")
    Call<Restaurant> putCheckin(
            @Path("id") Integer id
    );


    @POST("user")
    Call<User> postUser(
      @Body  User user
    );
    @POST("appointment")
    Call<Appointment> postAppointment(
        @Body Appointment appointment
    );

    @GET("appointments/{id}")
    Call<List<Appointment>> getAppointments(
            @Path("id") Integer id
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