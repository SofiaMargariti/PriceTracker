package com.example.sofia.pricetracker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.sofia.myfirstapp.R;

/**
 * Created by sofia on 6/24/15.
 */
public class NotificationHelper {
    private Context context;
    String message, id;

    public NotificationHelper(Context context){
        this.context = context;
    }

    public void createNotification(String message, String id) {
        // Prepare intent which is triggered if the
        // notification is selected
        int nid = 1;
        Intent intent = new Intent(context, NotificationReceiverActivity.class);
        intent.putExtra("sku_id", id);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Build notification
        // Actions are just fake
        Notification.Builder mBuilder =
                new Notification.Builder(context)
                        .setSmallIcon(R.drawable.skrooge)
                        .setContentTitle("Price tracker")
                        .setContentText(message)
                        .setContentIntent(pIntent)
                        .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(nid, mBuilder.build());

    }
}
