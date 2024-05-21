package com.example.todolist_mobile_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;


import com.example.todolist_mobile_app.AddingActivity.AddTaskActivity;
import com.example.todolist_mobile_app.Data.TaskData;
import com.example.todolist_mobile_app.Database.DatabaseManager;
import com.example.todolist_mobile_app.Dialogs.DialogInfoFragment;
import com.example.todolist_mobile_app.Notifications.NotificationHelper;
import com.example.todolist_mobile_app.Recycler.RecyclerViewManager;
import com.example.todolist_mobile_app.Utils.FileManager;

public class MainActivity extends AppCompatActivity {
    private RecyclerViewManager rvManager;
    private ToolbarManager toolbarManager;
    private NotificationHelper notificationHelper;
    private FileManager fm;

    public static final int REQUEST_CODE_SELECT_FILES = 1;
    public static final int REQUEST_CODE_TASK_ACTIVITY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        notificationHelper = new NotificationHelper(this);

        DatabaseManager.initDatabase(this);
        Log.i("CHECKID", "ID: " + DatabaseManager.getFirstUnusedId());
        fm = new FileManager(this);
        rvManager = new RecyclerViewManager(this, fm);

        findViewById(R.id.fab).setOnClickListener(this::goToAddTaskActivity);

        toolbarManager = new ToolbarManager(this, rvManager);

        Log.i("MY_TEST", "ONCREATE");
    }

    public FileManager getFileManager() {
        return fm;
    }

    public NotificationHelper getNotificationHelper() {
        return notificationHelper;
    }

    public RecyclerViewManager getRvManager() {
        return rvManager;
    }

    public void goToAddTaskActivity(View view) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        startActivityForResult(intent, REQUEST_CODE_TASK_ACTIVITY);
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
        Log.i("MY_TEST", "HALO " + requestCode);
        if (requestCode == REQUEST_CODE_TASK_ACTIVITY && resultCode == RESULT_OK && intent != null) {
            handleNotification(intent);
        }

        DialogInfoFragment dialogFragment = getDialogInfoFragment();
        if (dialogFragment != null) {
            dialogFragment.onActivityResult(requestCode, resultCode, intent);
        }
    }

    @Nullable
    public DialogInfoFragment getDialogInfoFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogInfoFragment dialogFragment = (DialogInfoFragment) fragmentManager.findFragmentByTag("FilePickerDialogFragment");
        return dialogFragment;
    }

    private void handleNotification(Intent intent) {
        int notifyId = intent.getIntExtra(TaskData.NOTIFY_ID, -1);
        int notifyOperation = intent.getIntExtra(TaskData.NOTIFY_OPERATION, -1);
        Log.i("MY_TEST", "GOT NOTIFY ID: " + notifyId + " OP: " + notifyOperation);
        if (notifyOperation != -1) {
//            Log.i("MY_TEST", "GOT NOTIFY ID: " + notifyId + " OP: " + notifyOperation);
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