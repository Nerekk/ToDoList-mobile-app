package com.example.todolist_mobile_app.RecyclerAttachment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist_mobile_app.R;

public class AttachmentViewHolder extends RecyclerView.ViewHolder {
    TextView attachmentName;
    ImageView ivFile;
    private int id;

    public AttachmentViewHolder(@NonNull View itemView) {
        super(itemView);
        attachmentName = itemView.findViewById(R.id.attachmentName);
        ivFile = itemView.findViewById(R.id.ivFile);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAttachmentName() {
        return attachmentName.toString();
    }
}
