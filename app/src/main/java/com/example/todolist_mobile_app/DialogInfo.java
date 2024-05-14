package com.example.todolist_mobile_app;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.todolist_mobile_app.Recycler.TaskData;

public class DialogInfo extends Dialog {
    private TaskData task;

    public DialogInfo(@NonNull Context context, TaskData task) {
        super(context);
        this.task = task;
        prepareDialog();
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
        ((TextView)findViewById(R.id.dialogEnd)).setText(task.getEndTimeFormatted());
        ((TextView)findViewById(R.id.dialogStart)).setText(task.getStartTimeFormatted());
    }
}
