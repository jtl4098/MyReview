package taekyungkil.mohawk.capstoneproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taekyungkil.mohawk.capstoneproject.adapter.RestaurantAdapter;
import taekyungkil.mohawk.capstoneproject.adapter.ReviewAdapter;
import taekyungkil.mohawk.capstoneproject.model.Restaurant;
import taekyungkil.mohawk.capstoneproject.model.Review;
import taekyungkil.mohawk.capstoneproject.model.User;

public class ReviewActivity extends AppCompatActivity {

    private FloatingActionButton add_Review;

    private Restaurant restaurant;
    private User user;
    private TextView name;
    private ArrayList<Review> reviews;

    private RecyclerView recyclerView;
    private ReviewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        restaurant = getIntent().getParcelableExtra("restaurant");
        user = getIntent().getParcelableExtra("user");
        add_Review = findViewById(R.id.btn_add_review);
        name = findViewById(R.id.restaurant_name_review);


        if(restaurant != null){
            name.setText(restaurant.getName());
        }

        add_Review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReviewActivity.this, AddReviewActivity.class);
                intent.putExtra("restaurant", restaurant);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchingData();
    }

    private void fetchingData(){
        reviews = new ArrayList<>();

        PlaceholderAPI service = RetrofitClientInstance.getRetrofitInstance().create(PlaceholderAPI.class);
        Call<List<Review>> call = service.getReviewsById(restaurant.getRestaurant_id());
        call.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                reviews.addAll(response.body());
                setupComponents();
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {

            }
        });
    }
    private void setupComponents(){
        recyclerView = findViewById(R.id.recyclerview_review);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ReviewAdapter(reviews);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ReviewAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Review review = reviews.get(position);
                Intent intent = new Intent(ReviewActivity.this, ReviewDetailActivity.class);
                intent.putExtra("review", review);
                startActivity(intent);

            }
        });
    }
}