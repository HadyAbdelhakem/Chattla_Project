package com.agri.chattla.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.agri.chattla.R;
import com.agri.chattla.model.Review;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import es.dmoral.toasty.Toasty;

public class AddReviewDialog extends Dialog {

    private ScaleRatingBar rtReview;
    private EditText etRate;
    private TextView tvAddRate;

    private float rate = 5;
    private Context context;

    private String expertID;
    private String myID;
    private String username;

    private String overalRate;
    private float totalRate = 0;

    DatabaseReference refRate, refUser,refExpert;

    public AddReviewDialog(@NonNull Context context, String expertID, String myID, String overalRate) {
        super(context);
        this.context = context;
        this.expertID = expertID;
        this.myID = myID;
        this.overalRate = overalRate;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setCancelable(true);
        this.setContentView(R.layout.layout_rate_dialog);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        refRate = FirebaseDatabase.getInstance().getReference().child("Rates");
        refUser = FirebaseDatabase.getInstance().getReference().child("Farmers").child(myID);
        refExpert = FirebaseDatabase.getInstance().getReference().child("Expert").child(expertID);
        refUser.child("userName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    username = (String) dataSnapshot.getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        initialViews();

        rtReview.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar ratingBar, float rating, boolean fromUser) {
                rate = rating;
            }
        });

        tvAddRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    addRate();
                    AddReviewDialog.this.dismiss();
            }
        });


    }

    private void addRate() {

        refRate.child(expertID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getValue() != null) {
                        Review review = snapshot.getValue(Review.class);
                        totalRate += 1;
                    }

                }

                Review mReview = new Review(rate, etRate.getText().toString(), username);
                refRate.child(expertID).push().setValue(mReview, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Log.e("databaseError", databaseError.getMessage());
                        } else {
                            refExpert.child("rate").setValue(calculateRate(Float.parseFloat(overalRate), totalRate, rate), new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    Toasty.success(context, R.string.review_added, Toasty.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private boolean checkInfo() {
        if (etRate.getText().toString().isEmpty()) {
            Toast.makeText(context, context.getResources().getString(R.string.message_enter_review), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (rate < 1) {
            Toast.makeText(context, context.getResources().getString(R.string.message_enter_review), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void initialViews() {
        rtReview = findViewById(R.id.rt_review);
        etRate = findViewById(R.id.et_rate);
        tvAddRate = findViewById(R.id.tv_add_rate);

    }

    private String calculateRate(Float overallRate, float totalRate, float newRate) {

        Float r = ((overallRate * totalRate) +  newRate) / (totalRate + 1);
        Log.e("ratng",overalRate+"//"+totalRate+"//"+newRate+"//"+r+"//");
        return String.valueOf(r);

    }
}

