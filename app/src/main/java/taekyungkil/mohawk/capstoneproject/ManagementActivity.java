package taekyungkil.mohawk.capstoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taekyungkil.mohawk.capstoneproject.adapter.AppointmentAdapter;
import taekyungkil.mohawk.capstoneproject.adapter.RestaurantAdapter;
import taekyungkil.mohawk.capstoneproject.model.Appointment;
import taekyungkil.mohawk.capstoneproject.model.AvailableSeatRequest;
import taekyungkil.mohawk.capstoneproject.model.Restaurant;
import taekyungkil.mohawk.capstoneproject.model.User;

public class ManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AppointmentAdapter adapter;
    private User user;
    private Restaurant restaurant;
    private ArrayList<Appointment> appointments;
    private TextView availableSeat;
    private Button checkIn, checkOut;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);
        user = getIntent().getParcelableExtra("user");
        restaurant = getIntent().getParcelableExtra("restaurant");
        availableSeat = findViewById(R.id.available_seat_management);

        availableSeat.setText(restaurant.getCurrent_seat() + " / " + restaurant.getMax_seat());

        checkIn = findViewById(R.id.btn_check_in);
        checkOut = findViewById(R.id.btn_check_out);

        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(restaurant.getCurrent_seat() > 0){
                    PlaceholderAPI service = RetrofitClientInstance.getRetrofitInstance().create(PlaceholderAPI.class);
                    Call<Restaurant> call = service.putCheckout(restaurant.getRestaurant_id());
                    call.enqueue(new Callback<Restaurant>() {
                        @Override
                        public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                            Toast.makeText(ManagementActivity.this,"Check Out",Toast.LENGTH_SHORT).show();
                            restaurant.setCurrent_seat( restaurant.getCurrent_seat() - 1);
                            availableSeat.setText(restaurant.getCurrent_seat() + " / " + restaurant.getMax_seat());
                        }

                        @Override
                        public void onFailure(Call<Restaurant> call, Throwable t) {
                            Log.d("Fail", t.getMessage());
                        }
                    });
                }else{
                    Toast.makeText(ManagementActivity.this,"Enough Available Seat",Toast.LENGTH_SHORT).show();
                }

            }
        });

        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(restaurant.getCurrent_seat() < restaurant.getMax_seat()){
                    PlaceholderAPI service = RetrofitClientInstance.getRetrofitInstance().create(PlaceholderAPI.class);
                    Call<Restaurant> call = service.putCheckin(restaurant.getRestaurant_id());
                    call.enqueue(new Callback<Restaurant>() {
                        @Override
                        public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                            Toast.makeText(ManagementActivity.this,"Check In",Toast.LENGTH_SHORT).show();
                            restaurant.setCurrent_seat( restaurant.getCurrent_seat() + 1);
                            availableSeat.setText(restaurant.getCurrent_seat() + " / " + restaurant.getMax_seat());
                        }

                        @Override
                        public void onFailure(Call<Restaurant> call, Throwable t) {
                            Log.d("Fail", t.getMessage());
                        }
                    });
                }else{
                    Toast.makeText(ManagementActivity.this,"Not Enough Available Seat",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchingData();
    }

    private void fetchingData(){
        appointments = new ArrayList<>();
        PlaceholderAPI service = RetrofitClientInstance.getRetrofitInstance().create(PlaceholderAPI.class);
        Call<List<Appointment>> call = service.getAppointments(restaurant.getRestaurant_id());
        call.enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {

                appointments.addAll(response.body());
                Log.d("Appoint", Integer.toString(appointments.size()));
                setComponents();
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {
                Log.d("Fail Appoint", t.getMessage());

            }
        });
    }
    private void setComponents(){
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new AppointmentAdapter(appointments);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new AppointmentAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                Appointment appointment = appointments.get(position);
                Log.d("Clicked", "button");
                PlaceholderAPI service = RetrofitClientInstance.getRetrofitInstance().create(PlaceholderAPI.class);
                if(restaurant.getCurrent_seat() < restaurant.getMax_seat()){
                    AvailableSeatRequest request = new AvailableSeatRequest(restaurant.getMax_seat(), restaurant.getCurrent_seat());
                    Call<Restaurant> call2 = service.putRestaurant(request,restaurant.getRestaurant_id());
                    call2.enqueue(new Callback<Restaurant>() {
                        @Override
                        public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                            Log.d("Result2", response.body().toString());

                            String str = "";
                            restaurant.setCurrent_seat(restaurant.getCurrent_seat() + 1);
                            str = Integer.toString(restaurant.getCurrent_seat()) + " / " + Integer.toString(restaurant.getMax_seat());
                            availableSeat.setText(str);

                            db.collection("appointment").add(appointment).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    Log.d("Result Success to Firebase ","");

                                    Call<Appointment> call3 = service.deleteAppointment(Integer.parseInt(appointment.getId()));
                                    call3.enqueue(new Callback<Appointment>() {
                                        @Override
                                        public void onResponse(Call<Appointment> call, Response<Appointment> response) {
                                            fetchingData();
                                        }

                                        @Override
                                        public void onFailure(Call<Appointment> call, Throwable t) {

                                        }
                                    });
                                }
                            });

                        }

                        @Override
                        public void onFailure(Call<Restaurant> call, Throwable t) {
                            Log.d("Result2 - ", t.getMessage());

                        }
                    });
                }
            }
        });
    }
}