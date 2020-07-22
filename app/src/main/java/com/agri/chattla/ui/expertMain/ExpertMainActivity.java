package com.agri.chattla.ui.expertMain;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.agri.chattla.R;
import com.agri.chattla.custom.InfoDialog;
import com.agri.chattla.model.Consult;
import com.agri.chattla.model.UserFirbase;
import com.agri.chattla.ui.base.BaseActivity;
import com.agri.chattla.ui.consultDetails.ConsultDetailsActivity;
import com.agri.chattla.utils.AppPreferences;
import com.agri.chattla.utils.FcmNotifier;
import com.agri.chattla.utils.Utilities;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ExpertMainActivity extends BaseActivity {

    private ShimmerRecyclerView rvConsults;
    private ExpertConsultsAdapter consultsAdapter;
    private List<Consult> consultList;
    private DatabaseReference refExperts, refConsults;

    private LinearLayout lyNoResults;
    private String currentUserId;
    private UserFirbase mUser;

    private ImageView imgLogout;
    private ValueEventListener listener;

    private ImageView imgInfo;

    SwipeRefreshLayout swipeRefreshLayout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_main);

        swipeRefreshLayout = findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recreate();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        currentUserId = AppPreferences.getUserPhone(ExpertMainActivity.this);
        refConsults = FirebaseDatabase.getInstance().getReference("Consults");

        lyNoResults = findViewById(R.id.ly_no_results);
        imgLogout = findViewById(R.id.img_logout);
        imgInfo = findViewById(R.id.img_info);

        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.showLogoutAlert(ExpertMainActivity.this, ExpertMainActivity.this.getResources().getString(R.string.message_logout));
            }
        });

        imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new InfoDialog(ExpertMainActivity.this,mUser).show();
            }
        });

        setupRecyclerConsult();

        getExpertField();

    }

    private void setupRecyclerConsult() {

        consultsAdapter = new ExpertConsultsAdapter(ExpertMainActivity.this, new ExpertConsultsAdapter.onItemClick() {
            @Override
            public void onItemClick(Consult consult, TextView textView) {

                showAlertDialog(consult);

            }
        });

        rvConsults = findViewById(R.id.rv_consults);
        rvConsults.setHasFixedSize(true);
        rvConsults.setLayoutManager(new LinearLayoutManager(ExpertMainActivity.this));
        rvConsults.setAdapter(consultsAdapter);
        rvConsults.showShimmerAdapter();
    }


    private void getExpertField() {
        consultList = new ArrayList<>();
        refExperts = FirebaseDatabase.getInstance().getReference("Expert");

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserFirbase user = snapshot.getValue(UserFirbase.class);
                    if (user != null)
                        if (user.getPhoneNumber().equals(currentUserId)) {
                            mUser = user;
                            if (mUser.getConsultId() != null) {
                                getExpertConsult(mUser.getConsultId());
                            } else {
                                getConsults(user.getField(),user.getField_two(),user.getField_three());
                                Log.e("" , user.getField() +" // "+ user.getField_two() +" // "+ user.getField_three() );
                            }
                        }
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        refExperts.addListenerForSingleValueEvent(listener);
    }

    private void getConsults(String userField, String field_two, String field_three) {

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                   if (snapshot.getValue()!=null){
                       Consult consult = snapshot.getValue(Consult.class);
                       if (consult != null && consult.getPaymentStatus().equals("paid") && !consult.getStatus().equals("finished")) {
                           if (consult.getCategory().equals(userField)) {
                               consultList.add(consult);
                           }
                           if (field_two!=null&&consult.getCategory().equals(field_two)){
                               consultList.add(consult);
                           }
                           if (field_three!=null&&consult.getCategory().equals(field_three)){
                               consultList.add(consult);
                           }
                           }
                       }
                   }

                if (consultList.size() == 0) {
                    rvConsults.setVisibility(View.GONE);
                    lyNoResults.setVisibility(View.VISIBLE);
                } else {
                    lyNoResults.setVisibility(View.GONE);
                    rvConsults.setVisibility(View.VISIBLE);
                }
                consultsAdapter.setConsults(consultList);
                rvConsults.hideShimmerAdapter();
                imgInfo.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        refConsults.addListenerForSingleValueEvent(listener);
    }

    public void showAlertDialog(Consult consult) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ExpertMainActivity.this);
        builder
                .setMessage(R.string.message_accept_consult)
                .setPositiveButton(ExpertMainActivity.this.getResources().getText(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        goDetailsConsult(consult);
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Consults");
                        reference.child(consult.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Consult consult1 = snapshot.getValue(Consult.class);
                                String string = "pending" ;
                                if (string.equals(consult1.getStatus())){
                                    goDetailsConsult(consult);
                                }else {
                                    showAlertDialog_sorry();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                })
                .setNegativeButton(ExpertMainActivity.this.getResources().getText(R.string.cancle), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder.create();
        alert11.show();
    }
    public void showAlertDialog_sorry(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ExpertMainActivity.this);
        builder
                .setMessage(R.string.sorry_message)
                .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        recreate();
                    }
                });

        AlertDialog alert12 = builder.create();
        alert12.show();
    }

    private void goDetailsConsult(Consult consult) {
        refConsults.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    refConsults.child(consult.getId()).child("status").setValue("accepted");
                    refConsults.child(consult.getId()).child("ExpertId").setValue(currentUserId);
                }
                refExperts.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            refExperts.child(currentUserId).child("consultId").setValue(consult.getId());
                        }
                        FcmNotifier.sendNotification("تم قبول استشارتك", "شتلة", consult.getFarmerToken());
                        startActivity(new Intent(ExpertMainActivity.this, ConsultDetailsActivity.class)
                                .putExtra("consult", consult));
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getExpertConsult(String consultId) {

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Consult consult = dataSnapshot1.getValue(Consult.class);
                    if (consult != null && consult.getId().equals(consultId)) {

                        AppPreferences.saveExpertConsult(ExpertMainActivity.this, new Gson().toJson(consult));
                        AppPreferences.saveHaveConsult(ExpertMainActivity.this, mUser.getConsultId());
                        Intent i = new Intent(ExpertMainActivity.this, ConsultDetailsActivity.class)
                                .putExtra("consult", consult).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        refConsults.addListenerForSingleValueEvent(listener);


    }

    @Override
    protected void onDestroy() {
        if (rvConsults != null && listener != null) {
            refExperts.removeEventListener(listener);
        }
        if (refExperts != null && listener != null) {
            refExperts.removeEventListener(listener);
        }

        super.onDestroy();
    }
}
