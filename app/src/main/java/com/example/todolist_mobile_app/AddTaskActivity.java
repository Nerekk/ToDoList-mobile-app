package com.example.todolist_mobile_app;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist_mobile_app.Database.DatabaseManager;
import com.example.todolist_mobile_app.Database.TaskDatabase;
import com.example.todolist_mobile_app.Recycler.RecyclerViewManager;

public class AddTaskActivity extends AppCompatActivity {
    public TaskDatabase db;
    EditText addTitle, addDesc, timeText, dateText;
    Spinner spinnerCategory, spinnerNotifs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_task_add);

        db = DatabaseManager.initDatabase(this);

        findViewById(R.id.addTaskButton).setOnClickListener(this::saveTaskAndReturn);
    }

    public void mapComponents() {

    }

    public void saveTaskAndReturn(View view) {
        // saving
        finish();
    }
}
