package com.example.todolist_mobile_app;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.todolist_mobile_app.AddingActivity.AddTaskActivity;
import com.example.todolist_mobile_app.Database.DatabaseManager;
import com.example.todolist_mobile_app.Database.TaskDatabase;
import com.example.todolist_mobile_app.Enums.Categories;
import com.example.todolist_mobile_app.Recycler.RecyclerViewManager;

public class MainActivity extends AppCompatActivity {
    private TaskDatabase db;
    private RecyclerViewManager rvManager;
    private ToolbarManager toolbarManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        db = DatabaseManager.initDatabase(this);
        rvManager = new RecyclerViewManager(this);

        findViewById(R.id.fab).setOnClickListener(this::goToAddTaskActivity);

        toolbarManager = new ToolbarManager(this, rvManager);
        Log.i("ONCREATE", "ONCREATE");
    }

    public void goToAddTaskActivity(View view) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        rvManager.getDataFromDBAndUpdateAdapter();
    }
}