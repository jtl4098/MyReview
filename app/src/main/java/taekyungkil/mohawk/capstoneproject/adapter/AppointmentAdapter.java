package taekyungkil.mohawk.capstoneproject.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import taekyungkil.mohawk.capstoneproject.R;
import taekyungkil.mohawk.capstoneproject.model.Appointment;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    ArrayList<Appointment> appointments;
    private static ClickListener clickListener;

    public interface ClickListener {
        void onItemClick(int position, View v);

    }

    public void setOnItemClickListener(ClickListener clickListener) {
        AppointmentAdapter.clickListener = clickListener;
    }

    public AppointmentAdapter(ArrayList<Appointment> appointments){
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false);
        AppointmentViewHolder appointmentViewHolder = new AppointmentViewHolder(view);
        return appointmentViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        holder.textname.setText(appointment.getUsername());
        String time = appointment.getHour() + " : " + appointment.getMinute();
        holder.texttime.setText(time);
        holder.appointment = appointment;
    }

    @Override
    public int getItemCount() {
        return this.appointments.size();
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textname;
        TextView texttime;
        View rootView;
        int position;
        Appointment appointment;
        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            textname = itemView.findViewById(R.id.username_appointment);
            texttime = itemView.findViewById(R.id.time_appointment);

//            itemView.findViewById(R.id.btn_appointment).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });
            itemView.findViewById(R.id.btn_appointment).setOnClickListener(this);
        }
        @Override
        public void onClick(View v){
            //Log.d("Clicked", "button");
            clickListener.onItemClick(getAdapterPosition(), v);
        }


    }
}
