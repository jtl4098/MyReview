package taekyungkil.mohawk.capstoneproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import taekyungkil.mohawk.capstoneproject.R;
import taekyungkil.mohawk.capstoneproject.model.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private static ClickListener clickListener;
    private ArrayList<Review> reviews;

    public void setOnItemClickListener(ClickListener clickListener) {
        ReviewAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);

    }

    public ReviewAdapter(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review_restaurant, parent, false);
        return new ReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviews.get(position);

        if(review != null){
            holder.title.setText(review.getTitle());
            holder.description.setText(review.getDescription());
            String rating = Float.toString(review.getRating()) + " / 5.0";
            holder.rating.setText(rating);
            if(review.getImg_url() != null){
                Glide.with(holder.itemView).load(review.getImg_url()).into(holder.imageView);
            }

        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageView;
        private TextView title;
        private TextView rating;
        private TextView description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_review);
            title = itemView.findViewById(R.id.title_review);
            rating = itemView.findViewById(R.id.rating_review);
            description = itemView.findViewById(R.id.review_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(),view);
        }
    }
}
