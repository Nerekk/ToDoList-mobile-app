package com.example.todolist_mobile_app.AddingActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist_mobile_app.Category;
import com.example.todolist_mobile_app.Database.DatabaseManager;
import com.example.todolist_mobile_app.Database.TaskDatabase;
import com.example.todolist_mobile_app.R;
import com.example.todolist_mobile_app.Recycler.RecyclerViewManager;

public class AddTaskActivity extends AppCompatActivity {
    public TaskDatabase db;
    EditText addTitle, addDesc, timeText, dateText;
    Spinner spinnerCategory, spinnerNotifs;
    ArrayAdapter<String> catAdapter, notifAdapter;
    ImageView ivClock, ivCalendar;

    private String[] categories;
    private String[] notifications = {"Off", "1 minute", "5 minutes", "15 minutes", "1 hour"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_task_add);

        db = DatabaseManager.initDatabase(this);
        mapComponents();
        setSpinners();
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

    private void setSpinners() {
        fillCategories();
        catAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, categories);
//        catAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        notifAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, notifications);
//        notifAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

        spinnerCategory.setAdapter(catAdapter);
        spinnerNotifs.setAdapter(notifAdapter);
    }

    private void setListeners() {

    }

    private void fillCategories() {
        Category[] c = Category.values();
        categories = new String[c.length];
        for (int i = 0; i < c.length; i++) {
            categories[i] = c[i].toString();
        }
    }


    public void saveTaskAndReturn(View view) {
        // saving
        finish();
    }
}
