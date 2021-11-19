package taekyungkil.mohawk.capstoneproject.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import taekyungkil.mohawk.capstoneproject.R;
import taekyungkil.mohawk.capstoneproject.model.Restaurant;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> implements Filterable {


    private ArrayList<Restaurant> restaurants;
    private ArrayList<Restaurant> filtered;

    private static ClickListener clickListener;
    public void setOnItemClickListener(ClickListener clickListener) {
        RestaurantAdapter.clickListener = clickListener;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            filtered = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                filtered.addAll(restaurants);

            }else{
                String filterPathhern = charSequence.toString().toLowerCase().trim();
                for(Restaurant item : restaurants){
                    if(item.getName().toLowerCase().contains(filterPathhern)){
                        filtered.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filtered;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            restaurants.clear();
            restaurants.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public interface ClickListener {
        void onItemClick(int position, View v);

    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageView;
        private TextView title;
        private TextView rating;
        private TextView isAvailable;
        private TextView isAvailableCount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            rating = itemView.findViewById(R.id.rating);
            isAvailable = itemView.findViewById(R.id.isAvailable);
            isAvailableCount = itemView.findViewById(R.id.isAvailable_count);
            imageView = itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(),view);
        }
    }

    public RestaurantAdapter(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    @NonNull
    @Override
    public RestaurantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant, parent, false);
        return new RestaurantAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantAdapter.ViewHolder holder, int position) {
        Restaurant restaurant = restaurants.get(position);
        holder.title.setText(restaurant.getName());

        if(restaurant.getMax_seat() - restaurant.getCurrent_seat() <= 0 ){
            holder.isAvailable.setTextColor(Color.RED);
            holder.isAvailable.setText("Unavailable");
        }
        holder.rating.setText(Double.toString(restaurant.getRating()));


        holder.isAvailableCount.setText(Integer.toString(restaurant.getCurrent_seat()) + " / " + Integer.toString(restaurant.getMax_seat()));
        Glide.with(holder.itemView).load(restaurant.getImg_url()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }
}
