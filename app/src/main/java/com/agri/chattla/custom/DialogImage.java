package com.agri.chattla.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.agri.chattla.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class DialogImage extends Dialog implements View.OnClickListener {

    private RoundedImageView imageView;
    private Button btCancle;
    //private Button btSave;
    private String imageUrl;


    public DialogImage(@NonNull Context context, String imageUrl) {
        super(context);
        this.imageUrl = imageUrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setCancelable(true);
        this.setContentView(R.layout.dialog_image);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        imageView = findViewById(R.id.Card_profile);
        btCancle = findViewById(R.id.bt_cancle);
        //btSave = findViewById(R.id.bt_save);

        Glide.with(getContext()).load(imageUrl).apply(new RequestOptions().error(R.drawable.loading))
                .into(imageView);

        //btSave.setOnClickListener(this);
        btCancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_cancle:
                dismiss();
                break;
//            case R.id.bt_save:
//                Image.downloadImage(imageView);
//                break;


        }
    }


}
