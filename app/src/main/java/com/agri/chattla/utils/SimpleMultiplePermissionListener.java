package com.agri.chattla.utils;


import com.agri.chattla.ui.base.BaseActivity;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class SimpleMultiplePermissionListener implements MultiplePermissionsListener {

    private final BaseActivity baseActivity;

    public SimpleMultiplePermissionListener(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    @Override
    public void onPermissionsChecked(MultiplePermissionsReport report) {

//        for (PermissionGrantedResponse response : report.getGrantedPermissionResponses()) {
//            baseActivity.showPermissionGranted(response.getPermissionName());
//        }
//        for (PermissionDeniedResponse response : report.getDeniedPermissionResponses()) {
//            baseActivity.showPermissionDenied(response.getPermissionName());
//        }

    }

    @Override
    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

        baseActivity.showPermissionRational(token);
    }
}
