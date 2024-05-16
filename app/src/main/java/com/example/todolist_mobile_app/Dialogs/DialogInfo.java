package com.example.todolist_mobile_app.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.todolist_mobile_app.AddingActivity.AddTaskActivity;
import com.example.todolist_mobile_app.R;
import com.example.todolist_mobile_app.Data.TaskData;
import com.example.todolist_mobile_app.Recycler.TaskListAdapter;
import com.example.todolist_mobile_app.Utils.DateFormatter;

public class DialogInfo extends Dialog {
    private TaskData task;
    private TaskListAdapter adapter;
    private Context context;
    ImageView ivEdit;

    public DialogInfo(@NonNull Context context, TaskListAdapter adapter, TaskData task) {
        super(context);
        this.task = task;
        this.adapter = adapter;
        this.context = context;
        prepareDialog();
        setListeners();
        show();
    }

    public void prepareDialog() {
        setContentView(R.layout.dialog_task_info);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ((TextView)findViewById(R.id.dialogTitle)).setText(task.getTitle());
        ((TextView)findViewById(R.id.dialogDesc)).setText(task.getDescription());
        ((TextView)findViewById(R.id.dialogCategory)).setText(task.getCategory().toString());
        ((TextView)findViewById(R.id.dialogNotifs)).setText(task.getNotificationStatus());
        ((TextView)findViewById(R.id.dialogEnd)).setText(DateFormatter.getFullToString(task.getEndTime()));
        ((TextView)findViewById(R.id.dialogStart)).setText(DateFormatter.getFullToString(task.getStartTime()));

    }

    private void setListeners() {
        findViewById(R.id.dialogInfoBackground).setOnClickListener(view -> dismiss());

        // empty listener to override background dismiss
        findViewById(R.id.dialogInfoView).setOnClickListener(view -> {});

        ivEdit = findViewById(R.id.ivEdit);
        ivEdit.setOnClickListener(view -> {
            Intent intent = new Intent(context, AddTaskActivity.class);
            intent.putExtra(TaskData.ID, task.getId());
            dismiss();
            context.startActivity(intent);
        });
    }

}
