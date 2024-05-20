package com.example.todolist_mobile_app.Dialogs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist_mobile_app.AddingActivity.AddTaskActivity;
import com.example.todolist_mobile_app.Data.FileModel;
import com.example.todolist_mobile_app.Database.DatabaseManager;
import com.example.todolist_mobile_app.Enums.Notifications;
import com.example.todolist_mobile_app.MainActivity;
import com.example.todolist_mobile_app.R;
import com.example.todolist_mobile_app.Data.TaskData;
import com.example.todolist_mobile_app.Recycler.RecyclerViewManager;
import com.example.todolist_mobile_app.Recycler.TaskListAdapter;
import com.example.todolist_mobile_app.RecyclerAttachment.AttachmentListAdapter;
import com.example.todolist_mobile_app.RecyclerAttachment.RecyclerViewAttachmentManager;
import com.example.todolist_mobile_app.Utils.DateFormatter;
import com.example.todolist_mobile_app.Utils.FileManager;

import java.util.ArrayList;
import java.util.List;

public class DialogInfoFragment extends DialogFragment {
    private TaskData task;
    private TaskListAdapter adapterTask;
    private AttachmentListAdapter adapterAttach;
    private MainActivity activity;
    private RecyclerViewAttachmentManager rvManagerAttachment;
    private RecyclerViewManager rvManagerTask;
    private RecyclerView recyclerView;
    private FileManager fm;
    private List<FileModel> allFiles;
    private ImageView ivEdit, ivSetStatus, ivAddAttachment;

    public static DialogInfoFragment newInstance(TaskData task) {
        DialogInfoFragment fragment = new DialogInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable("task", task);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            activity = (MainActivity) context;
        } else {
            throw new RuntimeException(context + " is not a MainActivity");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_task_info, container, false);
        setRetainInstance(true);
        if (getArguments() != null) {
            task = (TaskData) getArguments().getSerializable("task");
        }
        rvManagerTask = activity.getRvManager();
        prepareRecycler(view);
        prepareDialog(view);
        setListeners(view);

        return view;
    }

    private void prepareRecycler(View view) {
        recyclerView = view.findViewById(R.id.rvDialogInfo);
        fm = activity.getFileManager();
        allFiles = fm.getAllFilesForTask(task.getId());
        adapterAttach = new AttachmentListAdapter(allFiles, activity);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapterAttach);
        rvManagerAttachment = new RecyclerViewAttachmentManager(activity, fm, task.getId());
        rvManagerAttachment.setRvManager(recyclerView, adapterAttach, allFiles);
        rvManagerAttachment.prepareSwipe();
    }

    private void prepareDialog(View view) {
        getDialog().setContentView(R.layout.dialog_task_info);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ((TextView)view.findViewById(R.id.dialogTitle)).setText(task.getTitle());
        ((TextView)view.findViewById(R.id.dialogDesc)).setText(task.getDescription());
        ((TextView)view.findViewById(R.id.dialogCategory)).setText(task.getCategory().toString());
        ((TextView)view.findViewById(R.id.dialogNotifs)).setText(task.getNotificationStatus());
        ((TextView)view.findViewById(R.id.dialogEnd)).setText(DateFormatter.getFullToString(task.getEndTime()));
        ((TextView)view.findViewById(R.id.dialogStart)).setText(DateFormatter.getFullToString(task.getStartTime()));
    }

    private void setListeners(View view) {
        view.findViewById(R.id.dialogInfoBackground).setOnClickListener(v -> dismiss());

        // empty listener to override background dismiss
        view.findViewById(R.id.dialogInfoView).setOnClickListener(v -> {});

        ivEdit = view.findViewById(R.id.ivEdit);
        ivEdit.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddTaskActivity.class);
            intent.putExtra(TaskData.ID, task.getId());
            dismiss();
            getActivity().startActivityForResult(intent, MainActivity.REQUEST_CODE_TASK_ACTIVITY);
        });

        ivSetStatus = view.findViewById(R.id.ivDialogInfoSetStatus);
        if (task.isFinished()) {
            ivSetStatus.setImageResource(R.mipmap.ic_incomplete);
        } else {
            ivSetStatus.setImageResource(R.mipmap.ic_done);
        }
        ivAddAttachment = view.findViewById(R.id.ivDialogInfoAddAttachment);
        ivSetStatus.setOnClickListener(v -> {
            // ustaw status taska na przeciwny i ustaw notyfikacje na off
            task.setFinished(!task.isFinished());
            task.setNotification(Notifications.OFF);
            DatabaseManager.insert(task);
            activity.getNotificationHelper().cancelNotification(task.getId());
            rvManagerTask.getDataFromDBAndUpdateAdapter();
            dismiss();
        });

        ivAddAttachment.setOnClickListener(this::openFilePicker);
    }

    private void openFilePicker(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Choose files"), MainActivity.REQUEST_CODE_SELECT_FILES);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MainActivity.REQUEST_CODE_SELECT_FILES && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            processSelectedFiles(data);
        }
    }

    private void processSelectedFiles(@NonNull Intent data) {
        List<Uri> selectedUris = new ArrayList<>();
        if (data.getClipData() != null) {
            int count = data.getClipData().getItemCount();
            for (int i = 0; i < count; i++) {
                selectedUris.add(data.getClipData().getItemAt(i).getUri());
                fm.validateRepeats(selectedUris, allFiles);
            }
        } else if (data.getData() != null) {
            selectedUris.add(data.getData());
            fm.validateRepeats(selectedUris, allFiles);
        }

        List<FileModel> filesToAdd = fm.convertUrisToFileModels(selectedUris, task.getId());
        allFiles.addAll(filesToAdd);
        rvManagerAttachment.updateData(allFiles);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

