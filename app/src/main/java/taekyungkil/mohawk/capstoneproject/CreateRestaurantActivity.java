package taekyungkil.mohawk.capstoneproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taekyungkil.mohawk.capstoneproject.model.Restaurant;
import taekyungkil.mohawk.capstoneproject.model.User;

public class CreateRestaurantActivity extends AppCompatActivity {

    private TextInputEditText name, max, postal;
    private MaterialButton img;
    private Button submit;
    ProgressBar progressBar;
    private Uri resultData;

    private User user;

    private final int PICK_IMAGE_REQUEST = 22;
    private StorageReference mStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);
        name= findViewById(R.id.name_create);
        max = findViewById(R.id.max_create);
        postal = findViewById(R.id.address_create);
        img = findViewById(R.id.select_img_create);
        submit = findViewById(R.id.btn_register_create);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        user = getIntent().getParcelableExtra("user");
        mStorageReference = FirebaseStorage.getInstance().getReference();
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                SelectImage();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resultData == null){
                    Toast.makeText(CreateRestaurantActivity.this, "Please Select an Restaurant Image", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(name.getText().toString().equals("")){
                    Toast.makeText(CreateRestaurantActivity.this, "Please Enter Username", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(max.getText().toString().equals("")){
                    Toast.makeText(CreateRestaurantActivity.this, "Please Enter Username", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(postal.getText().toString().equals("")){
                    Toast.makeText(CreateRestaurantActivity.this, "Please Enter Username", Toast.LENGTH_SHORT).show();
                    return;
                }
                StorageReference reference;
                reference = mStorageReference.child("uploads/restaurant/img/"+name.getText().toString());
                reference.putFile(resultData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uri.isComplete());
                        Uri url = uri.getResult();

                        Restaurant restaurant = new Restaurant(name.getText().toString(),0, 0, Integer.parseInt(max.getText().toString()),url.toString() ,Integer.parseInt(user.getId()),postal.getText().toString());
                        PlaceholderAPI service = RetrofitClientInstance.getRetrofitInstance().create(PlaceholderAPI.class);
                        Call<Restaurant> call = service.postRestaurant(restaurant);
                        call.enqueue(new Callback<Restaurant>() {
                            @Override
                            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                                Log.d("RESPONSE", Integer.toString(response.body().getRestaurant_id()));
                            }

                            @Override
                            public void onFailure(Call<Restaurant> call, Throwable t) {
                               Log.d("Something", t.getMessage());
                               //TODO  D/Something: java.lang.IllegalStateException: Expected BEGIN_OBJECT but was STRING at line 1 column 2 path $
                            }
                        });

                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


            }
        });

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if(requestCode == PICK_IMAGE_REQUEST
                    && resultCode == RESULT_OK
                    && data != null
                    && data.getData() != null){
                resultData = data.getData();
            }
            progressBar.setVisibility(View.INVISIBLE);

        }catch (Exception e){
            Log.d("Error : ", e.getMessage());
        }


    }
}