package com.example.todolist_mobile_app.Recycler;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
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
