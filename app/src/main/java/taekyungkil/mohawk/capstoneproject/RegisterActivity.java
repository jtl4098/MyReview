package taekyungkil.mohawk.capstoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taekyungkil.mohawk.capstoneproject.model.User;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText username_register, password_register, firstName_register, lastName_register, postal_register;
    private Button btn_register ;
    private SwitchMaterial isOwner_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username_register = findViewById(R.id.username_register);
        password_register = findViewById(R.id.password_register);
        firstName_register = findViewById(R.id.first_register);
        lastName_register = findViewById(R.id.last_register);
        postal_register = findViewById(R.id.postal_register);
        isOwner_button = findViewById(R.id.isOwner);
        btn_register = findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Username : ", username_register.getText().toString());
                Log.d("Password : ", password_register.getText().toString());
                if(username_register.getText().toString().equals("")){
                    Toast.makeText(RegisterActivity.this, "Please Enter Username", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password_register.getText().toString().equals("")){
                    Toast.makeText(RegisterActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(firstName_register.getText().toString().equals("")){
                    Toast.makeText(RegisterActivity.this, "Please Enter First Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(lastName_register.getText().toString().equals("")){
                    Toast.makeText(RegisterActivity.this, "Please Enter Last Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(postal_register.getText().toString().equals("")){
                    Toast.makeText(RegisterActivity.this, "Please Enter Postal Code", Toast.LENGTH_SHORT).show();
                    return;
                }

                String username = username_register.getText().toString();
                String password = password_register.getText().toString();
                String firstName = firstName_register.getText().toString();
                String lastName = lastName_register.getText().toString();
                String postalCode = postal_register.getText().toString();
                boolean isOwner = isOwner_button.isChecked();
                User user;
                if(isOwner){
                    user = new User(firstName, lastName, postalCode, 1, username);
                }else{
                    user = new User(firstName, lastName, postalCode, 0, username);
                }

                //Log.d("user Result", user.toString());


                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.createUserWithEmailAndPassword(username, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();

                        PlaceholderAPI service = RetrofitClientInstance.getRetrofitInstance().create(PlaceholderAPI.class);
                        Call<User> call = service.postUser(user);
                        call.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                Log.d("Result", response.toString());
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {

                            }
                        });
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


    }
}