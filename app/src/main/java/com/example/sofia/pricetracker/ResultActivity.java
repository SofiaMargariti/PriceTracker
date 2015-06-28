package com.example.sofia.pricetracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.sofia.myfirstapp.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;


public class ResultActivity extends Activity {

    HashMap tracked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tracked = ((MainApp) this.getApplication()).getTracked();

        final ListView listview = (ListView) findViewById(R.id.listView);

        JSONArray skus = new JSONArray();

        try {
            skus = new JSONArray(getIntent().getStringExtra("json"));
        } catch (JSONException e){
            System.out.println(e.getMessage());
        }

        final JSONAdapter adapter = new JSONAdapter(this, skus);
        listview.setAdapter(adapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}