package com.example.todolist_mobile_app.Recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist_mobile_app.R;

import java.util.List;
import java.util.Objects;

public class TaskListAdapter extends RecyclerView.Adapter<TaskViewHolder> {
    List<TaskData> tasks;
    Context context;


    public TaskListAdapter(List<TaskData> tasks, Context context) {
        this.tasks = tasks;
        this.context = context;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.task_card, parent, false);

        return new TaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
//        int index = holder.getAdapterPosition();

        holder.taskTitle.setText(tasks.get(position).getTitle());
        holder.taskDate.setText(tasks.get(position).getStartTimeFormatted());
        holder.taskCategory.setText(tasks.get(position).getCategory().toString());
        if (Objects.equals(tasks.get(position).getStatus(), TaskData.DONE)) {
            holder.taskStatus.setImageResource(R.mipmap.ic_done);
        } else {
            holder.taskStatus.setImageResource(R.mipmap.ic_incomplete);
        }

        if (tasks.get(position).isNotifyOn()) {
            holder.taskNotification.setImageResource(R.mipmap.ic_notifications_on);
        } else {
            holder.taskNotification.setImageResource(R.mipmap.ic_notifications_off);
        }

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
