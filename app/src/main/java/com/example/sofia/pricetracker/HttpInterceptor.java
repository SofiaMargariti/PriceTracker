package com.example.sofia.pricetracker;

import android.content.Context;
import android.content.SharedPreferences;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;


public class HttpInterceptor implements Interceptor {

    private final Context context;
    private final static Object lock = new Object();
    private SharedPreferences prefs;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public HttpInterceptor(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences("com.example.sofia.myfirstapp", Context.MODE_PRIVATE);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final String tokenKey = "com.example.sofia.myfirstapp.token";
        final String expiresKey = "com.example.sofia.myfirstapp.expires";

        Request request = chain.request();
        if (request.urlString().contains("oauth2")) {
            Response response = chain.proceed(request);
            return response;
        }

        //Build new request
        Request.Builder builder = request.newBuilder();
        builder.header("Accept", "application/vnd.skroutz+json; version=3");

        String token = prefs.getString(tokenKey, null);
        // Long expires = prefs.getLong(expiresKey, new Date().getTime() / 1000L);
        setAuthHeader(builder, token); //write current token to request

        request = builder.build(); //overwrite old request
        Response response = chain.proceed(request); //perform request, here original request will be executed

        if (response.code() == 401) { //if unauthorized
            synchronized (lock) { //perform all 401 in sync blocks, to avoid multiply token updates
                String currentToken = prefs.getString(tokenKey, null); //get currently stored token

                int code = refresh() / 100; //refresh token
                if (code != 2) { //if refresh token failed for some reason
                    if (code == 4) //only if response is 400, 500 might mean that token was not updated
                        return response; //if token refresh failed - show error to user
                }

                if (prefs.getString(tokenKey, null) != null) { //retry requires new auth token,
                    setAuthHeader(builder, prefs.getString(tokenKey, null)); //set auth token to updated
                    request = builder.build();
                    return chain.proceed(request); //repeat request with new token
                }
            }
        }

        return response;
    }

    private void setAuthHeader(Request.Builder builder, String token) {
        if (token != null) //Add Auth token to each request if authorized
            builder.header("Authorization", String.format("Bearer %s", token));
    }


    private int refresh() {
        return new TokenHelper(context).refreshToken();
    }
}
