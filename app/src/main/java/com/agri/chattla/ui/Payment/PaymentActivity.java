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
    private FawryButton fawryButton;
    private LinearLayout lyCost;
    private TextView tvSaveCost;
    private TextView tvDiscount;
    private LinearLayout discountBox;

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
    private String paymentStatus;
    private String code ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        consult = (Consult) getIntent().getExtras().getSerializable("consult");
        price = (Price) getIntent().getExtras().get("price");
        topic = getIntent().getExtras().getString("topic");
        audiofile = (File) getIntent().getExtras().get("audiofile");
        imageUri = (Uri) getIntent().getExtras().get("imageUri");
        refDiscountCodes = FirebaseDatabase.getInstance().getReference().child("Discount");

        Log.e("Consultation_Details ",new Gson().toJson(consult));
        //Toasty.info(PaymentActivity.this , consult.getWeather() , Toasty.LENGTH_LONG).show();

        uriVoice = Uri.fromFile(new File(audiofile.getAbsolutePath()));

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
                checkCode(refDiscountCodes , discountCode.getText().toString());
            }
        });


    }

    public void checkCode(DatabaseReference reference , String string){
        dialog = new XProgressDialog(this, PaymentActivity.this.getResources().getString(R.string.loading_login), XProgressDialog.THEME_HORIZONTAL_SPOT);
        dialog.show();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(string)){
//                    Log.e("Code is : ",string);
//                    Toasty.info(PaymentActivity.this , string , Toasty.LENGTH_LONG).show();
                    reference.child(string).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getValue() != null){
                                Discount d = snapshot.getValue(Discount.class);
                                code = d.getValue();
                                String newPrice = String.valueOf(Integer.parseInt(price.getNewPrice()) - Integer.parseInt(code));
                                price.setNewPrice(newPrice);
                                Log.e("Amount is ",price.getNewPrice());

                                tvNewPrice.setText(String.format("%s جنيه", price.getNewPrice()));
                                tvLastPrice.setText(String.format("%s جنيه", price.getLastPrice()));
                                tvLastPrice.setPaintFlags(tvLastPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                tvSaveCost.setText(String.format("%s جنيه", Double.parseDouble(price.getLastPrice()) - Double.parseDouble(price.getNewPrice())));
                                //tvDiscount.setText(new StringBuilder().append("%").append(new DecimalFormat("##.##").format(getPercent(Double.parseDouble(price.getLastPrice()), Double.parseDouble(price.getNewPrice())))).append("خصم").toString());

                                discountBox.setVisibility(View.GONE);
                                tvCodeNotExist.setVisibility(View.GONE);
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
        tvCodeExpired = findViewById(R.id.tv_code_expired);
        tvCodeNotExist = findViewById(R.id.tv_code_not_exist);
        discountCode = findViewById(R.id.discount_code);
        btnApplyDiscount = findViewById(R.id.btn_apply_discount);
        discountBox = findViewById(R.id.discount_box);


        lyCost = findViewById(R.id.ly_cost);
        tvSaveCost = findViewById(R.id.tv_save_cost);
        tvDiscount = findViewById(R.id.tv_discount);


        imageviewBack.setOnClickListener(this);
        btPayment.setOnClickListener(this);

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

//        FawrySdk.initializeCardTokenizer(this,serverUrl,this,merchantId,merchantRefNum,"01004996690",
//                "anaselshawaf134@gmail.com",FawrySdk.Language.AR,PAYMENT_PLUGIN_REQUEST, null, new UUID(1, 2));
//        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//        ClipData clipData = ClipData.newPlainText("Pay Email" , "pay@chattla.com" );
//        clipboardManager.setPrimaryClip(clipData);
        //Toast.makeText(this , "تم نسخ النص" , Toast.LENGTH_LONG).show();

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
//                sendConsult();
                initialFawryPayment();
                fawryButton.performClick();
                break;

        }

    }

    private void sendConsult(String paymentStatus ) {

        dialog = new XProgressDialog(PaymentActivity.this, "جاري الارسال..", XProgressDialog.THEME_CIRCLE_PROGRESS);
        dialog.show();

        final String time = new SimpleDateFormat("dd-M-yyyy hh:mm a", Locale.getDefault()).format(Calendar.getInstance().getTime()).toLowerCase();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        if (imageUri != null) {
            final StorageReference fileReference = storageReference
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

                            refId = reference.child("Consults").push().getKey();

                            map.put("image", downloadUri.toString());
                            map.put("category", consult.getCategory());
                            map.put("desc", consult.getDesc());
                            map.put("topic", topic);
                            map.put("status", "pending");
                            map.put("sender", consult.getSender());
                            map.put("id", refId);
                            map.put("time", time);
                            map.put("lat", consult.getLat());
                            map.put("lng", consult.getLng());
                            map.put("farmerToken", consult.getFarmerToken());
                            map.put("merchantRefNum", merchantRefNum);
                            map.put("PaymentStatus", paymentStatus);
                            map.put("weather", consult.getWeather());
                            map.put("timestamp", ServerValue.TIMESTAMP);

                            sendVoice();

                        }
                    }
                }
            });
        }

    }

    private void sendVoice() {
        storageReference = FirebaseStorage.getInstance().getReference("Audio");
        if (uriVoice != null) {
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
                                        checkPaymentStatus();
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
            reference.child("Consults").child(refId).setValue(map);
            checkPaymentStatus();
            Toasty.success(PaymentActivity.this, "تم استكمال البيانات سيتم مراجعة الاستشارة", Toasty.LENGTH_SHORT).show();
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
        sendConsult("unPaid");
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
                        sendNotification();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
