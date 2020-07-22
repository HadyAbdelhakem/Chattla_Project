package com.agri.chattla.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.agri.chattla.R;
import com.agri.chattla.model.UserFirbase;
import com.makeramen.roundedimageview.RoundedImageView;

public class InfoDialog extends Dialog {

    private TextView tvUsername;
    private TextView tvBalance;
    private TextView tvRate;
    private RoundedImageView imageProfile;

    UserFirbase userFirbase;
    Context context;

    public InfoDialog(@NonNull Context context, UserFirbase userFirbase) {
        super(context);
        this.context = context;
        this.userFirbase = userFirbase;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setCancelable(true);
        this.setContentView(R.layout.layout_info_dialog);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        tvUsername = findViewById(R.id.tv_username);
        tvBalance = findViewById(R.id.tv_balance);
        tvRate = findViewById(R.id.tv_rate);
        imageProfile = findViewById(R.id.image_profile);


        String rate = userFirbase.getRate();
        int Balance = (int) userFirbase.getBalance();
        String string = Balance + "" ;

        Glide.with(getContext()).load(userFirbase.getProfile())
                .apply(new RequestOptions().error(R.drawable.ic_expert))
                .into(imageProfile);
        tvBalance.setText(string);
        if (rate.length() == 1){
            tvRate.setText(rate);
        }
        if (rate.length() == 3){
            tvRate.setText(rate.substring(0,3));
        }
        if (rate.length() >= 4){
            tvRate.setText(rate.substring(0,4));
        }
        tvUsername.setText(userFirbase.getUserName());


    }

//    private String enNumToAr(String val) {
//        char[] arabicChars = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};
//        StringBuilder builder = new StringBuilder();
//        for (
//                int i = 0; i < val.length(); i++) {
//            if (Character.isDigit(val.charAt(i))) {
//                builder.append(arabicChars[(int) (val.charAt(i)) - 48]);
//            } else {
//                builder.append(val.charAt(i));
//            }
//        }
//
//        String s = "" + builder.toString();
//        return s;
//    }
}
