package com.example.todolist_mobile_app.AddingActivity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist_mobile_app.Database.DatabaseManager;
import com.example.todolist_mobile_app.R;

public class AddTaskActivity extends AppCompatActivity {
    private AddTaskViewManager addTaskViewManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_task_add);

        DatabaseManager.initDatabase(this);
        addTaskViewManager = new AddTaskViewManager(this);
    }
}
