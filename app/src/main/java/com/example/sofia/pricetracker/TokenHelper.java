package com.example.sofia.pricetracker;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.sofia.myfirstapp.R;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by sofia on 6/28/15.
 */
public class TokenHelper {
    Context context;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public TokenHelper(Context context){
        this.context = context;
    }
    private String buildQueryString(HashMap<String, String> params){
        String query = "";
        Iterator it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            query += pair.getKey() + "=" + pair.getValue();
            if (it.hasNext())
                query += "&";
        }
        return query;
    }

    public int refreshToken() {
        //Refresh token, synchronously, save it, and return result code
        //you might use retrofit here
        String client_id = context.getString(R.string.client_id);
        String client_secret = context.getString(R.string.client_secret);
        String redirect_uri = context.getString(R.string.redirect_uri);
        final String baseAuthUrl = "https://www.skroutz.gr/oauth2/token";
        SharedPreferences prefs = context.getSharedPreferences("com.example.sofia.myfirstapp", Context.MODE_PRIVATE);

        HashMap<String, String> qparams = new HashMap<String, String>();
        qparams.put("client_id", client_id);
        qparams.put("client_secret", client_secret);
        qparams.put("grant_type", "client_credentials");
        qparams.put("scope", "public");
        qparams.put("redirect_uri", redirect_uri);

        String authUrl = baseAuthUrl + "?" + buildQueryString(qparams);

        RequestBody emptyBody = RequestBody.create(JSON, "");
        Request request = new Request.Builder()
                .url(authUrl)
                .post(emptyBody)
                .build();

        MainApp app = (MainApp) context.getApplicationContext();
        OkHttpClient client = app.getHttpClient();
        try {
            Response response = client.newCall(request).execute();
            String body = response.body().string();
            JSONObject obj = new JSONObject(body);
            long now = new Date().getTime() / 1000L;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("com.example.sofia.myfirstapp.token", obj.getString("access_token"));
            editor.putLong("com.example.sofia.myfirstapp.expires", now + obj.getLong("expires_in"));
            editor.apply();
            return response.code();
        } catch (IOException | JSONException e) {
            System.out.println(e.getMessage());
        }

        return 400;
    }
}
