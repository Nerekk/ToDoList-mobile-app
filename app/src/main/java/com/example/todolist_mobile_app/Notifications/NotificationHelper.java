package com.example.todolist_mobile_app.Notifications;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.todolist_mobile_app.MainActivity;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class NotificationHelper {
    MainActivity activity;

    public NotificationHelper(MainActivity activity) {
        this.activity = activity;
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "TodoListReminderChannel";
            String desc = "Channel for my todo app";

            int importance = android.app.NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("todoAppId", name, importance);
            channel.setDescription(desc);

            android.app.NotificationManager notificationManager = activity.getSystemService(android.app.NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void scheduleNotification(LocalDateTime dateTime, int minutesBefore, String taskMessage, int notificationId) {
        long notificationTimeInMillis = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - (minutesBefore * 60 * 1000);

        Intent intent = new Intent(activity, ReminderBroadcast.class);
        intent.putExtra("notification_message", taskMessage);
        intent.putExtra("notification_id", notificationId);
        Log.i("MY_TEST", "PUTTING MSG: " + taskMessage + " ID: " + notificationId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, notificationTimeInMillis, pendingIntent);
        }
        Toast.makeText(activity, "Alarm set", Toast.LENGTH_LONG).show();
        Log.i("MY_TEST", "SCHEDULED FOR ID: " + notificationId);
    }

    public void cancelNotification(int notificationId) {
        Intent intent = new Intent(activity, ReminderBroadcast.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        android.app.NotificationManager notificationManager = (android.app.NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(notificationId);
        }
        Log.i("MY_TEST", "CANCELED FOR ID: " + notificationId);
    }
}
