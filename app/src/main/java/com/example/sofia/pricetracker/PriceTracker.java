package com.example.sofia.pricetracker;

import android.content.Context;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class PriceTracker {

    private final String baseSkuUrl = "https://www.skroutz.com/sku/";
    private HashMap tracked;
    private NotificationHelper mNotificationHelper;
    private OkHttpClient client;

    public PriceTracker(Context context){
        this.tracked = ((MainApp) context).getTracked();;
        mNotificationHelper = new NotificationHelper(context);
        this.client = ( (MainApp) context ).getHttpClient();
    }

    protected String run (String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    protected void checkPrices() {
        Iterator it = tracked.entrySet().iterator();
        String result;
        Long minPrice, newMinPrice;
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            String sku_id = (String) pair.getKey();
            minPrice = (Long)((HashMap) pair.getValue()).get("min_price");
            String skuUrl = baseSkuUrl + sku_id;
            try {
                result = run(skuUrl);
                if (result != null){
                    JSONObject obj = new JSONObject(result);
                    newMinPrice = obj.getLong("min_price");
                    String display_name = obj.getString("display_name");
                    if (newMinPrice < minPrice){
                        String message = "New lower price " + newMinPrice + " found for " + display_name;
                        mNotificationHelper.createNotification(message, sku_id);
                    }
                }
            } catch (IOException | JSONException e){
                System.out.println(e.getMessage());
            }
        }
    }
}