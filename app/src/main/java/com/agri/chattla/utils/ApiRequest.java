package com.agri.chattla.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONException;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class ApiRequest {
    private static ApiRequest firstInstance = null;
    private static boolean firstThread = false;
    final OkHttpClient okHttpClient;
    private AlertDialog waitingAlertDialog;

    public ApiRequest(Context context) {
        okHttpClient = getUnsafeOkHttpClient();
        AndroidNetworking.initialize(context, okHttpClient);
    }

    public static ApiRequest getInstance(Context context) {
        if (firstInstance == null) {
            if (firstThread) {
                Thread.currentThread();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            firstInstance = new ApiRequest(context);
        }
        return firstInstance;
    }


    public static void sendNotification(String object, final ServiceCallback callback) {
        AndroidNetworking.post("https://fcm.googleapis.com/fcm/send")
                .addStringBody(object)
                .setPriority(Priority.HIGH)
                .addHeaders("Authorization", "key=" + AppConstants.serverKey)
                .setContentType("application/json")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("notificationResponse",response);
                        try {
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("notificationErrResponse",anError.getErrorBody());

                        try {
                            callback.onFail(anError);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * @param url      constant url of service
     * @param callback callback of service if you removed the model replace it with string
     */
    public void createGetRequest(String url,  final ServiceCallback callback) {
        AndroidNetworking.get(url)
                .setPriority(Priority.IMMEDIATE)
                .build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                try {
                    callback.onSuccess(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                try {
                    callback.onFail(anError);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            //builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier((hostname, session) -> true);

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public interface ServiceCallback<T> {
        void onSuccess(T response) throws JSONException;

        void onFail(ANError error) throws JSONException;
    }

    public interface UploadProgress {
        void onProgress(int percent);
    }

    public AlertDialog getWaitingAlertDialog() {
        return waitingAlertDialog;
    }

    public void setWaitingAlertDialog(AlertDialog waitingAlertDialog) {
        this.waitingAlertDialog = waitingAlertDialog;
    }
}
