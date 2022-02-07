/*
package com.agri.chattla.ui.addConsult;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import com.google.android.gms.location.LocationResult;

public class MyLocationService extends BroadcastReceiver {

    public static final String ACTION_PROCESS_UPDATE = "com.agri.chattla.ui.addConsult.UPDATE_LOCATION" ;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null ){
            final String action = intent.getAction();
            if (ACTION_PROCESS_UPDATE.equals(action)){
                try {
                    LocationResult result = LocationResult.extractResult(intent);
                    if (result != null && result.getLastLocation() != null){
                        Location location = result.getLastLocation();
                        String Time = new java.text.SimpleDateFormat("HH:mm dd/MM").format(location.getTime()).toLowerCase();
                        AddConsultActivity.getInstance().setLocation(location.getLatitude()+"" , location.getLongitude()+"" , Time );
                    }
                }catch (Exception e){
                    Log.e("Error ..  " , e+"");
                    AddConsultActivity.getInstance().setLocation("Error" , "Error" , "Error");
                }
            }
        }

    }
}
*/
