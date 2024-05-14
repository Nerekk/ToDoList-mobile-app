package com.example.todolist_mobile_app;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist_mobile_app.Database.DatabaseManager;
import com.example.todolist_mobile_app.Database.TaskDatabase;
import com.example.todolist_mobile_app.Recycler.RecyclerViewManager;
import com.example.todolist_mobile_app.Recycler.TaskData;

public class MainActivity extends AppCompatActivity {
    public TaskDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        db = DatabaseManager.initDatabase(this);
        new RecyclerViewManager(this);
    }


}