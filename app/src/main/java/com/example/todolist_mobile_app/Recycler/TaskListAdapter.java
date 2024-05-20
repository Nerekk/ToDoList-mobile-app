package com.example.todolist_mobile_app.Recycler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist_mobile_app.Data.TaskData;
import com.example.todolist_mobile_app.Dialogs.DialogInfoFragment;
import com.example.todolist_mobile_app.Enums.OrderType;
import com.example.todolist_mobile_app.Enums.TaskStatus;
import com.example.todolist_mobile_app.MainActivity;
import com.example.todolist_mobile_app.R;
import com.example.todolist_mobile_app.Utils.DateFormatter;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class TaskListAdapter extends RecyclerView.Adapter<TaskViewHolder> {
    List<TaskData> tasks;
    List<TaskData> originalTasks;
    Context context;
    MainActivity activity;
    TextView listStatus;


    public TaskListAdapter(List<TaskData> tasks, MainActivity activity, TextView listStatus) {
        this.tasks = tasks;
        this.originalTasks = tasks;
        this.activity = activity;
        this.listStatus = listStatus;
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

        prepareTaskCard(holder, position, index);
    }

    private void prepareTaskCard(@NonNull TaskViewHolder holder, int position, int index) {
        holder.setTaskId(tasks.get(position).getId());

        holder.taskTitle.setText(tasks.get(position).getTitle());
        holder.taskDate.setText(DateFormatter.getFullToString(tasks.get(position).getEndTime()));
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

        if (tasks.get(position).isHasFiles()) {
            holder.taskAttachment.setVisibility(View.VISIBLE);
        } else {
            holder.taskAttachment.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(view -> {
            TaskData task = tasks.get(index);

            DialogInfoFragment dialogFragment = DialogInfoFragment.newInstance(task);
            dialogFragment.show(activity.getSupportFragmentManager(), "FilePickerDialogFragment");
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void filter(String category, String query, String taskStatus, String order) {
        tasks.clear();

        for (TaskData data : originalTasks) {
            boolean matchesCategory = category.equalsIgnoreCase(TaskStatus.All.toString()) || data.getCategory().toString().equalsIgnoreCase(category);
            boolean matchesQuery = query.isEmpty() || data.getTitle().toLowerCase().contains(query.toLowerCase());

            if (matchesCategory && matchesQuery) {
                tasks.add(data);
            }
        }

        if (!taskStatus.equalsIgnoreCase(TaskStatus.All.toString())) {
            boolean isFinished = taskStatus.equalsIgnoreCase(TaskStatus.Done.toString());
            tasks.removeIf(task -> task.isFinished() != isFinished);
        }

        if (order.equalsIgnoreCase(OrderType.Upcoming.toString())) {
            tasks.sort(Comparator.comparing(TaskData::getEndTime));
        } else if (order.equalsIgnoreCase(OrderType.Newest.toString())) {
            tasks.sort(Comparator.comparing(TaskData::getStartTime).reversed());
        }
        notifyDataSetChanged();

        if (tasks.size() == 0) {
            listStatus.setVisibility(View.VISIBLE);
        } else {
            listStatus.setVisibility(View.GONE);
        }
    }
}
