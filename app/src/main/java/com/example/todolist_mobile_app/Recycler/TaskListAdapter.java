package com.example.todolist_mobile_app.Recycler;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        int index = holder.getAdapterPosition();

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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskData task = tasks.get(index);
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_task_info);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ((TextView)dialog.findViewById(R.id.dialogTitle)).setText(task.getTitle());
                ((TextView)dialog.findViewById(R.id.dialogDesc)).setText(task.getDescription());
                ((TextView)dialog.findViewById(R.id.dialogCategory)).setText(task.getCategory().toString());
                ((TextView)dialog.findViewById(R.id.dialogNotifs)).setText(task.getNotificationStatus());
                ((TextView)dialog.findViewById(R.id.dialogEnd)).setText(task.getEndTimeFormatted());
                ((TextView)dialog.findViewById(R.id.dialogStart)).setText(task.getStartTimeFormatted());

                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
