package com.example.todolist_mobile_app.Recycler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist_mobile_app.Data.TaskData;
import com.example.todolist_mobile_app.Dialogs.DialogInfo;
import com.example.todolist_mobile_app.Enums.Categories;
import com.example.todolist_mobile_app.R;
import com.example.todolist_mobile_app.Utils.DateFormatter;

import java.util.List;
import java.util.Objects;

public class TaskListAdapter extends RecyclerView.Adapter<TaskViewHolder> {
    List<TaskData> tasks;
    List<TaskData> originalTasks;
    Context context;


    public TaskListAdapter(List<TaskData> tasks, Context context) {
        this.tasks = tasks;
        this.originalTasks = tasks;
        this.context = context;
    }

    public void setTasks(List<TaskData> tasks) {
        this.originalTasks = tasks;
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
        int index = holder.getAdapterPosition();

        holder.setTaskId(tasks.get(position).getId());

        holder.taskTitle.setText(tasks.get(position).getTitle());
        holder.taskDate.setText(DateFormatter.getFullToString(tasks.get(position).getStartTime()));
        holder.taskCategory.setText(tasks.get(position).getCategory().toString());
        if (Objects.equals(tasks.get(position).getStatus(), TaskData.DONE)) {
            holder.taskStatus.setImageResource(R.mipmap.ic_done);
        } else {
            holder.taskStatus.setImageResource(R.mipmap.ic_incomplete);
        }

        if (tasks.get(position).getNotification().getValue() != 0) {
            holder.taskNotification.setImageResource(R.mipmap.ic_notifications_on);
        } else {
            holder.taskNotification.setImageResource(R.mipmap.ic_notifications_off);
        }

        holder.itemView.setOnClickListener(view -> {
            TaskData task = tasks.get(index);
            new DialogInfo(context, TaskListAdapter.this, task);
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void filter(String category, String query) {
        tasks.clear();
        for (TaskData data : originalTasks) {
            if (category.equalsIgnoreCase("all")) {
                if (query.isEmpty() || data.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    tasks.add(data);
                }
            } else {
                String c = data.getCategory().toString();
                if (c.equalsIgnoreCase(category) && data.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    tasks.add(data);
                }
            }

        }
        notifyDataSetChanged();
    }
}
