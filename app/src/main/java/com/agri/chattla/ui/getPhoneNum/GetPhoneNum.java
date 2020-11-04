package com.agri.chattla.ui.getPhoneNum;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.agri.chattla.R;
import com.agri.chattla.ui.addConsult.AddConsultActivity;
import com.agri.chattla.ui.login.LoginActivity;
import com.agri.chattla.ui.register.RegisterActivity;
import com.agri.chattla.ui.register.Terms_and_Conditions;
import com.agri.chattla.ui.verifactionCode.VerifactionCodeActivity;
import com.apkfuns.xprogressdialog.XProgressDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import es.dmoral.toasty.Toasty;

public class GetPhoneNum extends AppCompatActivity {

    private EditText editText;
    private TextView textView;
    private Uri uri ;
    private XProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_phone_num);

        dialog = new XProgressDialog(this, "من فضلك انتظر..", XProgressDialog.THEME_HORIZONTAL_SPOT);

        editText = findViewById(R.id.et_get_phone);
        findViewById(R.id.bt_get_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidInput()){
                    FirebaseDatabase.getInstance().getReference().child("Farmers").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(editText.getText().toString())){
                                /*Toasty.info(GetPhoneNum.this , "Has Child" , Toasty.LENGTH_LONG).show();*/
                                startActivity(new Intent(GetPhoneNum.this , LoginActivity.class).putExtra("phNum" , editText.getText().toString()));
                                finish();
                            }else {
                                /*Toasty.info(GetPhoneNum.this , "Has not Child" , Toasty.LENGTH_LONG).show();*/
                                Intent i = new Intent(GetPhoneNum.this, RegisterActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.putExtra("pn" , editText.getText().toString() );
                                startActivity(i);
                                finish();
                            }
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });

        textView = findViewById(R.id.tv_terms_and_conditions_1);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri = Uri.parse("https://sites.google.com/view/chattla-terms-and-conditions/home");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

    }

    private Boolean isValidInput() {

        dialog.show();

        if (editText.getText().toString().length() < 11){
            Toasty.error(GetPhoneNum.this, "برجاء كتابة رقم هاتف صحيح", Toasty.LENGTH_SHORT).show();
            dialog.dismiss();
            return false;
        }

        return true;
    }

}
