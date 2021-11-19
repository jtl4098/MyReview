package taekyungkil.mohawk.capstoneproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Rating;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taekyungkil.mohawk.capstoneproject.model.Restaurant;
import taekyungkil.mohawk.capstoneproject.model.Review;
import taekyungkil.mohawk.capstoneproject.model.User;

public class AddReviewActivity extends AppCompatActivity {
    private Restaurant restaurant;
    private User user;
    private ActionBar myActionBar;

    private TextInputEditText title,description;
    private RatingBar ratingBar;
    private Button button, selectPicture;

    private Uri resultData;
    private final int PICK_IMAGE_REQUEST = 22;
    private StorageReference mStorageReference;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
        restaurant = getIntent().getParcelableExtra("restaurant");
        user = getIntent().getParcelableExtra("user");
        myActionBar = getSupportActionBar();
        myActionBar.hide();

        title = findViewById(R.id.name_add_review);
        description = findViewById(R.id.description_add_review);
        ratingBar = findViewById(R.id.rating_bar_review);
        button = findViewById(R.id.share_add_review);
        selectPicture = findViewById(R.id.select_picture);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        selectPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(title.getText().toString().equals("")){
                    Toast.makeText(AddReviewActivity.this, "Please Enter Title", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(description.getText().toString().equals("")){
                    Toast.makeText(AddReviewActivity.this, "Please Enter Description", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                button.setClickable(false);
                StorageReference reference;
                reference = mStorageReference.child("uploads/restaurant/reviewImg/"+restaurant.getRestaurant_id() + "/"+title.getText().toString());
                reference.putFile(resultData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uri.isComplete());
                        Uri url = uri.getResult();

                        Review review = new Review(
                                Integer.parseInt(user.getId()),
                                restaurant.getRestaurant_id(),
                                title.getText().toString(),
                                ratingBar.getRating(),
                                description.getText().toString() );
                        review.setImg_url(url.toString());
                        review.setRestaurant_name(restaurant.getName());
                        PlaceholderAPI service = RetrofitClientInstance.getRetrofitInstance().create(PlaceholderAPI.class);
                        Call<Review> call = service.postReview(review);
                        call.enqueue(new Callback<Review>() {
                            @Override
                            public void onResponse(Call<Review> call, Response<Review> response) {
                                Toast.makeText(AddReviewActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFailure(Call<Review> call, Throwable t) {

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddReviewActivity.this, "Unable to upload the file! Please try again", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if(requestCode == PICK_IMAGE_REQUEST
                    && resultCode == RESULT_OK
                    && data != null
                    && data.getData() != null){
                resultData = data.getData();
                selectPicture.setText("Selected!");
            }


        }catch (Exception e){
            Log.d("Error : ", e.getMessage());
        }
    }

    // Select Image method
    private void SelectImage()
    {
        try{
            // Defining Implicit Intent to mobile gallery
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(
                    Intent.createChooser(
                            intent,
                            "Select Image from here..."),
                    PICK_IMAGE_REQUEST);
        }catch (Exception e){
            Log.d("Error : ", e.getMessage());
        }

    }
}