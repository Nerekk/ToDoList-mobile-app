package com.example.todolist_mobile_app.AddingActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist_mobile_app.Enums.Categories;
import com.example.todolist_mobile_app.Database.DatabaseManager;
import com.example.todolist_mobile_app.Database.TaskDatabase;
import com.example.todolist_mobile_app.Enums.Notifications;
import com.example.todolist_mobile_app.R;
import com.example.todolist_mobile_app.Data.TaskData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {
    public TaskDatabase db;
    EditText addTitle, addDesc, timeText, dateText;
    Spinner spinnerCategory, spinnerNotifs;
    ArrayAdapter<String> catAdapter, notifAdapter;
    ImageView ivClock, ivCalendar;
    private int hour, minute;
    DatePickerDialog datePickerDialog;

    private String[] categories;
    private String[] notifications;

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
        fillNotifications();
        catAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, categories);
//        catAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        notifAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, notifications);
//        notifAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

        spinnerCategory.setAdapter(catAdapter);
        spinnerNotifs.setAdapter(notifAdapter);
    }

    private void showDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;

                LocalDate date = LocalDate.of(year, month, day);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String formattedDate = date.format(formatter);
                dateText.setText(formattedDate);
            }
        };

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                timeText.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Select time");
        timePickerDialog.show();
    }

    private void setListeners() {
        ivClock.setOnClickListener(view -> showTimePicker());
        ivCalendar.setOnClickListener(view -> showDatePicker());
    }

    private void fillCategories() {
        Categories[] c = Categories.values();
        categories = new String[c.length];
        for (int i = 0; i < c.length; i++) {
            categories[i] = c[i].toString();
        }
    }

    private void fillNotifications() {
        Notifications[] n = Notifications.values();
        notifications = new String[n.length];
        for (int i = 0; i < n.length; i++) {
            if (n[i].getValue() == 0) {
                notifications[i] = "Off";
                continue;
            }
            notifications[i] = n[i].getValue() + " minutes";
        }
    }


    public void saveTaskAndReturn(View view) {
        TaskData newTask = createNewTask();
        DatabaseManager.insert(newTask);
        // saving
        setResult(1);
        finish();
    }

    private TaskData createNewTask() {
        Categories c = Categories.fromString(spinnerCategory.getSelectedItem().toString());
//        Notifications n = Notifications.fromValue(spinnerNotifs.getSelectedItem().toString());
        TaskData task = new TaskData(addTitle.getText().toString(), addDesc.getText().toString(), false, c);
        return task;
    }
}
