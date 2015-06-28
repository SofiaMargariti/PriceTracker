package com.example.sofia.pricetracker;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.sofia.myfirstapp.R;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by sofia on 6/24/15.
 */
public class NotificationReceiverActivity extends Activity {
    private final String SKU_PRODUCTS = "com.example.sofia.myfirstapp.sku_products";

    private String sku_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        this.sku_id = intent.getStringExtra("id");
    }

    private class GetProducts extends AsyncTask<String, String, String> {
        private static final String baseSkuProductsUrl =
                "https://api.skroutz.gr/skus/%s/products?order_by=pricevat";

        @Override
        protected String doInBackground(String... params){
            OkHttpClient client = ((MainApp) getApplication()).getHttpClient();
            String url = String.format(baseSkuProductsUrl, sku_id);
            try {
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e){
                System.out.println(e.getMessage());
            }
            return "";
        }

        @Override
        protected void onPostExecute(String responseBody) {
            Intent intent = new Intent(getApplicationContext(), SingleResultActivity.class);

            intent.putExtra(SKU_PRODUCTS, responseBody);

            startActivity(intent);
        }
    }
}