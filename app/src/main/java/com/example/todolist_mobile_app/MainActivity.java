package com.example.todolist_mobile_app;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist_mobile_app.Recycler.TaskData;
import com.example.todolist_mobile_app.Recycler.TaskListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<TaskData> tasks;
    RecyclerView recyclerView;
    TaskListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        tasks = getExampleData();
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new TaskListAdapter(tasks, getApplication());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private List<TaskData> getExampleData() {
        List<TaskData> data = new ArrayList<>();
        data.add(new TaskData("Test1", "Description", false, Category.Home));
        data.add(new TaskData("TESTUJEMY", "Description 1245521", true, Category.Recreation));
        data.add(new TaskData("DLUGI DLUGI TEKST", "Descrip", false, Category.Other));
        data.add(new TaskData("Zadanie", "Description dapsodkap", true, Category.Health));

        return data;
    }
}