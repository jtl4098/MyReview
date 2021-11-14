package taekyungkil.mohawk.capstoneproject.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {

    @SerializedName("user_id")
    private String id;
    @SerializedName("first_name")
    private String first_name;
    @SerializedName("last_name")
    private String last_name;
    @SerializedName("address")
    private String address;
    @SerializedName("is_owner")
    private int isOwner;

    @SerializedName("restaurant_id")
    private String restaurantId;

    @SerializedName("email")
    private String email;

    public User() {
    }

    public User(String first_name, String last_name, String address, int isOwner) {

        this.first_name = first_name;
        this.last_name = last_name;
        this.address = address;
        this.isOwner = isOwner;
    }

    public User(String first_name, String last_name, String address, int isOwner, String email) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.address = address;
        this.isOwner = isOwner;
        this.email = email;
    }

    protected User(Parcel in) {
        id = in.readString();
        first_name = in.readString();
        last_name = in.readString();
        address = in.readString();
        isOwner = in.readInt();
        restaurantId = in.readString();
        email = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int isOwner() {
        return isOwner;
    }

    public void setOwner(int owner) {
        isOwner = owner;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", address='" + address + '\'' +
                ", isOwner=" + isOwner +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(first_name);
        parcel.writeString(last_name);
        parcel.writeString(address);
        parcel.writeInt(isOwner);
        parcel.writeString(restaurantId);
        parcel.writeString(email);
    }
}
