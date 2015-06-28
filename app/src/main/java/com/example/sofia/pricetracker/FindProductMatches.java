package com.example.sofia.pricetracker;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by sofia on 6/26/15.
 */
public class FindProductMatches extends AsyncTask<String, String, String> {
    private final static String baseSearchUrl = "https://api.skroutz.gr/search?q=%s";
    private final static String baseFilteredCategoryUrl = "https://api.skroutz.gr/categories/%s/skus?q=%s";
    OkHttpClient client;
    Context context;

    public FindProductMatches(Context context){ this.context = context; }

    private JSONArray getMatchingSkusForCategory(String category_id, String query){
        JSONArray skus = new JSONArray();
        int limit = 1;
        String url = String.format(baseFilteredCategoryUrl, category_id, query);
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            String body = response.body().string();
            JSONObject obj = new JSONObject(body);
            JSONArray all = obj.getJSONArray("skus");
            for (int i=0; i < limit; i++){
                skus.put(i, all.getJSONObject(i));
            }
            return skus;
        } catch (IOException | JSONException e){
            System.out.println(e.getMessage());
        }
        return skus;
    }
    @Override
    protected String doInBackground(String... params){
        android.os.Debug.waitForDebugger();
        String result = "";
        String query = params[0];

        String searchUrl = String.format(baseSearchUrl, query);
        client = ((MainApp) context.getApplicationContext()).getHttpClient();

        try {
            Request request = new Request.Builder()
                    .url(searchUrl)
                    .build();

            Response response = client.newCall(request).execute();
            String body = response.body().string();
            JSONObject obj = new JSONObject(body);
            JSONArray categories = obj.getJSONArray("categories");
            JSONArray skus = new JSONArray();
            int cnt = 0;
            for (int i = 0; i < categories.length(); i++){
                JSONObject category = categories.getJSONObject(i);
                if (category.getInt("children_count") == 0) { // we are only interested in leaf categories since only those have skus
                    String category_id = category.getString("id");
                    JSONArray catSkus = getMatchingSkusForCategory(category_id, query);
                    for (int j=0; j < catSkus.length(); j++){
                        skus.put(catSkus.getJSONObject(j));
                    }
                    cnt++;
                }
                if (cnt >= 10) break;
            }

            return skus.toString();

        } catch (IOException | JSONException e){
            System.out.println(e.getMessage());
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result){
        android.os.Debug.waitForDebugger();
        Intent intent = new Intent(context, ResultActivity.class);
        intent.putExtra("json", result);
        context.startActivity(intent);
    }
}
