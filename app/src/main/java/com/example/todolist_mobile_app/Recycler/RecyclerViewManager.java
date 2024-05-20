package com.example.todolist_mobile_app.Recycler;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist_mobile_app.Data.TaskData;
import com.example.todolist_mobile_app.Database.DatabaseManager;
import com.example.todolist_mobile_app.Enums.OrderType;
import com.example.todolist_mobile_app.Enums.TaskStatus;
import com.example.todolist_mobile_app.MainActivity;
import com.example.todolist_mobile_app.R;
import com.example.todolist_mobile_app.Utils.FileManager;

import java.util.List;

public class RecyclerViewManager {
    List<TaskData> tasks;
    RecyclerView recyclerView;
    TaskListAdapter adapter;
    MainActivity activity;
    FileManager fm;
    private String lastQuery;
    private String lastCategory, lastTaskStatus, lastOrderType;

    public RecyclerViewManager(MainActivity activity, FileManager fm) {
        this.activity = activity;
        this.fm = fm;
        recyclerView = activity.findViewById(R.id.recyclerView);
        this.lastQuery = "";
        this.lastCategory = "All";
        this.lastTaskStatus = TaskStatus.All.toString();
        this.lastOrderType = OrderType.Upcoming.toString();
        tasks = DatabaseManager.getAll();
        adapter = new TaskListAdapter(tasks, activity, activity.findViewById(R.id.mainNoRvData));

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);

        prepareSwipe();
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
                int taskid = ((TaskViewHolder)viewHolder).getTaskId();
                Toast.makeText(activity, "Task " + taskid + " deleted", Toast.LENGTH_SHORT).show();
                fm.deleteAllAttachmentsForTask(taskid);
                DatabaseManager.deleteById(taskid);
                getDataFromDBAndUpdateAdapter();
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;

                    Paint paint = new Paint();
                    paint.setColor(ContextCompat.getColor(activity, R.color.color_swipe)); // Replace with your desired color

                    if (dX < 0) {
                        c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                                (float) itemView.getRight(), (float) itemView.getBottom(), paint);
                    }

                     Drawable deleteIcon = ContextCompat.getDrawable(activity, R.drawable.ic_delete_48_white); // Your delete icon
                     int iconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                     int iconTop = itemView.getTop() + (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                     int iconBottom = iconTop + deleteIcon.getIntrinsicHeight();
                     if (dX < 0) {
                         int iconLeft = itemView.getRight() - iconMargin - deleteIcon.getIntrinsicWidth();
                         int iconRight = itemView.getRight() - iconMargin;
                         deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                         deleteIcon.draw(c);
                     } else if (dX > 0) {
                         int iconLeft = itemView.getLeft() + iconMargin;
                         int iconRight = itemView.getLeft() + iconMargin + deleteIcon.getIntrinsicWidth();
                         deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                         deleteIcon.draw(c);
                     }
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void getDataFromDBAndUpdateAdapter() {
        tasks = DatabaseManager.getAll();
        adapter.setTasks(tasks);
        adapter.filter(lastCategory, lastQuery, lastTaskStatus, lastOrderType);
    }

    public void setLastQuery(String lastQuery) {
        this.lastQuery = lastQuery;
    }

    public void setLastCategory(String lastCategory) {
        this.lastCategory = lastCategory;
    }

    public void setLastTaskStatus(String lastTaskStatus) {
        this.lastTaskStatus = lastTaskStatus;
    }

    public void setLastOrderType(String lastOrderType) {
        this.lastOrderType = lastOrderType;
    }

    public void filterDataOnCategoryChange(String category) {
        setLastCategory(category);
        adapter.filter(lastCategory, lastQuery, lastTaskStatus, lastOrderType);
    }

    public void filterDataOnQueryChange(String query) {
        setLastQuery(query);
        adapter.filter(lastCategory, lastQuery, lastTaskStatus, lastOrderType);
    }

    public void filterDataOnTaskStatusChange(String taskType) {
        setLastTaskStatus(taskType);
        adapter.filter(lastCategory, lastQuery, lastTaskStatus, lastOrderType);
    }

    public void filterDataOnOrderChange(String order) {
        setLastOrderType(order);
        adapter.filter(lastCategory, lastQuery, lastTaskStatus, lastOrderType);
    }

}
