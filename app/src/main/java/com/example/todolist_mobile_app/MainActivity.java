package com.example.todolist_mobile_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist_mobile_app.AddingActivity.AddTaskActivity;
import com.example.todolist_mobile_app.Database.DatabaseManager;
import com.example.todolist_mobile_app.Database.TaskDatabase;
import com.example.todolist_mobile_app.Recycler.RecyclerViewManager;

public class MainActivity extends AppCompatActivity {
    public TaskDatabase db;
    public RecyclerViewManager rvManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        db = DatabaseManager.initDatabase(this);
        new RecyclerViewManager(this);

        findViewById(R.id.fab).setOnClickListener(this::goToAddTaskActivity);
    }

    public void goToAddTaskActivity(View view) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        startActivity(intent);
    }


}