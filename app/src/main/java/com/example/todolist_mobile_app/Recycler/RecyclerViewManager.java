package com.example.todolist_mobile_app.Recycler;

import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist_mobile_app.Data.TaskData;
import com.example.todolist_mobile_app.Database.DatabaseManager;
import com.example.todolist_mobile_app.MainActivity;
import com.example.todolist_mobile_app.R;

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
        prepareSwipe();
    }

    private void prepareFloatingActionButton() {
        activity.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void prepareSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(activity, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
//                Toast.makeText(activity, "on Swiped " + ((TaskViewHolder)viewHolder).getTaskId(), Toast.LENGTH_SHORT).show();
                Toast.makeText(activity, "Task deleted", Toast.LENGTH_SHORT).show();
                DatabaseManager.deleteById(((TaskViewHolder)viewHolder).getTaskId());
                getDataFromDBAndUpdateAdapter();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void getDataFromDBAndUpdateAdapter() {
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
