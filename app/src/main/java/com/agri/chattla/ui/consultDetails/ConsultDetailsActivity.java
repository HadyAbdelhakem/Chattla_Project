package com.agri.chattla.ui.consultDetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.apkfuns.xprogressdialog.XProgressDialog;
import com.bumptech.glide.Glide;
import com.agri.chattla.R;
import com.agri.chattla.model.Consult;
import com.agri.chattla.model.UserFirbase;
import com.agri.chattla.model.Weather;
import com.agri.chattla.ui.chat.ChatActivity;
import com.agri.chattla.utils.Utilities;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rygelouv.audiosensei.player.AudioSenseiPlayerView;

public class ConsultDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private RoundedImageView imgConsultation;
    private TextView tvType;
    private TextView tvDescription;
    private AudioSenseiPlayerView audioPlayer;
    private Button btStartConsultation;
    private ImageView imgLogout;
    private LinearLayout climateInfo;
//    private CardView lyMapContainer;
    private TextView tvWeatherDescription;
    private TextView tvTemp;
//    private TextView tvTempMax;
//    private TextView tvTempMin;
    private TextView tvHumidity;
    private TextView tvPressure;
    private Consult consult;
//    private MapView mapView;
//    private GoogleMap mMap;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_details);

        initialViews();

        consult = (Consult) getIntent().getExtras().getSerializable("consult");

//        initGoogleMap(savedInstanceState);

        if (consult != null)
            prepareData(consult);


        btStartConsultation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOtherUser();
            }
        });

    }

    private void getOtherUser() {
        XProgressDialog dialog = new XProgressDialog(this, "من فضلك انتظر..", XProgressDialog.THEME_HORIZONTAL_SPOT);
        dialog.show();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Farmers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserFirbase user = snapshot.getValue(UserFirbase.class);
                    if (user != null && user.getPhoneNumber().equals(consult.getSender())) {

                        dialog.dismiss();
                        startActivity(new Intent(ConsultDetailsActivity.this, ChatActivity.class)
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

    private void prepareData(Consult consult) {

        Weather weather = new Gson().fromJson(consult.getWeather(), Weather.class);

        tvType.setText(consult.getCategory());
        tvDescription.setText(consult.getDesc());

      if (weather!=null){
          tvWeatherDescription.setText(weather.getWeatherDescription());
          tvTemp.setText(weather.getTemp());
          tvHumidity.setText(weather.getHumidity());
          tvHumidity.append(" %");
          tvPressure.setText(weather.getPressure());
//          tvTempMax.setText(weather.getTempMax());
//          tvTempMin.setText(weather.getTempMin());
      }
      if (weather == null){
          climateInfo.setVisibility(View.GONE);
      }

      Glide.with(ConsultDetailsActivity.this)
                .load(consult.getImage()).error(R.drawable.logo)
                .into(imgConsultation);


      if (consult.getVoice() != null) {
            audioPlayer.setAudioTarget(consult.getVoice());
            audioPlayer.requestFocus();

      } else {
            audioPlayer.setVisibility(View.GONE);
      }

//        if (consult.getLat() == null || consult.getLat().isEmpty()) {
//            lyMapContainer.setVisibility(View.GONE);
//        } else {
//            latLng = new LatLng(Double.parseDouble(consult.getLat()), Double.parseDouble(consult.getLng()));
//        }


    }

    private void initialViews() {
        imgConsultation = findViewById(R.id.img_consultation);
        tvType = findViewById(R.id.tv_type);
        tvDescription = findViewById(R.id.tv_description);
        audioPlayer = findViewById(R.id.audio_player);
//        mapView = findViewById(R.id.location_map);
        btStartConsultation = findViewById(R.id.bt_start_consultation);
        imgLogout = findViewById(R.id.img_logout);
        climateInfo = findViewById(R.id.climateInfo);
//        lyMapContainer = findViewById(R.id.ly_map_container);
        tvWeatherDescription = findViewById(R.id.tv_weather_description);
        tvTemp = findViewById(R.id.tv_temp);
//        tvTempMax = findViewById(R.id.tv_temp_max);
//        tvTempMin = findViewById(R.id.tv_temp_min);
        tvHumidity = findViewById(R.id.tv_humidity);
        tvPressure = findViewById(R.id.tv_pressure);
        imgLogout.setOnClickListener(this);

    }


//    private void initGoogleMap(Bundle savedInstanceState) {
//
//        Bundle mapViewBundle = null;
//        if (savedInstanceState != null) {
//            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
//        }
//
//        mapView.onCreate(mapViewBundle);
//        mapView.getMapAsync(this);
//
//    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        if (latLng != null) {
//            setLocation(latLng);
//        }
//    }

//    private void setLocation(LatLng latLng) {
//        mMap.addMarker(new MarkerOptions()
//                .position(latLng)
//                .title("موقع الاستشارة"));
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
//
//    }

    @Override
    public void onResume() {
        super.onResume();
//        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
//        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
//        mapView.onStop();
    }


    @Override
    public void onPause() {
//        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
//        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
//        mapView.onLowMemory();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_logout:

                Utilities.showLogoutAlert(ConsultDetailsActivity.this, ConsultDetailsActivity.this.getResources().getString(R.string.message_logout));

                break;

        }
    }
}
