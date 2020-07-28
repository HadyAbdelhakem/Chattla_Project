package com.agri.chattla.ui.farmerMain;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.androidnetworking.error.ANError;
import com.agri.chattla.utils.ApiRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import static com.agri.chattla.utils.AppConstants.paymentStatusUrl;

public class ConsultStatusViewModel extends ViewModel {

    MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> mutableLiveError = new MutableLiveData<>();

    ApiRequest apiRequest;
    URL url;

    public void checkConsultStatus(Context context, String merchantCode, String merchantRefNumber, String signature) {

        Uri builtUri = Uri.parse(paymentStatusUrl)
                .buildUpon()
                .appendQueryParameter("merchantRefNumber", merchantRefNumber)
                .appendQueryParameter("signature", signature)
                .build();
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        apiRequest = ApiRequest.getInstance(context);
        apiRequest.createGetRequest(url.toString(), new ApiRequest.ServiceCallback<String>() {

            @Override
            public void onSuccess(String response) throws JSONException {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.has("paymentStatus")){
                        Log.e("response", response);
                        mutableLiveData.setValue(object.getString("paymentStatus"));

                    }else{
                        mutableLiveData.setValue("hasNoPaymentStatus");
                    }

                } catch (JSONException e) {
                    Log.e("response", ""+e.getMessage());
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(ANError error) throws JSONException {
                mutableLiveError = new MutableLiveData<>();
                mutableLiveError.setValue(error.getMessage());
            }
        });
    }
}
