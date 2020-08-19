package com.agri.chattla.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.agri.chattla.ui.login.LoginActivity;


public class AppPreferences {

    public static void logout(Context context) {
        clearUserDetails(context);
        Intent i = new Intent(context.getApplicationContext(), LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        context.startActivity(i);
        ((Activity) context).finish();

    }

    private static void clearUserDetails(Context context) {
        context.getSharedPreferences(AppConstants.PREFERENCE_USER, Context.MODE_PRIVATE).edit().clear().apply();
    }

    public static String getIsUserInConversation(Context context) {
        return context.getSharedPreferences(AppConstants.PREFERENCE_USER, Context.MODE_PRIVATE).getString(AppConstants.USER_IN_CONVERSATION, null);
    }

    public static void setIsUserInConversation(Context context, String flag) {
        SharedPreferences.Editor editor = context.getSharedPreferences(AppConstants.PREFERENCE_USER, Context.MODE_PRIVATE).edit();
        editor.putString(AppConstants.USER_IN_CONVERSATION, flag);
        editor.commit();
    }

    public static String getMyProfile(Context context) {
        return context.getSharedPreferences(AppConstants.PREFERENCE_USER, Context.MODE_PRIVATE).
                getString(AppConstants.IMAGE, null);
    }

    public static void saveMyProfile(Context context, String s) {
        SharedPreferences.Editor editor = context.getSharedPreferences(AppConstants.PREFERENCE_USER, Context.MODE_PRIVATE).edit();
        editor.putString(AppConstants.IMAGE, s);
        editor.commit();
    }

    public static String gethaveConsult(Context context) {
        return context.getSharedPreferences(AppConstants.PREFERENCE_USER, Context.MODE_PRIVATE).
                getString(AppConstants.HAVE_CONSULT, null);
    }

    public static void saveHaveConsult(Context context, String s) {
        SharedPreferences.Editor editor = context.getSharedPreferences(AppConstants.PREFERENCE_USER, Context.MODE_PRIVATE).edit();
        editor.putString(AppConstants.HAVE_CONSULT, s);
        editor.commit();
    }

    public static void setServiceFlag(Context context, String flag) {
        SharedPreferences.Editor editor = context.getSharedPreferences(AppConstants.PREFERENCE_USER, Context.MODE_PRIVATE).edit();
        editor.putString(AppConstants.ServiceType, flag);
        editor.commit();
    }


    public static String getLatLng(Context context) {
        return context.getSharedPreferences(AppConstants.PREFERENCE_USER, Context.MODE_PRIVATE).getString(AppConstants.LATLNG, null);
    }

    public static void setLatLng(Context context, String latlng) {
        SharedPreferences.Editor editor = context.getSharedPreferences(AppConstants.PREFERENCE_USER, Context.MODE_PRIVATE).edit();
        editor.putString(AppConstants.LATLNG, latlng);
        editor.commit();
    }

    public static String getServiceFlag(Context context) {
        return context.getSharedPreferences(AppConstants.PREFERENCE_USER, Context.MODE_PRIVATE).getString(AppConstants.ServiceType, null);
    }


    public static String getUserPhone(Context context) {
        return context.getSharedPreferences(AppConstants.PREFERENCE_USER, Context.MODE_PRIVATE).getString(AppConstants.PHONE, null);
    }

    public static void saveUserPhone(Context context, String s) {
        SharedPreferences.Editor editor = context.getSharedPreferences(AppConstants.PREFERENCE_USER, Context.MODE_PRIVATE).edit();
        editor.putString(AppConstants.PHONE, s);
        editor.commit();
    }

    public static void saveUserType(Context context, String type) {
        SharedPreferences.Editor editor = context.getSharedPreferences(AppConstants.PREFERENCE_USER, Context.MODE_PRIVATE).edit();
        editor.putString(AppConstants.USER_TYPE, type);
        editor.commit();
    }

    public static String getUserType(Context context) {
        return context.getSharedPreferences(AppConstants.PREFERENCE_USER, Context.MODE_PRIVATE).getString(AppConstants.USER_TYPE, null);
    }

    public static void saveExpertConsult(Context context, String userData) {
        SharedPreferences.Editor editor = context.getSharedPreferences(AppConstants.PREFERENCE_USER, Context.MODE_PRIVATE).edit();
        editor.putString(AppConstants.EXPERT_CONSULT, userData);
        editor.commit();
    }

    public static String getExpertConsult(Context context) {
        return context.getSharedPreferences(AppConstants.PREFERENCE_USER, Context.MODE_PRIVATE)
                .getString(AppConstants.EXPERT_CONSULT, null);
    }

    public static void saveFcmToken(Context context, String token) {
        SharedPreferences.Editor editor = context.getSharedPreferences(AppConstants.PREFERENCE_USER, Context.MODE_PRIVATE).edit();
        editor.putString(AppConstants.FCM_TOKEN, token);
        editor.commit();
    }

    public static String getFcmToken(Context context) {
        return context.getSharedPreferences(AppConstants.PREFERENCE_USER, Context.MODE_PRIVATE)
                .getString(AppConstants.FCM_TOKEN, null);
    }


}
