package taekyungkil.mohawk.capstoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taekyungkil.mohawk.capstoneproject.fragment.TimePickerFragment;
import taekyungkil.mohawk.capstoneproject.model.Appointment;
import taekyungkil.mohawk.capstoneproject.model.AvailableSeatRequest;
import taekyungkil.mohawk.capstoneproject.model.Restaurant;
import taekyungkil.mohawk.capstoneproject.model.User;

public class RestaurantDetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, TimePickerDialog.OnTimeSetListener  {
    private DrawerLayout myDrawer;
    private ActionBar myActionBar;
    private NavigationView myNavView;

    private Restaurant restaurant;

    private ImageView imageView, mapView;
    private TextView name, rating, available , detail_available_count, detail_number;

    private FloatingActionButton btn_check_available;

    private boolean isAvailable = false;
    private Appointment appointment;
    private User user;

    private int hour,minute;

    private CardView reviewCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        appointment = new Appointment();
        user = getIntent().getParcelableExtra("user");
        mapView = findViewById(R.id.map_detail);

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

        reviewCard = findViewById(R.id.review_card);

        reviewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestaurantDetailActivity.this, ReviewActivity.class);
                intent.putExtra("restaurant", restaurant);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
        setComponents();

        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", restaurant.getLng(),restaurant.getLat());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });
    }

    private void setComponents(){
        restaurant = getIntent().getParcelableExtra("restaurant");
        name = findViewById(R.id.detail_name);
        available = findViewById(R.id.detail_available);
        detail_available_count = findViewById(R.id.detail_available_count);
        imageView = findViewById(R.id.detail_img);
        rating = findViewById(R.id.detail_rating);
        detail_number = findViewById(R.id.detail_number);

        String ratingScore = Double.toString(restaurant.getRating()) + " / 5.0";
        rating.setText(ratingScore);

        detail_number.setText(restaurant.getContact_number());
        detail_number.setPaintFlags(detail_number.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        detail_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION ) ;
                contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                contactIntent
                        .putExtra(ContactsContract.Intents.Insert.NAME , restaurant.getName())
                        .putExtra(ContactsContract.Intents.Insert.PHONE , restaurant.getContact_number()) ;
                startActivityForResult(contactIntent , 1 ) ;
            }
        });

        if(restaurant.getName() != null){
            name.setText(restaurant.getName());
        }
        if (restaurant.getMax_seat() != 0 ){
            String str = "";
            str = Integer.toString(restaurant.getCurrent_seat()) + " / " + Integer.toString(restaurant.getMax_seat());
            detail_available_count.setText(str);

            if(restaurant.getMax_seat() - restaurant.getCurrent_seat() <= 0){
                isAvailable = false;
                available.setText("Unavailable");
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
                        appointment.setRestaurant_id(temp.getRestaurant_id());
                        appointment.setUser_id(user.getId());
                        DialogFragment timePicker = new TimePickerFragment();
                        timePicker.show(getSupportFragmentManager(), "time picker");

                    }
                    @Override
                    public void onFailure(Call<Restaurant> call, Throwable t) {

                    }
                });
            }
        });

    }
    @Override
    protected void onActivityResult ( int requestCode , int resultCode , Intent intent) {
        super .onActivityResult(requestCode , resultCode , intent) ;
        if (requestCode == 1 ) {
            if (resultCode == Activity. RESULT_OK ) {
                Toast. makeText ( this, "Added Contact" , Toast. LENGTH_SHORT ).show() ;
            }
            if (resultCode == Activity. RESULT_CANCELED ) {
                Toast. makeText ( this, "Cancelled Added Contact" ,
                        Toast. LENGTH_SHORT ).show() ;
            }
        }
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
                intent2.putExtra("user", user);
                startActivity(intent2);

                break;
            case R.id.nav_fav:
                Intent intent = new Intent(RestaurantDetailActivity.this, MyReviewActivity.class);
                intent.putExtra("user", user);
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

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        hour = hourOfDay;
        this.minute = minute;
        Log.d("Result Time" , Integer.toString(hour) + "  : " + Integer.toString(minute));
        appointment.setHour(hour);
        appointment.setMinute(minute);
        appointment.setUsername(user.getFirst_name() + " " +user.getLast_name());

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                String deviceToken = task.getResult();
                appointment.setToken(deviceToken);

                PlaceholderAPI service = RetrofitClientInstance.getRetrofitInstance().create(PlaceholderAPI.class);
                Call<Appointment> call = service.postAppointment(appointment);
                call.enqueue(new Callback<Appointment>() {
                    @Override
                    public void onResponse(Call<Appointment> call, Response<Appointment> response) {
                        Toast.makeText(RestaurantDetailActivity.this,"Successfully book an appointment",Toast.LENGTH_SHORT).show();
                        Calendar calendar =  Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        getConfirmDialog(calendar);


                    }

                    @Override
                    public void onFailure(Call<Appointment> call, Throwable t) {

                    }
                });
            }
        });


    }

    private void getConfirmDialog(Calendar calendar){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirm");
        builder.setMessage("Want to add the event to your calendar?");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra("beginTime", calendar.getTimeInMillis());
                intent.putExtra("endTime", calendar.getTimeInMillis() + 60 * 60 * 1000);
                intent.putExtra("title", restaurant.getName() + " Reservation");
                startActivity(intent);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}