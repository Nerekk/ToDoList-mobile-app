package com.example.todolist_mobile_app.RecyclerAttachment;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist_mobile_app.Data.FileModel;
import com.example.todolist_mobile_app.Data.TaskData;
import com.example.todolist_mobile_app.Database.DatabaseManager;
import com.example.todolist_mobile_app.MainActivity;
import com.example.todolist_mobile_app.R;
import com.example.todolist_mobile_app.Utils.FileManager;

import java.util.List;

public class RecyclerViewAttachmentManager {
    List<FileModel> files;
    RecyclerView recyclerView;
    private AttachmentListAdapter adapter;
    MainActivity activity;
    FileManager fm;
    private int taskId;

    public RecyclerViewAttachmentManager(MainActivity activity, FileManager fm, int taskId) {
        this.activity = activity;
        this.taskId = taskId;
        this.fm = fm;

//        recyclerView = activity.findViewById(R.id.rvAttachment);
//        files = fm.getAllFilesForTask(taskId);
//        adapter = new AttachmentListAdapter(files, activity);
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
//        recyclerView.setAdapter(adapter);
//        prepareSwipe();
    }

    public void setFiles(List<FileModel> files) {
        this.files = files;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void setAdapter(AttachmentListAdapter adapter) {
        this.adapter = adapter;
    }

    public void setRvManager(RecyclerView recyclerView, AttachmentListAdapter adapter, List<FileModel> files) {
        setRecyclerView(recyclerView);
        setAdapter(adapter);
        setFiles(files);
    }

    public void updateData(List<FileModel> models) {
        Log.i("ADD_TEST", "MY MODEL: " + models);
        files = models;
        adapter.setFiles(files);
        adapter.notifyDataSetChanged();
    }

    private void notifyAdapter() {
        adapter.setFiles(files);
        adapter.notifyDataSetChanged();
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

                int vhId = ((AttachmentViewHolder)viewHolder).getId();
                String filename = files.get(vhId).getFileName();
                fm.deleteAttachmentForTask(taskId, filename);
                files.remove(vhId);
                if (files.isEmpty()) {
                    updateHasFileStatusToFalse();
                }
                notifyAdapter();
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

    private void updateHasFileStatusToFalse() {
        TaskData data = DatabaseManager.getTaskById(taskId);
        data.setHasFiles(false);
        DatabaseManager.insert(data);
        activity.getRvManager().getDataFromDBAndUpdateAdapter();
    }

}
