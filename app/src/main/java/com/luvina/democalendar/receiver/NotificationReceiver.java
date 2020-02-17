package com.luvina.democalendar.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.luvina.democalendar.R;
import com.luvina.democalendar.activity.MainActivity;
import com.luvina.democalendar.utils.Constant;


/**
 * Class handling push notification
 */
public class NotificationReceiver extends BroadcastReceiver {

    /**
     * Create push notification when the event occurs
     *
     * @param context: Context
     * @param intent:  Intent
     * @author HoangNN
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Initialize an intent
        Intent notificationIntent = new Intent(context, MainActivity.class);
        int requestID = (int) System.currentTimeMillis();
        // Initialize a pending intent
        PendingIntent contentIntent = PendingIntent.getActivity(context, requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Declare Notification.Builder to build a notification
        Notification.Builder notificationBuilder;
        // If version SDK >= 26, create a channel to make a notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String chanelId = String.valueOf(requestID);
            String name = "MCalendar";
            String description = "MCalendar";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel chanel = new NotificationChannel(chanelId, name, importance);
            chanel.setDescription(description);
            notificationManager.createNotificationChannel(chanel);
            notificationBuilder = new Notification.Builder(context, chanelId);
        } else {
            notificationBuilder = new Notification.Builder(context);
        }
        // Set title, icon, content for notification
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(intent.getStringExtra(Constant.EVENT_NAME))
                .setContentText(intent.getStringExtra(Constant.START_TIME) + " - " + intent.getStringExtra(Constant.END_TIME))
                .setAutoCancel(true)
                .setContentIntent(contentIntent);
        // Show notification
        notificationManager.notify(requestID, notificationBuilder.build());
    }
}
