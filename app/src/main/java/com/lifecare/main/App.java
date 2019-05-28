package com.lifecare.main;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.RemoteViews;

import com.lifecare.main.Broadcast.MyBroadcastReceiver;

public class App extends Application {

    public static boolean NOTIFICATION_DISPLAYED = true;

    static NotificationManagerCompat notificationManager;
    static Notification notification;

    public static void notificationDisplay() {
        notificationManager.notify(0, notification);
    }

    public static void notificationNotDisplay() {
        notificationManager.cancel(0);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "0",
                    "LifeCare Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        notificationManager = NotificationManagerCompat.from(this);

        RemoteViews collapsedView = new RemoteViews(getPackageName(),
                R.layout.notification);

        Intent clickIntent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(this,
                0, clickIntent, 0);

        collapsedView.setOnClickPendingIntent(R.id.backgroundNotification, clickPendingIntent);

        notification = new NotificationCompat.Builder(this, "0")
                .setSmallIcon(R.drawable.ic_heart_with_electrocardiogram)
                .setOngoing(true)
                .setCustomContentView(collapsedView)
                .build();

        if (NOTIFICATION_DISPLAYED) {
            notificationDisplay();
        } else {
            notificationNotDisplay();
        }
    }
}
