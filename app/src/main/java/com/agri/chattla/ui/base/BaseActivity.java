package com.agri.chattla.ui.base;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.agri.chattla.R;
import com.agri.chattla.ui.custom.EasyArcLoadingDialog;
import com.agri.chattla.utils.SimpleMultiplePermissionListener;
import com.emeint.android.fawryplugin.Plugininterfacing.FawrySdk;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.dmoral.toasty.Toasty;


public class BaseActivity extends AppCompatActivity {

    private static EasyArcLoadingDialog loadingDialog;


    static SimpleMultiplePermissionListener simpleMultiplePermissionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        FawrySdk.init(FawrySdk.Styles.STYLE1);

        Toasty.Config.getInstance()
                .tintIcon(true) // optional (apply textColor also to the icon)
                .setToastTypeface(ResourcesCompat.getFont(this, R.font.tajawal_medium)) // optional
                .setTextSize(18) // optional
                .allowQueue(true) // optional (prevents several Toastys from queuing)
                .apply(); // required


        simpleMultiplePermissionListener = new SimpleMultiplePermissionListener(BaseActivity.this);


        loadingDialog = new EasyArcLoadingDialog(this);


    }

    public static void showLoadingDialog() {
        loadingDialog.show();
    }

    public static void hideLoadingDialog() {
        loadingDialog.cancel();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }


    public void showAlert(Context context, String title, String message, String positive, String negative,
                          DialogInterface.OnClickListener negativeListener, DialogInterface.OnClickListener listener) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        if (!TextUtils.isEmpty(title))
            alertDialog.setTitle(title);

        if (!TextUtils.isEmpty(message))
            alertDialog.setMessage(message);

        alertDialog.setCancelable(true)
                .setIcon(context.getResources().getDrawable(R.mipmap.ic_launcher))
                .setNegativeButton(positive, negativeListener)
                .setPositiveButton(negative, listener);

        AlertDialog chooseDialog = alertDialog.create();
        chooseDialog.show();
        Button negativeButton = chooseDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        Button positiveButton = chooseDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(context.getResources().getColor(R.color.colorPrimary));
    }


    public static void requestAllPermissions(Activity context) {
        Dexter.withActivity(context).withPermissions
                (Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.RECORD_AUDIO)
                .withListener(simpleMultiplePermissionListener).check();

    }


    public void showPermissionRational(final PermissionToken token) {


        new AlertDialog.Builder(this).setTitle("We need this permission").
                setMessage("Please allow this permission")
                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        token.continuePermissionRequest();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        token.cancelPermissionRequest();
                        dialog.dismiss();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        token.cancelPermissionRequest();
                    }
                }).show();

    }

    public void handlePermenentDeniedPermission() {

        new AlertDialog.Builder(this).setTitle("We need this permission").
                setMessage("Please allow this permission from app settings")
                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openSettings();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();


    }

    public void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    public void OpenGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 120);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getActualPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean hasPermissions(Context context, String[] permissions) {

        boolean hasAllPermissions = true;

        for (String permission : permissions) {
            //you can return false instead of assigning, but by assigning you can log all permission values
            if (!hasPermission(context, permission)) {
                hasAllPermissions = false;
            }
        }

        return hasAllPermissions;

    }

    public static boolean hasPermission(Context context, String permission) {

        int res = context.checkCallingOrSelfPermission(permission);

        return res == PackageManager.PERMISSION_GRANTED;

    }


}
