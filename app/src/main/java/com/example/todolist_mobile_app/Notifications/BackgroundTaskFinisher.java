package com.example.todolist_mobile_app.Notifications;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.example.todolist_mobile_app.Data.TaskData;
import com.example.todolist_mobile_app.Database.DatabaseManager;

public class BackgroundTaskFinisher extends IntentService {

    public BackgroundTaskFinisher() {
        super("Finish task");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            int notificationId = intent.getIntExtra("notification_id", -1);
            if (notificationId != -1) {
                DatabaseManager.initDatabase(getApplicationContext());
                TaskData data = DatabaseManager.getTaskById(notificationId);
                data.setFinished(true);
                DatabaseManager.insert(data);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(notificationId);
            }
        }
    }
}
