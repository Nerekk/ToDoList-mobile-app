package com.example.todolist_mobile_app.Recycler;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist_mobile_app.R;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    TextView taskCategory, taskTitle, taskDate;
    ImageView taskStatus, taskNotification;

    public TaskViewHolder(@NonNull View itemView) {
        super(itemView);
        taskCategory = itemView.findViewById(R.id.taskCategory);
        taskTitle = itemView.findViewById(R.id.taskTitle);
        taskDate = itemView.findViewById(R.id.taskDate);

        taskStatus = itemView.findViewById(R.id.taskStatus);
        taskNotification = itemView.findViewById(R.id.taskNotification);
    }

    public TextView getTaskCategory() {
        return taskCategory;
    }

    public TextView getTaskTitle() {
        return taskTitle;
    }

    public TextView getTaskDate() {
        return taskDate;
    }

    public ImageView getTaskStatus() {
        return taskStatus;
    }

    public ImageView getTaskNotification() {
        return taskNotification;
    }
}
