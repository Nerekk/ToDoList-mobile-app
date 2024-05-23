package com.example.todolist_mobile_app.Notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.todolist_mobile_app.MainActivity;
import com.example.todolist_mobile_app.R;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String channelId = "todoAppId";
        String message = intent.getStringExtra("notification_message");
        int notificationId = intent.getIntExtra("notification_id", -1);
        Log.i("MY_TEST", "onReceive: " + message + " id:" + notificationId);

        if (notificationId == -1) return;


        PendingIntent pendingIntent = createActivityIntent(context, notificationId);
        PendingIntent backgroundPendingIntent = createBackgroundTaskIntent(context, notificationId);

        NotificationCompat.Builder builder = buildNotification(context, channelId, message, pendingIntent, backgroundPendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, builder.build());
    }

    @NonNull
    private static NotificationCompat.Builder buildNotification(Context context, String channelId, String message, PendingIntent pendingIntent, PendingIntent backgroundPendingIntent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_done)
                .setContentTitle("Task reminder")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .addAction(0, "Finish task", backgroundPendingIntent);
        return builder;
    }

    private static PendingIntent createBackgroundTaskIntent(Context context, int notificationId) {
        Intent backgroundIntent = new Intent(context, BackgroundTaskFinisher.class);
        backgroundIntent.putExtra("notification_id", notificationId);
        PendingIntent backgroundPendingIntent = PendingIntent.getService(context, notificationId, backgroundIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        return backgroundPendingIntent;
    }

    private static PendingIntent createActivityIntent(Context context, int notificationId) {
        Intent activityIntent = new Intent(context, MainActivity.class);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activityIntent.putExtra("opentask", notificationId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationId, activityIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        return pendingIntent;
    }

}
