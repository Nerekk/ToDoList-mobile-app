package com.example.todolist_mobile_app;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
    ImageView ivClock, ivCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_task_add);

        db = DatabaseManager.initDatabase(this);
        mapComponents();
        setListeners();
    }

    private void mapComponents() {
        findViewById(R.id.addTaskButton).setOnClickListener(this::saveTaskAndReturn);

        addTitle = findViewById(R.id.addTitle);
        addDesc = findViewById(R.id.addDesc);
        timeText = findViewById(R.id.timeText);
        dateText = findViewById(R.id.dateText);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerNotifs = findViewById(R.id.spinnerNotifs);

        ivClock = findViewById(R.id.ivClock);
        ivCalendar = findViewById(R.id.ivCalendar);
    }

    private void setListeners() {

    }

    public void saveTaskAndReturn(View view) {
        // saving
        finish();
    }
}
