package com.example.todolist_mobile_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.todolist_mobile_app.AddingActivity.AddTaskActivity;
import com.example.todolist_mobile_app.Data.TaskData;
import com.example.todolist_mobile_app.Database.DatabaseManager;
import com.example.todolist_mobile_app.Notifications.NotificationHelper;
import com.example.todolist_mobile_app.Recycler.RecyclerViewManager;

public class MainActivity extends AppCompatActivity {
    private RecyclerViewManager rvManager;
    private ToolbarManager toolbarManager;
    private NotificationHelper notificationHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        notificationHelper = new NotificationHelper(this);

        DatabaseManager.initDatabase(this);

        rvManager = new RecyclerViewManager(this);

        findViewById(R.id.fab).setOnClickListener(this::goToAddTaskActivity);

        toolbarManager = new ToolbarManager(this, rvManager);

        Log.i("MY_TEST", "ONCREATE");
    }



    public void goToAddTaskActivity(View view) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        rvManager.getDataFromDBAndUpdateAdapter();
        Log.i("MY_TEST", "ONRESUME");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.i("MY_TEST", "HALO");
        if (requestCode == 1 && resultCode == RESULT_OK && intent != null) {
            handleNotification(intent);
        }
    }

    private void handleNotification(Intent intent) {
        int notifyId = intent.getIntExtra(TaskData.NOTIFY_ID, -1);
        int notifyOperation = intent.getIntExtra(TaskData.NOTIFY_OPERATION, -1);
        if (notifyOperation != -1) {
            Log.i("MY_TEST", "GOT NOTIFY ID: " + notifyId + " OP: " + notifyOperation);
            TaskData data = DatabaseManager.getTaskById(notifyId);
            Log.i("MY_TEST", "TAKING DATA: " + data.getTitle());

            switch (notifyOperation) {
                case 0:
                    Log.i("MY_TEST", "CASE 0");
                    notificationHelper.cancelNotification(notifyId);
                    break;
                case 1:
                    Log.i("MY_TEST", "CASE 1");
                    notificationHelper.cancelNotification(notifyId);
                    Log.i("MY_TEST", "CASE 1 AFTER CANCEL");
                    notificationHelper.scheduleNotification(data.getEndTime(), data.getNotification().getValue(), data.getTitle(), data.getId());
                    break;
            }
        }
    }




}