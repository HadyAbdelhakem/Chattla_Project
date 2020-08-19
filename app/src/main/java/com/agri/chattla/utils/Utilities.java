package com.agri.chattla.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.agri.chattla.R;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {

    private static Task<Void> reference;


    public static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();

    }

    public static boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

//    public static void ShowSnacker(String tittle,String msg, Activity context) {
//
//        Sneaker.with(context) // Activity, Fragment or ViewGroup
//                .setTitle(tittle, R.color.colorPrimary) // Title and title color
//                .setMessage(msg, R.color.colorPrimary) // Message and message color
//                .setDuration(4000) // Time duration to show
//                .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT) // Height of the Sneaker layout
//                .autoHide(true) // Auto hide Sneaker view
//                .setTypeface(ResourcesCompat.getFont(context, R.font.tajawal_medium)) // Custom font for title and message
//                .setCornerRadius(8,8) // Radius and margin for round corner Sneaker. - Version 1.0.2
//                .sneakSuccess();
//
//    }

    public static void showSnackbar(String msg, Context context, View view) {
        // Make and display Snackbar
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.tajawal_medium);

        // Set action text color
        snackbar.setActionTextColor(
                ContextCompat.getColor(context, R.color.white)
        );
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(R.id.snackbar_text);
        // set no of text line
        textView.setMaxLines(2);
        //set text color
        textView.setTextColor(ContextCompat.getColor(context, R.color.white));
        textView.setTypeface(typeface);
        //set text size
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        //Set Snackbar background color
        snackbarView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        snackbar.show();
    }

    public static void showAlertDialog(final Context context, String message, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder.create();
        alert11.show();
    }

    public static void showLogoutAlert(final Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setMessage(message)
                .setPositiveButton(R.string.cancle, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String string = AppPreferences.getUserPhone(context);
                        reference = FirebaseDatabase.getInstance().getReference("Farmers").child(string).child("FcmToken").removeValue();
                        AppPreferences.logout(context);
                    }
                });
        AlertDialog alert11 = builder.create();
        alert11.show();
    }

//    public static void showLogin(final Context context, String message) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder
//                .setTitle(context.getString(R.string.login))
//                .setMessage(message)
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
////                        AppPreferences.logout(context);
//                    }
//                })
//                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//        AlertDialog alert11 = builder.create();
//        alert11.show();
//    }


}
