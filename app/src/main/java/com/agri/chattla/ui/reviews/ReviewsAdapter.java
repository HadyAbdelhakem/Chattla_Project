package com.agri.chattla.ui.reviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agri.chattla.R;
import com.agri.chattla.model.Review;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.ArrayList;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private Context mContext;
    private List<Review> reviewList = new ArrayList<>();

    public void setList(List<Review> reviewList) {
        this.reviewList = reviewList;
        notifyDataSetChanged();
    }

    public ReviewsAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_review_item, parent, false);
        return new ReviewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final Review review = reviewList.get(position);

        holder.bind(review);

    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUsername;
        private ScaleRatingBar rtExpert;
        private TextView tvReview;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tv_username);
            rtExpert = itemView.findViewById(R.id.rt_expert);
            tvReview = itemView.findViewById(R.id.tv_review);
        }


        private void bind(final Review review) {

            tvUsername.setText(review.getUsername());
            tvReview.setText(review.getReview());
            rtExpert.setRating(review.getRate());

        }

    }

}
