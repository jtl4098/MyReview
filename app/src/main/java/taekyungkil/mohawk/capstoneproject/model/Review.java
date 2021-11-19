package taekyungkil.mohawk.capstoneproject.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {
    private int user_id;
    private int restaurant_id;
    private int id;
    private String title;
    private float rating;
    private String description;
    private String img_url;
    private String restaurant_name;

    public Review() {
    }

    public Review(int user_id, int restaurant_id, String title, float rating, String description) {
        this.user_id = user_id;
        this.restaurant_id = restaurant_id;
        this.title = title;
        this.rating = rating;
        this.description = description;
    }

    protected Review(Parcel in) {
        user_id = in.readInt();
        restaurant_id = in.readInt();
        id = in.readInt();
        title = in.readString();
        rating = in.readFloat();
        description = in.readString();
        img_url = in.readString();
        restaurant_name = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getImg_url() {
        return img_url;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(int restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(user_id);
        parcel.writeInt(restaurant_id);
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeFloat(rating);
        parcel.writeString(description);
        parcel.writeString(img_url);
        parcel.writeString(restaurant_name);
    }
}
