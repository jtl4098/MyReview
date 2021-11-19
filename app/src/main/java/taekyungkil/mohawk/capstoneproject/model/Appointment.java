package taekyungkil.mohawk.capstoneproject.model;

import com.google.gson.annotations.SerializedName;

public class Appointment {

    @SerializedName("id")
    private String id;

    @SerializedName("restaurant_id")
    private int restaurant_id;

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("hour")
    private int hour;

    @SerializedName("minute")
    private int minute;

    @SerializedName("is_confirmed")
    private int is_confirmed;

    private String username;

    private String token;

    public Appointment() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(int restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }



    public void setIs_confirmed(int is_confirmed) {
        this.is_confirmed = is_confirmed;
    }

    public int getIs_confirmed() {
        return is_confirmed;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
