package taekyungkil.mohawk.capstoneproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taekyungkil.mohawk.capstoneproject.adapter.ReviewAdapter;
import taekyungkil.mohawk.capstoneproject.model.Review;
import taekyungkil.mohawk.capstoneproject.model.User;

public class MyReviewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReviewAdapter adapter;
    private ArrayList<Review> reviews;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myreview);
        user = getIntent().getParcelableExtra("user");

    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchingData();
    }

    private void fetchingData(){
        reviews = new ArrayList<>();

        PlaceholderAPI service = RetrofitClientInstance.getRetrofitInstance().create(PlaceholderAPI.class);
        Call<List<Review>> call = service.getMyReviews(Integer.parseInt(user.getId()));
        call.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                reviews.addAll(response.body());
                setComponents();
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {

            }
        });
    }

    private void setComponents(){
        recyclerView = findViewById(R.id.recyclerview_myreview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ReviewAdapter(reviews);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ReviewAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Review review = reviews.get(position);
                Intent intent = new Intent(MyReviewActivity.this, ReviewDetailActivity.class);
                intent.putExtra("review", review);
                startActivity(intent);

            }
        });
    }
}