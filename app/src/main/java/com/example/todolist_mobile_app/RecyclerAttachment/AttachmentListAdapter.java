package com.example.todolist_mobile_app.RecyclerAttachment;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist_mobile_app.AddingActivity.AddTaskActivity;
import com.example.todolist_mobile_app.Data.FileModel;
import com.example.todolist_mobile_app.MainActivity;
import com.example.todolist_mobile_app.R;

import java.io.File;
import java.util.List;

public class AttachmentListAdapter extends RecyclerView.Adapter<AttachmentViewHolder> {
    List<FileModel> files;
    MainActivity activity;

    public AttachmentListAdapter(List<FileModel> files, MainActivity activity) {
        this.files = files;
        this.activity = activity;
    }

    public void setFiles(List<FileModel> files) {
        this.files = files;
    }

    @NonNull
    @Override
    public AttachmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity.getApplication());
        View v = layoutInflater.inflate(R.layout.attachment_card, parent, false);

        return new AttachmentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AttachmentViewHolder holder, int position) {
        holder.setId(position);
        holder.attachmentName.setText(files.get(position).getFileName());
        holder.ivFile.setImageResource(R.mipmap.ic_file);
        holder.itemView.setOnClickListener(view -> openFile(files.get(position)));
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    private void openFile(FileModel fileModel) {
        File file = new File(fileModel.getFilePath());
        Uri fileUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, getMimeType(fileUri.toString()));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        } else {
            Toast.makeText(activity, "No app found to open this file", Toast.LENGTH_SHORT).show();
        }
    }

    private String getMimeType(String url) {
        String type = null;
        String extension = url.substring(url.lastIndexOf(".") + 1);
        if (extension != null) {
            type = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
}
