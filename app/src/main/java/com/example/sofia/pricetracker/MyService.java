package com.example.sofia.pricetracker;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    private static final String TAG = "MyService";
    private Integer DELAY = 10 * 60 * 1000; // run every 10 minutes

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStart");
        final Handler handler = new Handler();

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        new PriceTracker(getApplicationContext()).execute();
                    }
                });
            }
        };
        timer.schedule(task, 0, DELAY);

        return START_NOT_STICKY;
    }

}
