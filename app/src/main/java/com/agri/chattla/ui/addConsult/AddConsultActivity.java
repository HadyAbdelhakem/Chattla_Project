package com.agri.chattla.ui.addConsult;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import com.makeramen.roundedimageview.RoundedImageView;
import com.rtchagas.pingplacepicker.PingPlacePicker;

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

    private RoundedImageView imgConsultation;
    private ImageView imageviewBack;
    private ImageView imgCamera;
    private ImageView imgGallery;
    private ImageView imgLocation;
    private Spinner spCategory;
    private Spinner spReasons;
    private Button btSaveConsult;
    private TextView farmLocation;
    private ArrayAdapter<String> categoryAdapter;
    private ArrayAdapter<String> reasonAdapter;
    private List<String> categoryList;
    private List<String> resonsList;
    private String selectedCategory;
    private String selectedReason;
    private View farmLocationLayout;
    FusedLocationProviderClient fusedLocationProviderClient;
    private FusedLocationProviderClient fusedLocationClient;
    private LinearLayout locationLayout;

    private XProgressDialog dialog;
    private Uri imageUri;
    int PICK_Camera_IMAGE = 100;
    int SELECT_IMAGE = 120;
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
    private Toolbar toolbar;
    private Price price;

    private DatabaseReference refFarmer;
    private UserFirbase farmer;
    private WeatherViewModel viewModel;

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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationLayout =findViewById(R.id.location_layout);

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

        setUpCategories();
        setUpReasons();

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
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
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
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
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

    @SuppressLint("WrongViewCast")
    private void initialComponent() {

        imgConsultation = findViewById(R.id.img_consultation);
        imgCamera = findViewById(R.id.img_camera);
        imgGallery = findViewById(R.id.img_gallery);
        imgLocation = findViewById(R.id.img_location);
        spCategory = findViewById(R.id.sp_category);
        spReasons = findViewById(R.id.sp_Reasons);
        recordBtn = findViewById(R.id.record_btn);
        timer = findViewById(R.id.record_timer);
        imageviewBack = findViewById(R.id.imageview_back);
        btSaveConsult = findViewById(R.id.bt_save_consult);
        farmLocation = findViewById(R.id.farm_Location);
        farmLocationLayout = findViewById(R.id.farm_Location_layout);

        imgCamera.setOnClickListener(this);
        imgGallery.setOnClickListener(this);
        imgLocation.setOnClickListener(this);
        recordBtn.setOnClickListener(this);
        imageviewBack.setOnClickListener(this);
        btSaveConsult.setOnClickListener(this);

        consult = new Consult();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_location:

                if (hasPermissions(AddConsultActivity.this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                })) {
                    //showPlacePicker();
                    //getLocation();
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
                if (hasPermissions(AddConsultActivity.this, new String[]{
                        Manifest.permission.CAMERA,
                })) {
                    OpenCamera();
                } else {
                    BaseActivity.requestAllPermissions(AddConsultActivity.this);

                }

                break;
            case R.id.img_gallery:

                if (hasPermissions(AddConsultActivity.this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                })) {
                    OpenGallery();
                } else {
                    BaseActivity.requestAllPermissions(AddConsultActivity.this);
                }

                break;


            case R.id.record_btn:
                if (isRecording) {
                    //Stop Recording
                    stopRecording();

                    recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_stopped, null));
                    isRecording = false;
                } else {
                    //Start Recording
                    if (checkPermissions()) {
                        startRecording();

                        recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_recording, null));
                        isRecording = true;
                    }
                }

                break;

            case R.id.imageview_back:
                startActivity(new Intent(AddConsultActivity.this, FarmerMainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
                break;

            case R.id.bt_save_consult:
                if (isValidInput()) {

                    consult.setCategory(selectedCategory);
                    consult.setDesc(selectedReason);
                    consult.setSender(AppPreferences.getUserPhone(AddConsultActivity.this));
                    consult.setFarmerToken(farmer.getFcmToken());

                    startActivity(new Intent(AddConsultActivity.this, PaymentActivity.class)
                            .putExtra("consult", consult)
                            .putExtra("topic", topic)
                            .putExtra("price", price)
                            .putExtra("imageUri", imageUri)
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

    private void showPlacePicker() {

        PingPlacePicker.IntentBuilder builder = new PingPlacePicker.IntentBuilder();
        builder.setAndroidApiKey("AIzaSyBib2cVM7tdINxSmhSj2QwPDyHJgXWtP9o");

        try {
            Intent placeIntent = builder.build(AddConsultActivity.this);
            startActivityForResult(placeIntent, 102);
        } catch (Exception ex) {
            // Google Play services is not available...
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null){
                    try {
                        Geocoder geocoder = new Geocoder(AddConsultActivity.this,
                                Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude() , location.getLongitude() , 1
                        );

                        lat = String.valueOf(addresses.get(0).getLatitude());
                        lng = String.valueOf(addresses.get(0).getLongitude());
                        getWeatherInfo(lat,lng);
                        consult.setLat(lat);
                        consult.setLng(lng);
                        locationLayout.setVisibility(View.GONE);
                        farmLocationLayout.setVisibility(View.VISIBLE);
                        farmLocation.setText(addresses.get(0).getAddressLine(0));
                        dialog.dismiss();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    getLocationManual();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public void OpenGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        startActivityForResult(Intent.createChooser(intent, "Select Picture To Send"), SELECT_IMAGE);
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

    private void OpenCamera() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(AddConsultActivity.this.getPackageManager()) != null) {
            //Create a fileUri to store the image
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(AddConsultActivity.this, "com.agri.chattla.provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(pictureIntent,
                        PICK_Camera_IMAGE);

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK) {
            imageUri = data.getData();
            if (imageUri != null) {
                imgConsultation.setVisibility(View.VISIBLE);
                imgConsultation.setImageURI(imageUri);
            }

             String imagePath = getActualPath(AddConsultActivity.this, imageUri);

            String filePath = SiliCompressor.with(AddConsultActivity.this).compress(imagePath,
                    new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/compress/images"));

//            imageUri = Uri.fromFile(new File(filePath));

        } else if (requestCode == PICK_Camera_IMAGE && resultCode == RESULT_OK) {
            imgConsultation.setVisibility(View.VISIBLE);
            imgConsultation.setImageURI(imageUri);

        } else if (requestCode == 102 && data != null) {
            Place place = PingPlacePicker.getPlace(data);
            if (place != null && place.getLatLng() != null) {
                lat = String.valueOf(place.getLatLng().latitude);
                lng = String.valueOf(place.getLatLng().longitude);
                getWeatherInfo(lat, lng);
                farmLocationLayout.setVisibility(View.VISIBLE);
                farmLocation.setText(place.getName());
                consult.setLat(lat);
                consult.setLng(lng);
            }
        }
    }

    private void getLocationManual(){
        refFarmer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null){
                    UserFirbase userFirbase = snapshot.getValue(UserFirbase.class);
                    String cit = userFirbase.getCit();
                    String gov = userFirbase.getGov();
                    String mLocation = cit + " , " + gov ;
                    locationLayout.setVisibility(View.GONE);
                    farmLocationLayout.setVisibility(View.VISIBLE);
                    farmLocation.setText(mLocation);
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

        if (selectedCategory == null) {
            Toasty.error(AddConsultActivity.this, "اختر نوع المحصول", Toasty.LENGTH_SHORT).show();
            return false;
        }
        if (selectedReason == null ) {
            Toasty.error(AddConsultActivity.this, "اختر سبب للاستشارة", Toasty.LENGTH_SHORT).show();
            return false;
        }
        if (uriVoice == null) {
            Toasty.error(AddConsultActivity.this, "ادخل مقطع صوتى", Toasty.LENGTH_SHORT).show();
            return false;
        }
        if (imageUri == null) {
            Toasty.error(AddConsultActivity.this, "ادخل صورة للاستشارة", Toasty.LENGTH_SHORT).show();
            return false;
        }
//        if (lat == null) {
//            Toasty.error(AddConsultActivity.this, "حدد الموقع الجغرافي", Toasty.LENGTH_SHORT).show();
//            return false;
//        }


        return true;
    }


}
