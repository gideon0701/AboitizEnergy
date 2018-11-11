package com.pentagon.test6;

import android.util.Log;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;
    private final static String API_URL = "http://192.168.168.253:3333/api/image/";

    static Retrofit getClient() {
        try{
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.MINUTES)
                    .writeTimeout(10, TimeUnit.MINUTES)
                    .readTimeout(10, TimeUnit.MINUTES)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(MessageFormat.format("{0}",API_URL))
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();

        }catch(Exception e) {
            Log.e("ERROR", e.toString());
        }

        return retrofit;
    }
}
