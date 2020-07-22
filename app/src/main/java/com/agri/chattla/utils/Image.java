package com.agri.chattla.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;


import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Image {

    public static void downloadImage(RoundedImageView view) {
        BitmapDrawable draw = (BitmapDrawable) view.getDrawable();
        Bitmap bitmap = draw.getBitmap();

        FileOutputStream outStream = null;
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/ShatlaImages");
        dir.mkdirs();
        String fileName = String.format("%d.jpg", System.currentTimeMillis());
        File outFile = new File(dir, fileName);

        Toast.makeText(view.getContext(), "تم حفظ الصورة", Toast.LENGTH_SHORT).show();
        try {
            outStream = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(outFile));
        view.getContext().sendBroadcast(intent);
    }

}
