package com.agri.chattla.ui.reviews;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.agri.chattla.R;
import com.agri.chattla.model.Review;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class ReviewsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imgBack;
    private ShimmerRecyclerView rvReviews;
    private LinearLayout lyNoResults;

    private ReviewsAdapter reviewsAdapter;

    private DatabaseReference reference;

    private String expertID;
    List<Review> reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

//        expertID = getIntent().getExtras().getString("expertID");
//
//        initialViews();
//
//        setupRecyclerViewReviews();
//
//        reference = FirebaseDatabase.getInstance().getReference().child("Rates").child(expertID);
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.getValue() != null) {
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        Review review = snapshot.getValue(Review.class);
//                        reviewList.add(review);
//                    }
//                    reviewsAdapter.setList(reviewList);
//                    rvReviews.hideShimmerAdapter();
//                }else {
//                    rvReviews.setVisibility(View.GONE);
//                    lyNoResults.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        imgBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }

//    private void setupRecyclerViewReviews() {
//        reviewsAdapter = new ReviewsAdapter(this);
//        rvReviews.setHasFixedSize(true);
//        rvReviews.setLayoutManager(new LinearLayoutManager(ReviewsActivity.this));
//        rvReviews.setAdapter(reviewsAdapter);
//        rvReviews.showShimmerAdapter();
//
//    }
//
//    private void initialViews() {
//        toolbar = findViewById(R.id.toolbar);
//        imgBack = findViewById(R.id.img_back);
//        rvReviews = findViewById(R.id.rv_reviews);
//        lyNoResults = findViewById(R.id.ly_no_results);
//
//        reviewList = new ArrayList<>();
//    }
}
