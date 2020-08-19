package com.agri.chattla.ui.addConsult;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.agri.chattla.model.AgriTypes;
import com.agri.chattla.model.IrrTypes;
import com.agri.chattla.model.LandTypes;
import com.agri.chattla.model.WaterChannel;
import com.apkfuns.xprogressdialog.XProgressDialog;
import com.agri.chattla.R;
import com.agri.chattla.model.Consult;
import com.agri.chattla.model.Field;
import com.agri.chattla.model.Price;
import com.agri.chattla.model.Reason;
import com.agri.chattla.model.UserFirbase;
import com.agri.chattla.ui.Payment.PaymentActivity;
import com.agri.chattla.ui.base.BaseActivity;
import com.agri.chattla.ui.farmerMain.FarmerMainActivity;
import com.agri.chattla.utils.AppPreferences;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iceteck.silicompressorr.SiliCompressor;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rtchagas.pingplacepicker.PingPlacePicker;
import com.rygelouv.audiosensei.player.AudioSenseiPlayerView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class AddConsultActivity extends BaseActivity implements View.OnClickListener {


    private ImageView imageviewBack;
    private ImageView imgLocation;
    private Spinner spCategory;
    private Spinner spAgriType;
    private Spinner spirrType;
    private Spinner spWaterChannel;
    private Spinner spReasons;
    private Spinner spLandType;
    private Button btSaveConsult ;
    private TextView farmLocation;
    private ArrayAdapter<String> categoryAdapter;
    private ArrayAdapter<String> agriTypeAdapter;
    private ArrayAdapter<String> irrTypeAdapter;
    private ArrayAdapter<String> waterChannelAdapter;
    private ArrayAdapter<String> landTypeAdapter;
    private ArrayAdapter<String> reasonAdapter;
    private List<String> categoryList;
    private List<String> resonsList;
    private List<String> agriTypesList;
    private List<String> irrTypesList;
    private List<String> landTypesList;
    private List<String> waterChannelList;
    private String selectedCategory;
    private String selectedAgriType;
    private String selectedirrType;
    private String selectedWaterChannel;
    private String selectedlandType;
    private String selectedReason;
    private View farmLocationLayout;
    private LinearLayout locationLayout;
    private LinearLayout locationSelected;
    private EditText cropItemBox;
    private EditText areaBox;
    private EditText nearCropsBox;
    private EditText problemTextBox;
    private XProgressDialog dialog;
    private Uri imageUri = null ;
    private String imageFilePath;
    private File photoFile;
    private String lat, lng;
    private ImageView recordBtn;
    private boolean isRecording = false;
    private MediaRecorder mediaRecorder;
    private Chronometer timer;
    private String recordPermission = Manifest.permission.RECORD_AUDIO;
    private int PERMISSION_CODE = 21;
    private Consult consult;
    private Uri uriVoice;
    private File audiofile;
    private String topic;
    private Field field;
    private Reason reason;
    private AgriTypes agriTypes;
    private IrrTypes irrTypes;
    private LandTypes landTypes;
    private WaterChannel waterChannel ;
    private Toolbar toolbar;
    private Price price;
    private DatabaseReference refFarmer;
    private UserFirbase farmer;
    private WeatherViewModel viewModel;
    static AddConsultActivity instance;
    private AudioSenseiPlayerView audioPlayer;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    int PICK_Camera_IMAGE = 100;
    int SELECT_IMAGE = 120;
    private Uri problemImageUri = null;
    private LinearLayout imageLayout;
    private ImageView imgCamera;
    private ImageView imgGallery;
    private RoundedImageView imgConsultation;
    private Button btDeletePic;
    int PICK_Camera_SAIMAGE = 101;
    int SELECT_SAIMAGE = 121;
    private LinearLayout imageLayoutSA;
    private Uri SAImageUri = null;
    private ImageView img_cameraSA;
    private ImageView img_gallerySA;
    private RoundedImageView img_SA;
    private Button deleteSAPic;
    int PICK_Camera_WAIMAGE = 103;
    int SELECT_WAIMAGE = 122;
    private LinearLayout imageLayoutWA;
    private Uri WAImageUri = null;
    private ImageView img_cameraWA;
    private ImageView img_galleryWA;
    private RoundedImageView img_WA;
    private Button deleteWAPic;
    private LinearLayout recorderLayout;
    private LinearLayout audioLayout;
    private Button deleteVoice;
    private CheckBox checkBox;




    public static AddConsultActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_consult);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        dialog = new XProgressDialog(this, /*AddConsultActivity.this.getResources().getString(R.string.loading_login)*/ "انتظر", XProgressDialog.THEME_HORIZONTAL_SPOT);
        price = (Price) getIntent().getExtras().get("price");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationLayout = findViewById(R.id.location_layout);
        locationSelected = findViewById(R.id.locationSelected);
        imageLayout = findViewById(R.id.imageLayout);
        imageLayoutSA = findViewById(R.id.imageLayoutSA);
        imageLayoutWA = findViewById(R.id.imageLayoutWA);
        recorderLayout = findViewById(R.id.recorderLayout);
        audioLayout = findViewById(R.id.audioLayout);
        cropItemBox = findViewById(R.id.cropItem);
        areaBox = findViewById(R.id.area);
        nearCropsBox = findViewById(R.id.nearCrops);
        problemTextBox = findViewById(R.id.problemTextBox);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        instance = this;

        refFarmer = FirebaseDatabase.getInstance().getReference("Farmers").child(AppPreferences.getUserPhone(this));
        refFarmer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                farmer = dataSnapshot.getValue(UserFirbase.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        initialComponent();
        setUpReasons();
        setUpCategories();
        setUpAgriTypes();
        setUpIrrTypes();
        setUpLandTypes();
        setUpWaterChannel();
    }

    public void soilAnalysisImg(View v) {
        CheckBox checkBox = (CheckBox)v;
        if(checkBox.isChecked()){
            imageLayoutSA.setVisibility(View.VISIBLE);
        }else if (!checkBox.isChecked()){
            imageUri = null ;
            SAImageUri = null ;
            deleteSAPic.setVisibility(View.GONE);
            img_SA.setVisibility(View.GONE);
            imageLayoutSA.setVisibility(View.GONE);
        }
    }

    public void waterAnalysisImg(View v) {
        CheckBox checkBox = (CheckBox)v;
        if(checkBox.isChecked()){
            imageLayoutWA.setVisibility(View.VISIBLE);
        }else if (!checkBox.isChecked()){
            imageUri = null ;
            WAImageUri = null ;
            deleteWAPic.setVisibility(View.GONE);
            img_WA.setVisibility(View.GONE);
            imageLayoutWA.setVisibility(View.GONE);
        }
    }

    public void problemImg(View v) {
        CheckBox checkBox = (CheckBox)v;
        if(checkBox.isChecked()){
            imageLayout.setVisibility(View.VISIBLE);
        }else if (!checkBox.isChecked()){
            imageUri = null ;
            problemImageUri = null ;
            btDeletePic.setVisibility(View.GONE);
            imgConsultation.setVisibility(View.GONE);
            imageLayout.setVisibility(View.GONE);
        }
    }

    public void problemVoice(View v) {
        CheckBox checkBox = (CheckBox)v;
        if(checkBox.isChecked()){
            recorderLayout.setVisibility(View.VISIBLE);
        }else if (!checkBox.isChecked()){
            uriVoice = null ;
            audiofile = null ;
            timer.setBase(SystemClock.elapsedRealtime());
            audioLayout.setVisibility(View.GONE);
            recorderLayout.setVisibility(View.GONE);
            deleteVoice.setVisibility(View.GONE);
        }
    }

    public void problemText(View v) {
        CheckBox checkBox = (CheckBox)v;
        if(checkBox.isChecked()){
            problemTextBox.setVisibility(View.VISIBLE);
        }else if (!checkBox.isChecked()){
            problemTextBox.setText("");
            problemTextBox.setVisibility(View.GONE);
        }
    }

    private void updateLocation() {

        buildLocationRequest();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent());

    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this , MyLocationService.class);
        intent.setAction(MyLocationService.ACTION_PROCESS_UPDATE);
        return PendingIntent.getBroadcast(this   ,  0 , intent , PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);

    }

    public void setLocation(final String lat , final String lng ){
        getWeatherInfo(lat,lng);
        consult.setLat(lat);
        consult.setLng(lng);
        locationLayout.setVisibility(View.GONE);
        locationSelected.setVisibility(View.VISIBLE);
        dialog.dismiss();
    }

    private void setUpReasons() {
        resonsList = new ArrayList<>();
        resonsList.add("اختر سبب الاستشارة");
        getAllReasons();
        reasonAdapter = new ArrayAdapter<String>(AddConsultActivity.this.getBaseContext(), R.layout.layout_custom_spinner, resonsList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                    tv.setGravity(Gravity.CENTER);
                } else {
                    tv.setTextColor(Color.BLACK);
                    tv.setGravity(Gravity.CENTER);
                }
                return view;
            }
        };

        reasonAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spReasons.setAdapter(reasonAdapter);
        spReasons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    selectedReason = resonsList.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setUpAgriTypes(){
        agriTypesList = new ArrayList<>();
        agriTypesList.add("اختر نوع الزراعة");
        getAllAgriTypes();
        agriTypeAdapter = new ArrayAdapter<String>(AddConsultActivity.this.getBaseContext() , R.layout.layout_custom_spinner, agriTypesList){
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                    tv.setGravity(Gravity.CENTER);
                } else {
                    tv.setTextColor(Color.BLACK);
                    tv.setGravity(Gravity.CENTER);
                }
                return view;
            }
        };

        agriTypeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spAgriType.setAdapter(agriTypeAdapter);
        spAgriType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAgriType = agriTypesList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void setUpLandTypes(){
        landTypesList = new ArrayList<>();
        landTypesList.add("اختر نوع التربة");
        getLandType();
        landTypeAdapter = new ArrayAdapter<String>(AddConsultActivity.this.getBaseContext() ,  R.layout.layout_custom_spinner , landTypesList){
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                    tv.setGravity(Gravity.CENTER);
                } else {
                    tv.setTextColor(Color.BLACK);
                    tv.setGravity(Gravity.CENTER);
                }
                return view;
            }
        };

        landTypeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spLandType.setAdapter(landTypeAdapter);
        spLandType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedlandType = landTypesList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpWaterChannel(){
        waterChannelList = new ArrayList<>();
        waterChannelList.add("اختر");
        getWaterChannel();
        waterChannelAdapter = new ArrayAdapter<String>(AddConsultActivity.this.getBaseContext() , R.layout.layout_custom_spinner , waterChannelList ){
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                    tv.setGravity(Gravity.CENTER);
                } else {
                    tv.setTextColor(Color.BLACK);
                    tv.setGravity(Gravity.CENTER);
                }
                return view;
            }
        };

        waterChannelAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spWaterChannel.setAdapter(waterChannelAdapter);
        spWaterChannel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedWaterChannel = waterChannelList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpIrrTypes(){
        irrTypesList = new ArrayList<>();
        irrTypesList.add("اختر نوع الرى");
        getIrrType();
        irrTypeAdapter = new ArrayAdapter<String>(AddConsultActivity.this.getBaseContext() , R.layout.layout_custom_spinner , irrTypesList){
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                    tv.setGravity(Gravity.CENTER);
                } else {
                    tv.setTextColor(Color.BLACK);
                    tv.setGravity(Gravity.CENTER);
                }
                return view;
            }
        };

        irrTypeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spirrType.setAdapter(irrTypeAdapter);
        spirrType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedirrType = irrTypesList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpCategories() {
        categoryList = new ArrayList<>();
        categoryList.add("اختر نوع المحصول");
        getAllCategories();
        categoryAdapter = new ArrayAdapter<String>(AddConsultActivity.this.getBaseContext(), R.layout.layout_custom_spinner, categoryList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                    tv.setGravity(Gravity.CENTER);
                } else {
                    tv.setTextColor(Color.BLACK);
                    tv.setGravity(Gravity.CENTER);
                }
                return view;
            }
        };

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spCategory.setAdapter(categoryAdapter);
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
                    topic = "field_" + position;
                    selectedCategory = categoryList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    private void getAllCategories() {
        DatabaseReference refField = FirebaseDatabase.getInstance().getReference("fields");
        refField.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Field mField = dataSnapshot.getValue(Field.class);
                    if (mField != null) {
                        field = mField;
                        categoryList.add(mField.getName());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void getAllReasons() {
        DatabaseReference refRes = FirebaseDatabase.getInstance().getReference("Reasons");
        refRes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Reason mReason = dataSnapshot.getValue(Reason.class);
                    if (mReason != null) {
                        reason = mReason;
                        resonsList.add(mReason.getName());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void getAllAgriTypes(){
        DatabaseReference refRes = FirebaseDatabase.getInstance().getReference("AgriType");
        refRes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    AgriTypes mAgriTypes = dataSnapshot.getValue(AgriTypes.class);
                    if (mAgriTypes != null){
                        agriTypes = mAgriTypes;
                        agriTypesList.add(mAgriTypes.getName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getIrrType(){
        DatabaseReference refRes = FirebaseDatabase.getInstance().getReference("IrrType");
        refRes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    IrrTypes mIrrTypes = dataSnapshot.getValue(IrrTypes.class);
                    if (mIrrTypes != null){
                        irrTypes = mIrrTypes;
                        irrTypesList.add(mIrrTypes.getName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getLandType(){
        DatabaseReference refRes = FirebaseDatabase.getInstance().getReference("LandType");
        refRes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    LandTypes mLandTypes = dataSnapshot.getValue(LandTypes.class);
                    if (mLandTypes != null){
                        landTypes = mLandTypes ;
                        landTypesList.add(mLandTypes.getName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getWaterChannel(){
        DatabaseReference refRes = FirebaseDatabase.getInstance().getReference("WaterChannel");
        refRes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    WaterChannel mWaterChannel = dataSnapshot.getValue(WaterChannel.class);
                    if (mWaterChannel != null){
                        waterChannel = mWaterChannel ;
                        waterChannelList.add(mWaterChannel.getName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("WrongViewCast")
    private void initialComponent() {

        imgConsultation = findViewById(R.id.img_consultation);
        img_SA = findViewById(R.id.img_SA);
        img_WA = findViewById(R.id.img_WA);
        imgCamera = findViewById(R.id.img_camera);
        img_cameraSA = findViewById(R.id.img_cameraSA);
        img_cameraWA = findViewById(R.id.img_cameraWA);
        img_gallerySA = findViewById(R.id.img_gallerySA);
        img_galleryWA = findViewById(R.id.img_galleryWA);
        imgGallery = findViewById(R.id.img_gallery);
        imgLocation = findViewById(R.id.img_location);
        spCategory = findViewById(R.id.sp_category);
        spAgriType = findViewById(R.id.spAgriType);
        spirrType = findViewById(R.id.irrType);
        spWaterChannel = findViewById(R.id.water_channel);
        spReasons = findViewById(R.id.sp_Reasons);
        spLandType = findViewById(R.id.landType);
        recordBtn = findViewById(R.id.record_btn);
        timer = findViewById(R.id.record_timer);
        imageviewBack = findViewById(R.id.imageview_back);
        btSaveConsult = findViewById(R.id.bt_save_consult);
        btDeletePic = findViewById(R.id.deletePic);
        deleteSAPic = findViewById(R.id.deleteSAPic);
        deleteWAPic = findViewById(R.id.deleteWAPic);
        deleteVoice = findViewById(R.id.deleteVoice);
        farmLocation = findViewById(R.id.farm_Location);
        farmLocationLayout = findViewById(R.id.farm_Location_layout);
        audioPlayer = findViewById(R.id.audio_player);
        checkBox = findViewById(R.id.problemVoice);

        if (Build.VERSION.SDK_INT == 30){
            checkBox.setVisibility(View.GONE);
        }

        imgCamera.setOnClickListener(this);
        imgGallery.setOnClickListener(this);
        imgLocation.setOnClickListener(this);
        recordBtn.setOnClickListener(this);
        imageviewBack.setOnClickListener(this);
        btSaveConsult.setOnClickListener(this);
        btDeletePic.setOnClickListener(this);
        deleteSAPic.setOnClickListener(this);
        deleteWAPic.setOnClickListener(this);
        deleteVoice.setOnClickListener(this);
        img_cameraSA.setOnClickListener(this);
        img_gallerySA.setOnClickListener(this);
        img_cameraWA.setOnClickListener(this);
        img_galleryWA.setOnClickListener(this);
        consult = new Consult();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_location:

                if (hasPermissions(AddConsultActivity.this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                })){
                    final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
                    if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                        buildAlertMessageNoGps();
                    }
                    else {
                        dialog.show();
                        getLocation();
                    }
                } else {
                    BaseActivity.requestAllPermissions(AddConsultActivity.this);
                }
                break;

            case R.id.img_camera:
                if (hasPermissions(AddConsultActivity.this, new String[]{Manifest.permission.CAMERA,})) {
                    OpenCamera(PICK_Camera_IMAGE);
                } else {
                    BaseActivity.requestAllPermissions(AddConsultActivity.this);
                }
                break;

            case R.id.img_gallery:
                if (hasPermissions(AddConsultActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,})) {
                    OpenGallery(SELECT_IMAGE);
                } else {
                    BaseActivity.requestAllPermissions(AddConsultActivity.this);
                }
                break;

            case R.id.deletePic:
                imageUri = null ;
                problemImageUri = null ;
                btDeletePic.setVisibility(View.GONE);
                imgConsultation.setVisibility(View.GONE);
                imageLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.img_cameraSA:
                if (hasPermissions(AddConsultActivity.this, new String[]{Manifest.permission.CAMERA,})) {
                    OpenCamera(PICK_Camera_SAIMAGE);
                } else {
                    BaseActivity.requestAllPermissions(AddConsultActivity.this);
                }
                break;

            case R.id.img_gallerySA:
                if (hasPermissions(AddConsultActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,})) {
                    OpenGallery(SELECT_SAIMAGE);
                } else {
                    BaseActivity.requestAllPermissions(AddConsultActivity.this);
                }
                break;

            case R.id.deleteSAPic:
                imageUri = null ;
                SAImageUri = null ;
                deleteSAPic.setVisibility(View.GONE);
                img_SA.setVisibility(View.GONE);
                imageLayoutSA.setVisibility(View.VISIBLE);
                break;

            case R.id.img_cameraWA:
                if (hasPermissions(AddConsultActivity.this, new String[]{Manifest.permission.CAMERA,})) {
                    OpenCamera(PICK_Camera_WAIMAGE);
                } else {
                    BaseActivity.requestAllPermissions(AddConsultActivity.this);
                }
                break;

            case R.id.img_galleryWA:
                if (hasPermissions(AddConsultActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,})) {
                    OpenGallery(SELECT_WAIMAGE);
                } else {
                    BaseActivity.requestAllPermissions(AddConsultActivity.this);
                }
                break;

            case R.id.deleteWAPic:
                imageUri = null ;
                WAImageUri = null ;
                deleteWAPic.setVisibility(View.GONE);
                img_WA.setVisibility(View.GONE);
                imageLayoutWA.setVisibility(View.VISIBLE);
                break;

            case R.id.record_btn:
                if (isRecording) {
                    stopRecording();

                    recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_stopped, null));
                    isRecording = false;
                } else {
                    if (checkPermissions()) {

                        startRecording();
                        recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_recording, null));
                        isRecording = true;
                    }
                }
                break;

            case R.id.deleteVoice:
                uriVoice = null ;
                audiofile = null ;
                timer.setBase(SystemClock.elapsedRealtime());
                audioLayout.setVisibility(View.GONE);
                recorderLayout.setVisibility(View.VISIBLE);
                deleteVoice.setVisibility(View.GONE);
                break;

            case R.id.imageview_back:
                startActivity(new Intent(AddConsultActivity.this, FarmerMainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
                break;

            case R.id.bt_save_consult:

                if (isValidInput()) {

                    consult.setDesc(selectedReason);
                    consult.setCategory(selectedCategory);
                    consult.setCropitem(cropItemBox.getText().toString());
                    consult.setArea(areaBox.getText().toString());
                    consult.setNearCrops(nearCropsBox.getText().toString());
                    consult.setAgriType(selectedAgriType);
                    consult.setIrrType(selectedirrType);
                    consult.setLandType(selectedlandType);
                    consult.setProblemText(problemTextBox.getText().toString()+"-");
                    consult.setWaterChannel(selectedWaterChannel);
                    consult.setSender(AppPreferences.getUserPhone(AddConsultActivity.this));
                    consult.setFarmerToken(farmer.getFcmToken());

                    startActivity(new Intent(AddConsultActivity.this, PaymentActivity.class)
                            .putExtra("consult", consult)
                            .putExtra("topic", topic)
                            .putExtra("price", price)
                            .putExtra("problemimageUri", problemImageUri)
                            .putExtra("SAimageUri", SAImageUri)
                            .putExtra("WAimageUri", WAImageUri)
                            .putExtra("audiofile", audiofile));

                }
                break;

        }

    }

    private void stopRecording() {

        timer.stop();
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        uriVoice = Uri.fromFile(new File(audiofile.getAbsolutePath()));
        recorderLayout.setVisibility(View.GONE);
        audioLayout.setVisibility(View.VISIBLE);
        deleteVoice.setVisibility(View.VISIBLE);
        audioPlayer.setAudioTarget(audiofile.getAbsolutePath());
        audioPlayer.requestFocus();

    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("برجاء تفعيل استخدام الموقع الجغرافى")
                .setCancelable(false)
                .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void startRecording() {
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();

        File dir = Environment.getExternalStorageDirectory();
        try {
            audiofile = File.createTempFile("sound", ".3gp", dir);
        } catch (IOException e) {
            return;
        }
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(audiofile.getAbsolutePath());
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaRecorder.start();
    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(AddConsultActivity.this, recordPermission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            BaseActivity.requestAllPermissions(AddConsultActivity.this);
            return false;
        }
    }

    private void getLocation() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        updateLocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                    }
                }).check();
    }

    public void OpenGallery(int Integer) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        startActivityForResult(Intent.createChooser(intent, "Select Picture To Send"), Integer);
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = AddConsultActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    private void OpenCamera(int Integer) {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(AddConsultActivity.this.getPackageManager()) != null) {
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(AddConsultActivity.this, "com.agri.chattla.provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(pictureIntent, Integer);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK) {
            imageUri = data.getData();
            problemImageUri = imageUri;
            imageUri = null;
            if (problemImageUri != null) {
                imgConsultation.setVisibility(View.VISIBLE);
                imgConsultation.setImageURI(problemImageUri);
                imageLayout.setVisibility(View.GONE);
                btDeletePic.setVisibility(View.VISIBLE);
            }
        } else if (requestCode == PICK_Camera_IMAGE && resultCode == RESULT_OK) {
            problemImageUri = imageUri;
            imageUri = null ;
            imgConsultation.setVisibility(View.VISIBLE);
            imgConsultation.setImageURI(problemImageUri);
            imageLayout.setVisibility(View.GONE);
            btDeletePic.setVisibility(View.VISIBLE);
        }


        else if (requestCode == SELECT_SAIMAGE && resultCode == RESULT_OK){
            imageUri = data.getData();
            SAImageUri = imageUri ;
            imageUri = null ;
            if (SAImageUri != null) {
                img_SA.setVisibility(View.VISIBLE);
                img_SA.setImageURI(SAImageUri);
                imageLayoutSA.setVisibility(View.GONE);
                deleteSAPic.setVisibility(View.VISIBLE);
            }
        } else if (requestCode == PICK_Camera_SAIMAGE && resultCode == RESULT_OK) {
            SAImageUri = imageUri;
            imageUri = null;
            img_SA.setVisibility(View.VISIBLE);
            img_SA.setImageURI(SAImageUri);
            imageLayoutSA.setVisibility(View.GONE);
            deleteSAPic.setVisibility(View.VISIBLE);
        }


        else if (requestCode == SELECT_WAIMAGE && resultCode == RESULT_OK){
            imageUri = data.getData();
            WAImageUri = imageUri ;
            imageUri = null ;
            if (WAImageUri != null) {
                img_WA.setVisibility(View.VISIBLE);
                img_WA.setImageURI(WAImageUri);
                imageLayoutWA.setVisibility(View.GONE);
                deleteWAPic.setVisibility(View.VISIBLE);
            }
        } else if (requestCode == PICK_Camera_WAIMAGE && resultCode == RESULT_OK) {
            WAImageUri = imageUri;
            imageUri = null;
            img_WA.setVisibility(View.VISIBLE);
            img_WA.setImageURI(WAImageUri);
            imageLayoutWA.setVisibility(View.GONE);
            deleteWAPic.setVisibility(View.VISIBLE);
        }
    }

    private void getWeatherInfo(String lat, String lng) {
        viewModel.getWeatherInfo(this, lat, lng);
        viewModel.mutableLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                consult.setWeather(s);
            }
        });
    }


    private Boolean isValidInput() {

        if (selectedReason == null || selectedReason == "اختر سبب الاستشارة" ) {
            Toasty.error(AddConsultActivity.this, "اختر سبب للاستشارة", Toasty.LENGTH_SHORT).show();
            return false;
        }
        if (selectedCategory == null || selectedCategory == "اختر نوع المحصول") {
            Toasty.error(AddConsultActivity.this, "اختر نوع المحصول", Toasty.LENGTH_SHORT).show();
            return false;
        }
        if (cropItemBox.getText().toString().length() == 0 ){
            Toasty.error(AddConsultActivity.this, "برجاء كتابة الصنف", Toasty.LENGTH_SHORT).show();
            return false;
        }
        if (selectedAgriType == null || selectedAgriType == "اختر نوع الزراعة"){
            Toasty.error(AddConsultActivity.this, "اختر نوع الزراعة", Toasty.LENGTH_SHORT).show();
            return false;
        }
        if (areaBox.getText().toString().length() == 0 ){
            Toasty.error(AddConsultActivity.this, "برجاء كتابة المساحة المزروعة", Toasty.LENGTH_SHORT).show();
            return false;
        }
        if (nearCropsBox .getText().toString().length() == 0 ){
            Toasty.error(AddConsultActivity.this, "برجاء كتابة المحاصيل المجاورة", Toasty.LENGTH_SHORT).show();
            return false;
        }
        if (selectedirrType == null || selectedirrType == "اختر نوع الرى"){
            Toasty.error(AddConsultActivity.this, "اختر نوع الرى", Toasty.LENGTH_SHORT).show();
            return false;
        }
        if (selectedlandType == null || selectedlandType == "اختر نوع التربة" ){
            Toasty.error(AddConsultActivity.this, "اختر نوع التربة", Toasty.LENGTH_SHORT).show();
            return false;
        }
        if (selectedWaterChannel == null || selectedWaterChannel == "اختر" ){
            Toasty.error(AddConsultActivity.this, "هل يوجد قنوات صرف ؟", Toasty.LENGTH_SHORT).show();
            return false;
        }
        if (consult.getLat() == null) {
            Toasty.error(AddConsultActivity.this, "حدد الموقع الجغرافي", Toasty.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }


}
