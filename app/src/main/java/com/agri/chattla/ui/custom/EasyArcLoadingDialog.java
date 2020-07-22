package com.agri.chattla.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.WindowManager;

import com.agri.chattla.R;


public class EasyArcLoadingDialog extends Dialog {


    public EasyArcLoadingDialog(Context context) {
        super(context);
        this.init(context);
    }


    public EasyArcLoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.init(context);
    }


    protected EasyArcLoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.init(context);
    }


    private void init(Context context) {
        this.setContentView(R.layout.easy_arc_loading_dialog);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setCancelable(false);
        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
    }
}
