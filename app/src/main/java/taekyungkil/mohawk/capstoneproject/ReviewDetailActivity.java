package taekyungkil.mohawk.capstoneproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import taekyungkil.mohawk.capstoneproject.model.Review;

public class ReviewDetailActivity extends AppCompatActivity {

    private TextView title, description, name_review_detail;
    private RatingBar ratingBar;
    private ImageView imageView;

    private Review review;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);
        review = getIntent().getParcelableExtra("review");


        title = findViewById(R.id.title_review_detail);
        description = findViewById(R.id.description_review_detail);
        ratingBar = findViewById(R.id.rating_review_detail);
        imageView = findViewById(R.id.img_review_detail);
        name_review_detail = findViewById(R.id.name_review_detail);

        if(review != null){
            title.setText(review.getTitle());
            description.setText(review.getDescription());
            ratingBar.setRating(review.getRating());
            name_review_detail.setText(review.getRestaurant_name());
            if(review.getImg_url() != null){
                Glide.with(imageView).load(review.getImg_url()).into(imageView);
            }

        }

    }
}