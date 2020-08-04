package com.agri.chattla.ui.splash;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.agri.chattla.R;
import com.agri.chattla.model.FBDBV;
import com.agri.chattla.ui.welcome.WelcomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {

    private DatabaseReference DBVersion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        DBVersion = FirebaseDatabase.getInstance().getReference().child("FBDBV");
        DBVersion.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null){
                    FBDBV dbv = snapshot.getValue(FBDBV.class);
                    //Log.e("DBV //  " , dbv.getVersion());
                    if (dbv.getVersion().equals("1")){
                        checkConnection();
                    }else {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
                        builder.setMessage("لإستخدام التطبيق برجاء تحميل الاصدار الاخير")
                                .setCancelable(false)
                                .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                        finishAffinity();
                                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                        try {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                        } catch (android.content.ActivityNotFoundException anfe) {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                        }
                                    }
                                });
                        final AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void checkConnection() {

        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();


        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI ){
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {

                        Intent loginpage = new Intent(SplashScreen.this, WelcomeActivity.class);
                        startActivity(loginpage);
                        finish();
                    }
                },3000);
            }

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE ){
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        Intent loginpage = new Intent(SplashScreen.this, WelcomeActivity.class);
                        startActivity(loginpage);
                        finish();
                    }
                },3000);
            }
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
            builder.setMessage(SplashScreen.this.getResources().getString(R.string.no_internet))
                    .setPositiveButton(R.string.exit , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }

}
