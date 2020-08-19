package com.agri.chattla.ui.login;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.agri.chattla.ui.register.Terms_and_Conditions;
import com.apkfuns.xprogressdialog.XProgressDialog;
import com.agri.chattla.R;
import com.agri.chattla.model.Consult;
import com.agri.chattla.model.UserFirbase;
import com.agri.chattla.ui.farmerMain.FarmerMainActivity;
import com.agri.chattla.ui.phone.MobileNumActivity;
import com.agri.chattla.utils.AppPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {


    EditText etPhone, etPassword;
    private DatabaseReference refFarmer, refExpert;
    private String txtPhone;
    private String txtPassword;
    private String loadLogin ;

    private XProgressDialog dialog;
    private UserFirbase currentUser;
    private DatabaseReference refConsults;
    private Consult mConsult;
    private Intent i = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);


        refConsults = FirebaseDatabase.getInstance().getReference().child("Consults");
        refFarmer = FirebaseDatabase.getInstance().getReference().child("Farmers");
        refExpert = FirebaseDatabase.getInstance().getReference().child("Expert");

        dialog = new XProgressDialog(this, LoginActivity.this.getResources().getString(R.string.loading_login), XProgressDialog.THEME_HORIZONTAL_SPOT);

        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);


        findViewById(R.id.bt_login_farmer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validInput()){
                    checkUser(refFarmer, "farmer");
                }
            }
        });

        findViewById(R.id.bt_login_expert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validInput()){
                    checkUser(refExpert, "expert");
                }

            }
        });

        findViewById(R.id.tv_terms_and_conditions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, Terms_and_Conditions.class);
                startActivity(i);
            }
        });

        findViewById(R.id.tv_not_have_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, MobileNumActivity.class);
                startActivity(i);
//                finish();
            }
        });

        findViewById(R.id.forgetpassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(LoginActivity.this);
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
//                bottomSheetDialog.setCanceledOnTouchOutside(false);

                EditText phone_num = bottomSheetDialog.findViewById(R.id.edittext2_0);
                String stringPhoneNum = phone_num.getText().toString();
                Button button = bottomSheetDialog.findViewById(R.id.btn2_0);


                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toasty.success(LoginActivity.this, "تم إرسال الطلب", Toasty.LENGTH_SHORT).show();

                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.show();
            }
        });
    }

    @Override
    protected void onUserLeaveHint() {
        //finishAffinity();
        super.onUserLeaveHint();
    }

    private boolean validInput() {

        dialog.show();
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        txtPhone = etPhone.getText().toString();
        txtPassword = etPassword.getText().toString();

        if (txtPhone.isEmpty()) {
            dialog.dismiss();
            etPhone.setError("برجاء ادخال رقم الموبايل");
            etPhone.requestFocus();
            return false;
        }

        if (txtPhone.length() < 11) {
            dialog.dismiss();
            etPhone.setError("رقم الهاتف غير صحيح");
            etPhone.requestFocus();
            return false;
        }

        if (txtPassword.isEmpty()) {
            dialog.dismiss();
            etPassword.setError("ادخل كلمة المرور");
            etPassword.requestFocus();
            return false;
        }

        if (txtPassword.length() < 6) {
            dialog.dismiss();
            etPassword.setError("كلمة المرور غير صحيحة");
            etPassword.requestFocus();
            return false;
        }
        return true ;
    }

    public void checkUser(DatabaseReference reference, String type) {

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(etPhone.getText().toString())) {

                    LoginFunction(reference, type);

                } else {
                    dialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("هذا المستخدم غير موجود .!")
                            .setPositiveButton("موافق", null);

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void LoginFunction(DatabaseReference reference, String type) {

        reference.child(txtPhone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                UserFirbase user = dataSnapshot.getValue(UserFirbase.class);
                if (user != null) {
                    if (txtPassword.equals(user.getPassWord())) {

                        currentUser = user;
                        AppPreferences.saveUserPhone(LoginActivity.this, txtPhone);
                        AppPreferences.saveFcmToken(LoginActivity.this, FirebaseInstanceId.getInstance().getToken());

                        if (type.equals("farmer")) {

                            if (AppPreferences.getFcmToken(LoginActivity.this) != null) {

                                refFarmer.child(currentUser.getPhoneNumber()).child("FcmToken").setValue(AppPreferences.getFcmToken(LoginActivity.this), new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Consults");
                                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    Consult consult = snapshot.getValue(Consult.class);
                                                    if (consult != null && consult.getSender().equals(currentUser.getPhoneNumber())) {
                                                        Log.e("xxxxxxx", AppPreferences.getFcmToken(LoginActivity.this));
                                                        HashMap<String,Object> map=new HashMap<>();
                                                        map.put("farmerToken",AppPreferences.getFcmToken(LoginActivity.this));
                                                        ref.child(snapshot.getKey()).updateChildren(map);
                                                    }
                                                }

                                                i = new Intent(LoginActivity.this, FarmerMainActivity.class);
                                                AppPreferences.saveUserType(LoginActivity.this, "Farmer");
                                                dialog.dismiss();
                                                startActivity(i);
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                });

                            }else {
                            AppPreferences.saveFcmToken(LoginActivity.this, null);

                            i = new Intent(LoginActivity.this, FarmerMainActivity.class);
                            AppPreferences.saveUserType(LoginActivity.this, "Farmer");
                            dialog.dismiss();
                            startActivity(i);
                            finish();
                            }
                        }else {
                            finishAffinity();
                        }


                    } else {
                        dialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage("كلمة المرور غير صحيحة .!")
                                .setPositiveButton("موافق", null);

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


}


