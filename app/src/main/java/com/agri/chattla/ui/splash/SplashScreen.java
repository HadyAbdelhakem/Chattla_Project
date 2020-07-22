package com.agri.chattla.ui.splash;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.agri.chattla.R;
import com.agri.chattla.ui.welcome.WelcomeActivity;
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        //checkConnection();

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
                },1000);
            }

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE ){
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        Intent loginpage = new Intent(SplashScreen.this, WelcomeActivity.class);
                        startActivity(loginpage);
                        finish();
                    }
                },1000);
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
