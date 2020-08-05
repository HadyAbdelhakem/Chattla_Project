package com.agri.chattla.ui.farmerMain;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidnetworking.error.ANError;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.agri.chattla.R;
import com.agri.chattla.model.Consult;
import com.agri.chattla.model.Notification;
import com.agri.chattla.model.Price;
import com.agri.chattla.model.UserFirbase;
import com.agri.chattla.ui.addConsult.AddConsultActivity;
import com.agri.chattla.ui.base.BaseActivity;
import com.agri.chattla.ui.chat.ChatActivity;
import com.agri.chattla.utils.ApiRequest;
import com.agri.chattla.utils.AppPreferences;
import com.agri.chattla.utils.Utilities;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.himanshurawat.hasher.HashType;
import com.himanshurawat.hasher.Hasher;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.agri.chattla.utils.AppConstants.merchantId;
import static com.agri.chattla.utils.AppConstants.paymentKey;

public class FarmerMainActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView tvTittle;
    private Button btAddConsult;
    private ImageView imgLogout;

    private ShimmerRecyclerView rvConsults;
    private MyConsultsAdapter myConsultsAdapter;
    private List<Consult> consultList;
    private DatabaseReference reference;

    private LinearLayout lyNoResults;
    private String currentUser;
    private Price price;
    private ConsultStatusViewModel viewModel;
    private String isConsultUnPaid = "false";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        viewModel = new ViewModelProvider(this).get(ConsultStatusViewModel.class);

        initialViews();

        currentUser = AppPreferences.getUserPhone(this);

        setupRecyclerConsult();

        getMyConsults();

        getConsultPrice();

    }

    private void getConsultPrice() {
        reference = FirebaseDatabase.getInstance().getReference("Price");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    price = snapshot.getValue(Price.class);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initialViews() {
        tvTittle = findViewById(R.id.tv_tittle);
        lyNoResults = findViewById(R.id.ly_no_results);
        rvConsults = findViewById(R.id.rv_consults);
        btAddConsult = findViewById(R.id.bt_add_consult);
        imgLogout = findViewById(R.id.img_logout);
        btAddConsult.setOnClickListener(this);
        imgLogout.setOnClickListener(this);
    }


    private void setupRecyclerConsult() {

        myConsultsAdapter = new MyConsultsAdapter(FarmerMainActivity.this, new MyConsultsAdapter.onItemClick() {

            @Override
            public void onItemClick(Consult consult, LinearLayout layout) {
                if (consult.getStatus().equals("accepted") || consult.getStatus().equals("finished")) {
                    getExpertInfo(consult);
                }
            }
        });

        rvConsults.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(FarmerMainActivity.this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rvConsults.setLayoutManager(layoutManager);
        rvConsults.setAdapter(myConsultsAdapter);
        rvConsults.showShimmerAdapter();
    }

    private void getExpertInfo(Consult consult) {
        DatabaseReference refExperts = FirebaseDatabase.getInstance().getReference("Expert");
        refExperts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserFirbase user = snapshot.getValue(UserFirbase.class);
                    if (user != null && user.getId().equals(consult.getExpertId())) {
                        startActivity(new Intent(FarmerMainActivity.this, ChatActivity.class)
                                .putExtra("user", user)
                                .putExtra("consult", consult));
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getMyConsults() {

        reference = FirebaseDatabase.getInstance().getReference("Consults");
        reference.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                consultList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Consult consult = snapshot.getValue(Consult.class);

//                    if (consult != null && consult.getSender().equals(currentUser)) {
//                        consultList.add(consult);
//                        if (consult.getPaymentStatus().equals("unPaid")) {
//                            checkPaymentStatus(consult);
//                        }
//                    }

                    if (consult != null && consult.getSender().equals(currentUser)) {
                        if (consult.getPaymentStatus().equals("unPaid")) {
                            checkPaymentStatus(consult);
                        }
                        consultList.add(consult);
                    }
                }
                if (consultList.size() == 0) {
                    rvConsults.setVisibility(View.GONE);
                    lyNoResults.setVisibility(View.VISIBLE);
                } else {
                    lyNoResults.setVisibility(View.GONE);
                    rvConsults.setVisibility(View.VISIBLE);
                }
                myConsultsAdapter.setConsults(consultList);
                rvConsults.hideShimmerAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bt_add_consult:


                /*startActivity(new Intent(FarmerMainActivity.this, AddConsultActivity.class)
                        .putExtra("price", price));*/

                if (isConsultUnPaid.equals("true")) {
                    Toasty.error(this, "يرجى دفع الاستشارة السابقة لحجز استشارة جديدة", Toasty.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(FarmerMainActivity.this, AddConsultActivity.class)
                            .putExtra("price", price));
                }

                break;

            case R.id.img_logout:

                Utilities.showLogoutAlert(FarmerMainActivity.this, FarmerMainActivity.this.getResources().getString(R.string.message_logout));

                break;
        }
    }

    private void checkPaymentStatus(Consult consult) {
        viewModel.checkConsultStatus(FarmerMainActivity.this, merchantId, consult.getMerchantRefNum(), getSha_256(consult.getMerchantRefNum()));
        viewModel.mutableLiveData.observe(FarmerMainActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

//                Log.e("PaymentStatus", s);
                switch (s) {

                    case "PAID":
                        FirebaseDatabase.getInstance().getReference("Consults").child(consult.getId())
                                .child("PaymentStatus").setValue("paid");

                        sendNotification(consult);
                        break;

                    case "UNPAID":
                        isConsultUnPaid = "true";
                        break;

                    case "hasNoPaymentStatus":
                    case "REFUNDED":
                    case "EXPIRED":
                    case "CANCELLED":
                    case "FAILED":
                        FirebaseDatabase.getInstance().getReference("Consults").child(consult.getId())
                                .removeValue();
                        break;

                }

            }
        });
    }

    private String getSha_256(String merchantRefNum) {
        String s = "MPW3sIOoZt6OJidw0JhjFg==".concat(merchantRefNum).concat(paymentKey);
        String sha_256 = Hasher.Companion.hash(s, HashType.SHA_256);
        return sha_256;
    }

    private void sendNotification(Consult consult) {
        Notification notification = new Notification();
        notification.setTo("/topics/" + consult.getTopic());
        notification.setData(new Notification.Data("استشارة جديدة"));
        notification.setNotification(new Notification.Notification_("محصول " + consult.getCategory(), "تمت إضافة استشارة جديدة" ));

        String object = new Gson().toJson(notification);
        ApiRequest.sendNotification(object, new ApiRequest.ServiceCallback() {
            @Override
            public void onSuccess(Object response) throws JSONException {
                Log.e("response", response.toString());
//                Toasty.success(PaymentActivity.this.getBaseContext(), "Done").show();
            }

            @Override
            public void onFail(ANError error) throws JSONException {
                Log.e("responseError", error.getErrorBody());

            }
        });

    }

}
