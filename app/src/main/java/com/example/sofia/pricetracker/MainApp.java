package com.example.sofia.pricetracker;

import android.app.Application;

import com.squareup.okhttp.OkHttpClient;

import java.util.HashMap;

/**
 * Created by sofia on 6/26/15.
 */
public class MainApp extends Application {

    private OkHttpClient httpClient;
    private HashMap<String, HashMap<String, String>> tracked;

    @Override
    public void onCreate(){
        super.onCreate();
        this.httpClient = new OkHttpClient();
        this.httpClient.interceptors().add(new HttpInterceptor(this));
        this.tracked = new HashMap<>();
    }

    public OkHttpClient getHttpClient(){
        return httpClient;
    }

    public HashMap getTracked(){
        return tracked;
    }
}
