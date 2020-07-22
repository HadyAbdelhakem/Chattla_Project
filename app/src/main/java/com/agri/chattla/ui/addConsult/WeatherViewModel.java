package com.agri.chattla.ui.addConsult;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.androidnetworking.error.ANError;
import com.agri.chattla.model.Weather;
import com.agri.chattla.utils.ApiRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import static com.agri.chattla.utils.AppConstants.WeatherKey;
import static com.agri.chattla.utils.AppConstants.WeatherUrl;

public class WeatherViewModel extends ViewModel {

    MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> mutableLiveError = new MutableLiveData<>();

    ApiRequest apiRequest;
    URL url;

    public void getWeatherInfo(Context context, String lat, String lon) {

        Uri builtUri = Uri.parse(WeatherUrl)
                .buildUpon()
                .appendQueryParameter("lat", lat)
                .appendQueryParameter("lon", lon)
                .appendQueryParameter("units", "metric")
                .appendQueryParameter("appid", WeatherKey)
                .appendQueryParameter("lang", "ar")
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
                Log.e("response", response);
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONObject main = jsonObj.getJSONObject("main");
                    JSONObject wind = jsonObj.getJSONObject("wind");
                    JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

                    String temp = main.getString("temp") + "°C";
                    String tempMin =  main.getString("temp_min") + "°C";
                    String tempMax =  main.getString("temp_max") + "°C";
                    String pressure = main.getString("pressure");
                    String humidity = main.getString("humidity");
                    String windSpeed = wind.getString("speed");
                    String weatherDescription = weather.getString("description");

                    Weather weather1=new Weather(temp,tempMin,tempMax,pressure,humidity,windSpeed,weatherDescription);
                    mutableLiveData.setValue(new Gson().toJson(weather1));

                } catch (JSONException e) {
                    Log.e("response", "" + e.getMessage());
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
