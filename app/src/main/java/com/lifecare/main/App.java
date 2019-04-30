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

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        RemoteViews collapsedView = new RemoteViews(getPackageName(),
                R.layout.notification);

        Intent clickIntent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(this,
                0, clickIntent, 0);

        collapsedView.setOnClickPendingIntent(R.id.backgroundNotification, clickPendingIntent);

        Notification notification = new NotificationCompat.Builder(this, "0")
                .setSmallIcon(R.drawable.ic_menu_send)
                .setOngoing(true)
                .setCustomContentView(collapsedView)
                .build();

        notificationManager.notify(0, notification);
    }

}
