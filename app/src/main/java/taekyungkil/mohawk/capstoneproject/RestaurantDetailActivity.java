package taekyungkil.mohawk.capstoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taekyungkil.mohawk.capstoneproject.model.AvailableSeatRequest;
import taekyungkil.mohawk.capstoneproject.model.Restaurant;

public class RestaurantDetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout myDrawer;
    private ActionBar myActionBar;
    private NavigationView myNavView;

    private Restaurant restaurant;

    private ImageView imageView;
    private TextView name, rating, available , detail_available_count;

    private FloatingActionButton btn_check_available;

    private boolean isAvailable = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        myDrawer = (DrawerLayout)
                findViewById(R.id.drawer_layout);
        myActionBar = getSupportActionBar();

        myActionBar.setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle myactionbartoggle = new
                ActionBarDrawerToggle(this, myDrawer,
                (R.string.open), (R.string.close));
        myDrawer.addDrawerListener(myactionbartoggle);
        myactionbartoggle.syncState();

        myNavView = (NavigationView)
                findViewById(R.id.nav_view);
        myNavView.setNavigationItemSelectedListener(this);

        setComponents();
    }

    private void setComponents(){
        restaurant = getIntent().getParcelableExtra("restaurant");
        name = findViewById(R.id.detail_name);
        available = findViewById(R.id.detail_available);
        detail_available_count = findViewById(R.id.detail_available_count);
        imageView = findViewById(R.id.detail_img);
        if(restaurant.getName() != null){
            name.setText(restaurant.getName());
        }
        if (restaurant.getMax_seat() != 0 ){
            String str = "";
            str = Integer.toString(restaurant.getCurrent_seat()) + " / " + Integer.toString(restaurant.getMax_seat());
            detail_available_count.setText(str);

            if(restaurant.getMax_seat() - restaurant.getCurrent_seat() <= 0){
                isAvailable = false;
            }else{
                isAvailable = true;
                available.setTextColor(Color.BLUE);
            }
        }
        if(restaurant.getImg_url() != null){
            Glide.with(this).load(restaurant.getImg_url()).into(imageView);
        }

        btn_check_available = findViewById(R.id.btn_check_available);

        btn_check_available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaceholderAPI service = RetrofitClientInstance.getRetrofitInstance().create(PlaceholderAPI.class);
                Call<Restaurant> call = service.getRestaurant(restaurant.getRestaurant_id());
                call.enqueue(new Callback<Restaurant>() {
                    @Override
                    public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                        Log.d("Result", response.body().toString());

                        Restaurant temp = response.body();
                        Log.d("result", Integer.toString(temp.getMax_seat()));
                        Log.d("result", Integer.toString(temp.getCurrent_seat()));

                        if(temp.getMax_seat() < temp.getCurrent_seat() || temp.getMax_seat() != temp.getCurrent_seat()){
                            AvailableSeatRequest request = new AvailableSeatRequest(temp.getMax_seat(), temp.getCurrent_seat());

                            Call<Restaurant> call2 = service.putRestaurant(request,temp.getRestaurant_id());
                            call2.enqueue(new Callback<Restaurant>() {
                                @Override
                                public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                                    Log.d("Result2", response.body().toString());

                                    String str = "";
                                    restaurant.setCurrent_seat(restaurant.getCurrent_seat() + 1);
                                    str = Integer.toString(restaurant.getCurrent_seat()) + " / " + Integer.toString(restaurant.getMax_seat());
                                    detail_available_count.setText(str);
                                }

                                @Override
                                public void onFailure(Call<Restaurant> call, Throwable t) {
                                    Log.d("Result2 - failed", t.getMessage());
                                    String str = "";
                                    restaurant.setCurrent_seat(restaurant.getCurrent_seat() + 1);
                                    str = Integer.toString(restaurant.getCurrent_seat()) + " / " + Integer.toString(restaurant.getMax_seat());
                                    detail_available_count.setText(str);
                                }
                            });
                        }else{
                            Toast.makeText(RestaurantDetailActivity.this,"Please wait",Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onFailure(Call<Restaurant> call, Throwable t) {

                    }
                });
            }
        });

    }
    //set navigation item selected to get proper page
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Show visual for selection
        item.setChecked(true);
        // Close the Drawer
        myDrawer.closeDrawers();
        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent intent2 = new Intent(RestaurantDetailActivity.this, HomeActivity.class);
                startActivity(intent2);

                break;
            case R.id.nav_fav:
                Intent intent = new Intent(RestaurantDetailActivity.this, MyReviewActivity.class);
                startActivity(intent);

                break;

        }
        return false;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Find out the current state of the drawer (open or closed)
        boolean isOpen = myDrawer.isDrawerOpen(GravityCompat.START);
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                // Home button - open or close the drawer
                if (isOpen == true) {
                    myDrawer.closeDrawer(GravityCompat.START);
                } else {
                    myDrawer.openDrawer(GravityCompat.START);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}