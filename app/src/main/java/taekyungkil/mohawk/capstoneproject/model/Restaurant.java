package taekyungkil.mohawk.capstoneproject.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Restaurant implements Parcelable {

    @SerializedName("restaurant_id")
    private int restaurant_id;

    @SerializedName("name")
    private String name;
    private boolean isAvailable;

    @SerializedName("rating")
    private double rating;

    @SerializedName("current_seat")
    private int current_seat;
    @SerializedName("max_seat")
    private int max_seat;

    @SerializedName("img_url")
    private String img_url;

    @SerializedName("owner_id")
    private int owner_Id;

    @SerializedName("address")
    private String address;

    public Restaurant(int restaurant_id, String name, boolean isAvailable, double rating, int current_seat, int max_seat) {
        this.restaurant_id = restaurant_id;
        this.name = name;
        this.isAvailable = isAvailable;
        this.rating = rating;
        this.current_seat = current_seat;
        this.max_seat = max_seat;
    }

    public Restaurant(String name, double rating, int current_seat, int max_seat, String img_url, int owner_Id, String address) {
        this.name = name;

        this.rating = rating;
        this.current_seat = current_seat;
        this.max_seat = max_seat;
        this.img_url = img_url;
        this.owner_Id = owner_Id;
        this.address = address;
    }

    public Restaurant() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    protected Restaurant(Parcel in) {
        restaurant_id = in.readInt();
        name = in.readString();
        isAvailable = in.readByte() != 0;
        rating = in.readDouble();
        current_seat = in.readInt();
        max_seat = in.readInt();
        img_url = in.readString();
        owner_Id = in.readInt();
        address = in.readString();
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    public int getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(int restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getCurrent_seat() {
        return current_seat;
    }

    public void setCurrent_seat(int current_seat) {
        this.current_seat = current_seat;
    }

    public int getMax_seat() {
        return max_seat;
    }

    public void setMax_seat(int max_seat) {
        this.max_seat = max_seat;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getOwner_Id() {
        return owner_Id;
    }

    public void setOwner_Id(int owner_Id) {
        this.owner_Id = owner_Id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(restaurant_id);
        parcel.writeString(name);
        parcel.writeByte((byte) (isAvailable ? 1 : 0));
        parcel.writeDouble(rating);
        parcel.writeInt(current_seat);
        parcel.writeInt(max_seat);
        parcel.writeString(img_url);
        parcel.writeInt(owner_Id);
        parcel.writeString(address);
    }
}
