package com.example.todolist_mobile_app.Interfaces;

import com.example.todolist_mobile_app.Recycler.TaskData;

import java.util.List;

public interface DataCallback {
    void onDataLoaded(List<TaskData> tasks);
}
