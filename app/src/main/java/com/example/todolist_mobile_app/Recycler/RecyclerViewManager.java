package com.example.todolist_mobile_app.Recycler;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist_mobile_app.Category;
import com.example.todolist_mobile_app.Database.DatabaseManager;
import com.example.todolist_mobile_app.MainActivity;
import com.example.todolist_mobile_app.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewManager {
    List<TaskData> tasks;
    RecyclerView recyclerView;
    TaskListAdapter adapter;
    MainActivity activity;

    public RecyclerViewManager(MainActivity activity) {
        this.activity = activity;
        DatabaseManager.getAll(list -> {
            tasks = list;
            recyclerView = activity.findViewById(R.id.recyclerView);
            adapter = new TaskListAdapter(tasks, activity.getApplication());
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
            recyclerView.setAdapter(adapter);
        });
//        tasks = getExampleData();
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
