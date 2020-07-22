package com.agri.chattla.utils;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FcmNotifier {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void sendNotification(final String body, final String title, final String key) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("text", body);
                    dataJson.put("title", title);
                    dataJson.put("priority", "high");
                    json.put("notification", dataJson);
                    json.put("to", key);
                    RequestBody body = RequestBody.create(json.toString(), JSON);
                    Request request = new Request.Builder()
                            .header("Authorization", "key="+AppConstants.serverKey)
                            .header("Content-Type","application/json")
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                    Log.e("finalResponse", finalResponse);
                } catch (Exception e) {

                    Log.i("finalResponse", e.getMessage());
                }
                return null;
            }
        }.execute();

    }

}
