package taekyungkil.mohawk.capstoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import taekyungkil.mohawk.capstoneproject.adapter.RestaurantAdapter;
import taekyungkil.mohawk.capstoneproject.model.Restaurant;
import taekyungkil.mohawk.capstoneproject.model.User;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout myDrawer;
    private ActionBar myActionBar;
    private NavigationView myNavView;

    private RestaurantAdapter adapter;
    private ArrayList<Restaurant> restaurants;
    private RecyclerView recyclerView;

    private User user;
    private SearchView home_searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        user = getIntent().getParcelableExtra("user");
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

        //setUpComponent();
        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();

    }

    @Override
    protected void onResume() {
        super.onResume();
        testVariables();
    }

    private void setUpComponent(){
        recyclerView = findViewById(R.id.home_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new RestaurantAdapter(restaurants);
        recyclerView.setAdapter(adapter);


        home_searchView = findViewById(R.id.home_searchView);
        home_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        adapter.setOnItemClickListener(new RestaurantAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.d("Clicked ", "YES");
                Intent intent = new Intent(HomeActivity.this, RestaurantDetailActivity.class);
                intent.putExtra("restaurant", restaurants.get(position));
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
    }
    private void testVariables(){
        restaurants = new ArrayList<>();

        PlaceholderAPI service = RetrofitClientInstance.getRetrofitInstance().create(PlaceholderAPI.class);

        Call<List<Restaurant>> call = service.getRestaurants();
        call.enqueue(new Callback<List<Restaurant>>() {
                         @Override
                         public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                             Log.d("Result", response.body().toString());
                             restaurants.addAll(response.body());
                             setUpComponent();
                         }

                         @Override
                         public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                             Log.d("Result", t.getMessage());
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
//                Intent intent2 = new Intent(HomeActivity.this, HomeActivity.class);
//                intent2.putExtra("user", user);
//                startActivity(intent2);

                break;
            case R.id.nav_fav:
                Intent intent = new Intent(HomeActivity.this, MyReviewActivity.class);
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


}