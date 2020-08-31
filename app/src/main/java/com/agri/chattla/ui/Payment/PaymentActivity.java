package com.agri.chattla.ui.Payment;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.androidnetworking.error.ANError;
import com.apkfuns.xprogressdialog.XProgressDialog;
import com.agri.chattla.R;
import com.agri.chattla.model.Consult;
import com.agri.chattla.model.Discount;
import com.agri.chattla.model.Item;
import com.agri.chattla.model.Notification;
import com.agri.chattla.model.Price;
import com.agri.chattla.ui.base.BaseActivity;
import com.agri.chattla.ui.farmerMain.FarmerMainActivity;
import com.agri.chattla.utils.ApiRequest;
import com.emeint.android.fawryplugin.Plugininterfacing.FawrySdk;
import com.emeint.android.fawryplugin.Plugininterfacing.PayableItem;
import com.emeint.android.fawryplugin.interfaces.FawrySdkCallback;
import com.emeint.android.fawryplugin.views.cutomviews.FawryButton;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

import static com.agri.chattla.utils.AppConstants.merchantId;
import static com.agri.chattla.utils.AppConstants.paymentKey;

public class PaymentActivity extends BaseActivity implements FawrySdkCallback, View.OnClickListener {

    private ImageView imageviewBack;
    private Button btPayment;
    private Button bt_payment_free;
    private FawryButton fawryButton;
    private LinearLayout lyCost;
    private TextView tvSaveCost;
    private TextView tvDiscount;
    private LinearLayout discountBox;
    private LinearLayout ly_cost;
    private LinearLayout costFree_box;


    private StorageReference storageReference;
    private DatabaseReference reference;
    private DatabaseReference refDiscountCodes;
    private FirebaseDatabase database;
    private XProgressDialog dialog;
    private String refId;
    private String topic;
    private Toolbar toolbar;

    private static final int PAYMENT_PLUGIN_REQUEST = 0;
    private String serverUrl = "https://www.atfawry.com";
    private String merchantRefNum;

    private Price price;
    private TextView tvNewPrice;
    private TextView tvLastPrice;
    private TextView tvCodeExpired;
    private TextView tvCodeNotExist;
    private EditText discountCode;
    private Button btnApplyDiscount;


    private Consult consult;

    HashMap<String, Object> map;
    private Uri uriVoice;
    private File audiofile;
    private Uri imageUri;
    private Uri SAimageUri;
    private Uri WAimageUri;
    private String paymentStatus;
    private String code , addValue , expertId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        consult = (Consult) getIntent().getExtras().getSerializable("consult");
        topic = getIntent().getExtras().getString("topic");
        price = (Price) getIntent().getExtras().get("price");
        imageUri = (Uri) getIntent().getExtras().get("problemimageUri");
        SAimageUri = (Uri) getIntent().getExtras().get("SAimageUri");
        WAimageUri = (Uri) getIntent().getExtras().get("WAimageUri");
        audiofile = (File) getIntent().getExtras().get("audiofile");

        refDiscountCodes = FirebaseDatabase.getInstance().getReference().child("Discount");

        Log.e("Consultation_Details ",new Gson().toJson(consult));

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        FawrySdk.init(FawrySdk.Styles.STYLE1);
        merchantRefNum = randomAlphaNumeric(16);

        initialComponent();

        discountCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() == 0){
                    btnApplyDiscount.setEnabled(false);
                    btnApplyDiscount.setVisibility(View.GONE);
                }else {
                    btnApplyDiscount.setEnabled(true);
                    btnApplyDiscount.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        btnApplyDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCode(refDiscountCodes , discountCode.getText().toString().toLowerCase());
                /*Toasty.success(PaymentActivity.this , discountCode.getText().toString().toLowerCase() ,Toasty.LENGTH_LONG).show();*/
            }
        });
    }

    public void checkCode(DatabaseReference reference , String string){
        dialog = new XProgressDialog(this, "انتظر", XProgressDialog.THEME_HORIZONTAL_SPOT);
        dialog.show();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(string)){
                    reference.child(string).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getValue() != null){
                                Discount d = snapshot.getValue(Discount.class);
                                code = d.getValue();
                                if (d.getAddValue() != null){
                                    consult.setAddValue(d.getAddValue());
                                    consult.setCodeExpertId(d.getExpertID());
                                }
                                String newPrice = String.valueOf(Integer.parseInt(price.getNewPrice()) - Integer.parseInt(code));
                                price.setNewPrice(newPrice);

                                if (Integer.parseInt(newPrice) == 0){
                                    ly_cost.setVisibility(View.GONE);
                                    discountBox.setVisibility(View.GONE);
                                    tvCodeNotExist.setVisibility(View.GONE);
                                    btPayment.setVisibility(View.GONE);
                                    costFree_box.setVisibility(View.VISIBLE);
                                    bt_payment_free.setVisibility(View.VISIBLE);
                                    /*Log.e("Amount is ",price.getNewPrice());*/
                                }else {
                                    tvNewPrice.setText(String.format("%s جنيه", price.getNewPrice()));
                                    tvLastPrice.setText(String.format("%s جنيه", price.getLastPrice()));
                                    tvLastPrice.setPaintFlags(tvLastPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                    tvSaveCost.setText(String.format("%s جنيه", Double.parseDouble(price.getLastPrice()) - Double.parseDouble(price.getNewPrice())));
                                    discountBox.setVisibility(View.GONE);
                                    tvCodeNotExist.setVisibility(View.GONE);
                                    /*Log.e("Amount is ",price.getNewPrice());*/
                                }
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }else {

                    tvCodeNotExist.setVisibility(View.VISIBLE);
                    discountCode.setText("");
                    dialog.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initialComponent() {
        imageviewBack = findViewById(R.id.imageview_back);
        fawryButton = findViewById(R.id.fawry_button);
        tvNewPrice = findViewById(R.id.tv_new_price);
        tvLastPrice = findViewById(R.id.tv_last_price);
        btPayment = findViewById(R.id.bt_payment);
        bt_payment_free = findViewById(R.id.bt_payment_free);
        tvCodeExpired = findViewById(R.id.tv_code_expired);
        tvCodeNotExist = findViewById(R.id.tv_code_not_exist);
        discountCode = findViewById(R.id.discount_code);
        btnApplyDiscount = findViewById(R.id.btn_apply_discount);
        discountBox = findViewById(R.id.discount_box);
        ly_cost = findViewById(R.id.ly_cost);
        costFree_box = findViewById(R.id.costFree_box);


        lyCost = findViewById(R.id.ly_cost);
        tvSaveCost = findViewById(R.id.tv_save_cost);
        tvDiscount = findViewById(R.id.tv_discount);


        imageviewBack.setOnClickListener(this);
        btPayment.setOnClickListener(this);
        bt_payment_free.setOnClickListener(this);

        map = new HashMap<>();

        if (price != null) {
            tvNewPrice.setText(String.format("%s جنيه", price.getNewPrice()));
            tvLastPrice.setText(String.format("%s جنيه", price.getLastPrice()));
            tvLastPrice.setPaintFlags(tvLastPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvSaveCost.setText(String.format("%s جنيه", Double.parseDouble(price.getLastPrice()) - Double.parseDouble(price.getNewPrice())));
            //tvDiscount.setText(new StringBuilder().append("%").append(new DecimalFormat("##.##").format(getPercent(Double.parseDouble(price.getLastPrice()), Double.parseDouble(price.getNewPrice())))).append("خصم").toString());
        }
    }



    private void initialFawryPayment() {
        FawrySdk.initialize(this, serverUrl, this, merchantId, merchantRefNum,
                getUserCart(), FawrySdk.Language.AR, PAYMENT_PLUGIN_REQUEST /*for activity Result*/, null, new UUID(1, 2));

    }

    private double getPercent(Double lastPrice, Double newPrice) {

        double difference = lastPrice - newPrice;
        double res = (difference / lastPrice) * 100;

        return res;

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.imageview_back:
                finish();
                break;

            case R.id.bt_payment:
                initialFawryPayment();
                fawryButton.performClick();
                break;

            case R.id.bt_payment_free:
                addImages("paid");
                break;

        }

    }

    private void sendConsult(String paymentStatus ) {

        final String time = new SimpleDateFormat("dd-M-yyyy hh:mm a", Locale.getDefault()).format(Calendar.getInstance().getTime()).toLowerCase();
        if (consult.getTime() == null){
            consult.setTime(time);
        }

        refId = reference.child("Consults").push().getKey();
        map.put("desc", consult.getDesc());
        map.put("category", consult.getCategory());
        map.put("Cropitem" , consult.getCropitem());
        map.put("AgriType" , consult.getAgriType());
        map.put("area" , consult.getArea());
        map.put("nearCrops" , consult.getNearCrops());
        map.put("IrrType" , consult.getIrrType());
        map.put("landType" , consult.getLandType());
        map.put("waterChannel" , consult.getWaterChannel());
        map.put("ProblemText" , consult.getProblemText());
        map.put("weather", consult.getWeather());
        map.put("topic", topic);
        map.put("status", "pending");
        map.put("sender", consult.getSender());
        map.put("id", refId);
        map.put("time", consult.getTime());
        map.put("lat", consult.getLat());
        map.put("lng", consult.getLng());
        map.put("farmerToken", consult.getFarmerToken());
        map.put("merchantRefNum", merchantRefNum);
        map.put("PaymentStatus", paymentStatus);
        map.put("timestamp", ServerValue.TIMESTAMP);
        sendVoice();
    }

    private void addImages(String PaymentStatus){

        dialog = new XProgressDialog(PaymentActivity.this, "جاري الارسال..", XProgressDialog.THEME_CIRCLE_PROGRESS);
        dialog.show();

        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        if (imageUri != null) {

            StorageReference fileReference = storageReference
                    .child(System.currentTimeMillis() + "." + getFileExtention(imageUri));


            Task<Uri> urlTask = fileReference.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        if (downloadUri == null)
                            return;
                        else {
                            map.put("image", downloadUri.toString());
                            if (SAimageUri != null){

                                StorageReference fileReference = storageReference
                                        .child(System.currentTimeMillis() + "." + getFileExtention(SAimageUri));

                                Task<Uri> uriTask = fileReference.putFile(SAimageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if (!task.isSuccessful()){
                                            throw task.getException();
                                        }

                                        return fileReference.getDownloadUrl();
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()){
                                            Uri downloadUri = task.getResult();
                                            if (downloadUri == null)
                                                return;
                                            else {
                                                map.put("SAimage" , downloadUri.toString());
                                                if (WAimageUri != null){

                                                    StorageReference fileReference = storageReference
                                                            .child(System.currentTimeMillis() + "." + getFileExtention(WAimageUri));

                                                    Task<Uri> uriTask1 = fileReference.putFile(WAimageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                                        @Override
                                                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                            if (!task.isSuccessful()){
                                                                throw task.getException();
                                                            }

                                                            return fileReference.getDownloadUrl();
                                                        }
                                                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Uri> task) {
                                                            if (task.isSuccessful()){
                                                                Uri downloadUri = task.getResult();
                                                                if (downloadUri == null)
                                                                    return;
                                                                else {
                                                                    map.put("WAimage" , downloadUri.toString());
                                                                    sendConsult(PaymentStatus);
                                                                }
                                                            }
                                                        }
                                                    });

                                                }else {
                                                    map.put("WAimage" , "e");
                                                    sendConsult(PaymentStatus);
                                                }
                                            }
                                        }
                                    }
                                });

                            }else {
                                map.put("SAimage" , "e");
                                if (WAimageUri != null){

                                    StorageReference fileReference = storageReference
                                            .child(System.currentTimeMillis() + "." + getFileExtention(WAimageUri));

                                    Task<Uri> uriTask1 = fileReference.putFile(WAimageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                        @Override
                                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                            if (!task.isSuccessful()){
                                                throw task.getException();
                                            }

                                            return fileReference.getDownloadUrl();
                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()){
                                                Uri downloadUri = task.getResult();
                                                if (downloadUri == null)
                                                    return;
                                                else {
                                                    map.put("WAimage" , downloadUri.toString());
                                                    sendConsult(PaymentStatus);
                                                }
                                            }
                                        }
                                    });

                                }else {
                                    map.put("WAimage" , "e");
                                    sendConsult(PaymentStatus);
                                }
                            }
                        }
                    }
                }
            });
        }else {
            map.put("image", "e");
            if (SAimageUri != null){

                StorageReference fileReference = storageReference
                        .child(System.currentTimeMillis() + "." + getFileExtention(SAimageUri));

                Task<Uri> uriTask = fileReference.putFile(SAimageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }

                        return fileReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            Uri downloadUri = task.getResult();
                            if (downloadUri == null)
                                return;
                            else {
                                map.put("SAimage" , downloadUri.toString());
                                if (WAimageUri != null){

                                    StorageReference fileReference = storageReference
                                            .child(System.currentTimeMillis() + "." + getFileExtention(WAimageUri));

                                    Task<Uri> uriTask1 = fileReference.putFile(WAimageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                        @Override
                                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                            if (!task.isSuccessful()){
                                                throw task.getException();
                                            }

                                            return fileReference.getDownloadUrl();
                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()){
                                                Uri downloadUri = task.getResult();
                                                if (downloadUri == null)
                                                    return;
                                                else {
                                                    map.put("WAimage" , downloadUri.toString());
                                                    sendConsult(PaymentStatus);
                                                }
                                            }
                                        }
                                    });

                                }else {
                                    map.put("WAimage" , "e");
                                    sendConsult(PaymentStatus);
                                }
                            }
                        }
                    }
                });

            }else {
                map.put("SAimage" , "e");
                if (WAimageUri != null){

                    StorageReference fileReference = storageReference
                            .child(System.currentTimeMillis() + "." + getFileExtention(WAimageUri));

                    Task<Uri> uriTask1 = fileReference.putFile(WAimageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()){
                                throw task.getException();
                            }

                            return fileReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()){
                                Uri downloadUri = task.getResult();
                                if (downloadUri == null)
                                    return;
                                else {
                                    map.put("WAimage" , downloadUri.toString());
                                    sendConsult(PaymentStatus);
                                }
                            }
                        }
                    });

                }else {
                    map.put("WAimage" , "e");
                    sendConsult(PaymentStatus);
                }
            }
        }
    }

    private void sendVoice() {
        storageReference = FirebaseStorage.getInstance().getReference("Audio");
        if (audiofile != null) {
            uriVoice = Uri.fromFile(new File(audiofile.getAbsolutePath()));
            final StorageReference fileReference = storageReference.child(audiofile.getAbsolutePath());

            Task<Uri> urlTask = fileReference.putFile(uriVoice).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        if (downloadUri == null)
                            return;
                        else {
                            map.put("voice", downloadUri.toString());
                            reference.child("Consults").child(refId).setValue(map, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    if (databaseError != null) {
                                        Log.e("databaseError", databaseError.getMessage());
                                    } else {
                                        //checkPaymentStatus();
                                        dialog.dismiss();
                                        Toasty.success(PaymentActivity.this, "تم إضافة إستشارة جديدة", Toasty.LENGTH_SHORT).show();
                                        startActivity(new Intent(PaymentActivity.this, FarmerMainActivity.class)
                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                        finish();
                                    }
                                }
                            });
                        }
                    }
                }
            });

        } else {
            map.put("voice", "e");
            reference.child("Consults").child(refId).setValue(map);
            //checkPaymentStatus();
            Toasty.success(PaymentActivity.this, "تم حجز الاستشارة", Toasty.LENGTH_SHORT).show();
            dialog.dismiss();
            startActivity(new Intent(PaymentActivity.this, FarmerMainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }

    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * paymentKey.length());
            builder.append(paymentKey.charAt(character));
        }
        return builder.toString();
    }

    private List<PayableItem> getUserCart() {
        ArrayList arrayList = new ArrayList();
        Item item = new Item();
        item.setPrice(price.getNewPrice());
        item.setDescription("إستشارة");
        item.setQty("1");
        item.setSku("ITEM_00");
        arrayList.add(item);
        return arrayList;
    }

    @Override
    public void paymentOperationSuccess(String s, Object o) {
        addImages("unPaid");
    }

    @Override
    public void paymentOperationFailure(String s, Object o) {
        Toasty.error(PaymentActivity.this,s, Toasty.LENGTH_SHORT);
    }

    private void sendNotification() {
        Notification notification = new Notification();
        notification.setTo("/topics/"+topic);
        notification.setData(new Notification.Data("استشارة جديدة"));
        notification.setNotification(new Notification.Notification_("محصول " + consult.getCategory(), "تمت إضافة استشارة جديدة خاصة بمحصول" + " "+consult.getCategory()));

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

    private String getFileExtention(Uri uri) {
        ContentResolver contentResolver = PaymentActivity.this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void checkPaymentStatus(){
        reference.child("Consults").child(refId).child("PaymentStatus").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    paymentStatus=dataSnapshot.getValue().toString();
                    if (paymentStatus.equals("paid")){

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}