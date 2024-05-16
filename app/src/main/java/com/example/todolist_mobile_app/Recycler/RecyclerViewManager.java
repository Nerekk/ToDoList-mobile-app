package com.example.todolist_mobile_app.Recycler;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist_mobile_app.Enums.Categories;
import com.example.todolist_mobile_app.Data.TaskData;
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
    private String lastQuery;
    private String lastCategory;

    public RecyclerViewManager(MainActivity activity) {
        this.activity = activity;
        recyclerView = activity.findViewById(R.id.recyclerView);
        this.lastQuery = "";
        this.lastCategory = "All";
        tasks = DatabaseManager.getAll();
        adapter = new TaskListAdapter(tasks, activity.getApplication());
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);

        prepareFloatingActionButton();
    }

    private void prepareFloatingActionButton() {
        activity.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void getDataFromDB() {
        tasks = DatabaseManager.getAll();
        adapter.setTasks(tasks);
        adapter.filter(lastCategory, lastQuery);
    }

    public void setLastQuery(String lastQuery) {
        this.lastQuery = lastQuery;
    }

    public void setLastCategory(String lastCategory) {
        this.lastCategory = lastCategory;
    }

    public void filterData(String category) {
        adapter.filter(category, lastQuery);
    }

}
