package com.example.sofia.pricetracker;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.sofia.myfirstapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

class JSONAdapter extends BaseAdapter implements ListAdapter {

    private final Activity activity;
    private final JSONArray jsonArray;
    final HashMap tracked;

    public JSONAdapter(Activity activity, JSONArray jsonArray) {
        assert activity != null;
        assert jsonArray != null;

        this.jsonArray = jsonArray;
        this.activity = activity;
        this.tracked = ((MainApp) activity.getApplication()).getTracked();
    }

    @Override
    public int getCount() {

        return jsonArray.length();
    }

    @Override
    public JSONObject getItem(int position) {

        return jsonArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        JSONObject jsonObject = getItem(position);

        return jsonObject.optLong("id");
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null)
            convertView = activity.getLayoutInflater().inflate(R.layout.list_item, null);

        TextView dname = (TextView) convertView.findViewById(R.id.display_name);
        Button track = (Button) convertView.findViewById(R.id.button2);

        final JSONObject jsonObject = getItem(position);
        try {
            dname.setText(jsonObject.getString("display_name"));
        } catch (JSONException e){
            e.printStackTrace();
        }

        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout row = (LinearLayout)v.getParent();
                Button btn = (Button)row.getChildAt(1);
                HashMap<String, String> sku = new HashMap<String, String>();
                String id;

                try {
                    id = jsonObject.getString("id");
                    if (btn.getText().toString().equals("Track")){
                        sku.put("display_name", jsonObject.getString("display_name"));
                        sku.put("url", jsonObject.getString("web_uri"));
                        sku.put("min_price", jsonObject.getString("price_min"));
                        tracked.put(id, sku);
                        btn.setText("Stop");
                    } else {
                        tracked.remove(id);
                        btn.setText("Track");
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }

            }
        });
        return convertView;
    }
}