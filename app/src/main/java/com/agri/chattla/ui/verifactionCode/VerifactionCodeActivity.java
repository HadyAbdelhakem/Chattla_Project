package com.agri.chattla.ui.verifactionCode;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.agri.chattla.R;
import com.agri.chattla.ui.register.RegisterActivity;
import com.agri.chattla.ui.phone.MobileNumActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;

public class VerifactionCodeActivity extends AppCompatActivity {

    private String codeSent;
    private FirebaseAuth mAuth;
    private EditText editText;
    String phoneNumber1 , FphoneNumber ;
    ProgressDialog dialog ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_sms);

        mAuth = FirebaseAuth.getInstance();
        editText = findViewById(R.id.et_code);

        phoneNumber1 = getIntent().getStringExtra("pn");
        FphoneNumber = "+2" + phoneNumber1;
        TextView textview3_2 = findViewById(R.id.textview3_2);
        textview3_2.setText(phoneNumber1);

        sendVerificationcode(FphoneNumber);


        findViewById(R.id.btn3_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText edittext3_0 = findViewById(R.id.et_code);
                String code = edittext3_0.getText().toString();

                if (code.isEmpty()){
                    edittext3_0.setError("ادخل الكود المرسل اليك");
                    edittext3_0.requestFocus();
                    return;
                }

                if (code.length() < 6){
                    edittext3_0.setError("الكود غير صحيح");
                    edittext3_0.requestFocus();
                    return;
                }

                dialog = new ProgressDialog(VerifactionCodeActivity.this);
                dialog.setMessage("انتظر");
                dialog.show();
                verifyCode(code);
            }
        });

        findViewById(R.id.imageview3_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        findViewById(R.id.textview3_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(VerifactionCodeActivity.this, MobileNumActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(VerifactionCodeActivity.this);
        builder.setMessage("هل انت متأكد من الغاء عملية التسجيل؟")
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton( "الغاء" , null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private  void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        dialog.dismiss();
                        if (task.isSuccessful()) {
                            Intent i = new Intent(VerifactionCodeActivity.this, RegisterActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.putExtra("pn" , phoneNumber1 );
                            startActivity(i);
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(),"الكود غير صحيح",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void sendVerificationcode(String phone){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if (code != null ){
                editText.setText(code);
                dialog = new ProgressDialog(VerifactionCodeActivity.this);
                dialog.setMessage("انتظر");
                dialog.show();
                verifyCode(code);
            }else {
                dialog = new ProgressDialog(VerifactionCodeActivity.this);
                dialog.setMessage("انتظر");
                dialog.show();
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            Toast.makeText(VerifactionCodeActivity.this, e.getMessage() , Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent = s ;
        }
    };

}


