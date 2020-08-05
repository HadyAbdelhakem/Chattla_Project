package com.agri.chattla.ui.phone;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.agri.chattla.R;
import com.agri.chattla.ui.splash.SplashScreen;
import com.agri.chattla.ui.verifactionCode.VerifactionCodeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;

import com.agri.chattla.model.UserFirbase;

public class MobileNumActivity extends AppCompatActivity {

    private UserFirbase user ;
    FirebaseDatabase database ;
    DatabaseReference ref ;
    ProgressDialog dialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_phone);

        user = new UserFirbase();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("Users");

        dialog = new ProgressDialog(MobileNumActivity.this);
        dialog.setMessage("انتظر");

        findViewById(R.id.btn2_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText edittext2_0 = findViewById(R.id.edittext2_0);
                final String phone = edittext2_0.getText().toString();

                if (edittext2_0.getText().toString().isEmpty()){
                    edittext2_0.setError("ادخل رقم الهاتف");
                    edittext2_0.requestFocus();
                    return;
                }

                if (phone.length() < 11){
                    edittext2_0.setError("رقم الهاتف غير صحيح");
                    edittext2_0.requestFocus();
                    return;
                }



                dialog.show();
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild(phone)){
                            dialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(MobileNumActivity.this);
                            builder.setMessage("هذا الرقم مسجل بالفعل .!")
                                    .setPositiveButton("موافق", null);

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                        else {
                            dialog.dismiss();
                            Intent i = new Intent(MobileNumActivity.this, VerifactionCodeActivity.class);
                            i.putExtra("pn" , phone);
                            startActivity(i);
                         }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        });

        findViewById(R.id.imageview2_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
