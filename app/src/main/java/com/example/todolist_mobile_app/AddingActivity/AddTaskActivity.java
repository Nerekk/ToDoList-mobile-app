package com.example.todolist_mobile_app.AddingActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist_mobile_app.Enums.Categories;
import com.example.todolist_mobile_app.Database.DatabaseManager;
import com.example.todolist_mobile_app.Database.TaskDatabase;
import com.example.todolist_mobile_app.Enums.Notifications;
import com.example.todolist_mobile_app.R;
import com.example.todolist_mobile_app.Data.TaskData;
import com.example.todolist_mobile_app.Utils.DateFormatter;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {
    public TaskDatabase db;
    EditText addTitle, addDesc, timeText, dateText;
    Spinner spinnerCategory, spinnerNotifs;
    ArrayAdapter<String> catAdapter, notifAdapter;
    ImageView ivClock, ivCalendar, ivBack, ivSave;
    private int hour, minute;
    DatePickerDialog datePickerDialog;
    ToggleButton toggleButton;

    private TaskData editTask;
    private int taskId;

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

        fillDataIfSent();
    }

    private void fillDataIfSent() {
        Intent intent = getIntent();
        taskId = intent.getIntExtra(TaskData.ID, -1);

        if (taskId != -1) {
            fillActivity(DatabaseManager.getTaskById(taskId));
        }
    }

    private void fillActivity(TaskData task) {
        editTask = task;

        addTitle.setText(task.getTitle());
        addDesc.setText(task.getDescription());
        timeText.setText(DateFormatter.getTimeToString(task.getEndTime()));
        dateText.setText(DateFormatter.getDateToString(task.getEndTime()));

        spinnerCategory.setSelection(task.getCategory().ordinal());
        spinnerNotifs.setSelection(task.getNotification().getIndex());

        if (task.isFinished()) {
            toggleButton.setChecked(true);
        } else {
            toggleButton.setChecked(false);
        }
    }


    private void mapComponents() {
        addTitle = findViewById(R.id.addTitle);
        addDesc = findViewById(R.id.addDesc);

        timeText = findViewById(R.id.timeText);
        timeText.setText(DateFormatter.getTimeToString(LocalDateTime.now()));

        dateText = findViewById(R.id.dateText);
        dateText.setText(DateFormatter.getDateToString(LocalDateTime.now()));

        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerNotifs = findViewById(R.id.spinnerNotifs);

        ivClock = findViewById(R.id.ivClock);
        ivCalendar = findViewById(R.id.ivCalendar);

        toggleButton = findViewById(R.id.toggleButton);

        ivBack = findViewById(R.id.backIcon);
        ivSave = findViewById(R.id.doneIcon);
    }

    private void setSpinners() {
        categories = Categories.fillCategories(false);
        notifications = Notifications.fillNotifications();
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
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String formattedDate = date.format(formatter);
                dateText.setText(formattedDate);
            }
        };

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
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
//        int style = AlertDialog.THEME_HOLO_LIGHT;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Select time");
        timePickerDialog.show();
    }

    private void setListeners() {
        ivClock.setOnClickListener(view -> showTimePicker());
        ivCalendar.setOnClickListener(view -> showDatePicker());

        ivBack.setOnClickListener(view -> finish());
        ivSave.setOnClickListener(this::saveTaskAndReturn);
    }


    public void saveTaskAndReturn(View view) {
        int notifyTime = Notifications.fromValue(spinnerNotifs.getSelectedItem().toString()).getValue();
        if (!isEndTimeValid(getDateTimeFromActivity(), notifyTime) && notifyTime!=0) {
            Toast.makeText(this, "To use notifications, the time cannot be earlier than the present!", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent resultIntent = new Intent();

        if (taskId == -1) {
            if (handleNewTaskCreation(resultIntent)) return;

        } else {
            if (handleExistingTaskUpdate(resultIntent)) return;
        }


        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private boolean handleExistingTaskUpdate(Intent resultIntent) {
        if (!updateTask(editTask)) {
            Toast.makeText(this, "All fields must be filled except description!", Toast.LENGTH_SHORT).show();
            return true;
        }
        Log.i("DB_INSERT", "Inserting");
        long notifyId = DatabaseManager.insert(editTask);
        Log.i("MY_TEST", "notifyId: " + notifyId + " N: " + editTask.getNotification());
        resultIntent.putExtra(TaskData.NOTIFY_ID, (int) notifyId);
        if (editTask.getNotification() != Notifications.OFF) {
            resultIntent.putExtra(TaskData.NOTIFY_OPERATION, 1);
        } else {
            resultIntent.putExtra(TaskData.NOTIFY_OPERATION, 0);
        }
        return false;
    }

    private boolean handleNewTaskCreation(Intent resultIntent) {
        TaskData newTask = createNewTask();
        if (newTask == null) {
            Toast.makeText(this, "All fields must be filled except description!", Toast.LENGTH_SHORT).show();
            return true;
        }
        Log.i("DB_INSERT", "Inserting");
        long notifyId = DatabaseManager.insert(newTask);
        Log.i("MY_TEST", "notifyId: " + notifyId + " N: " + newTask.getNotification());
        resultIntent.putExtra(TaskData.NOTIFY_ID, (int) notifyId);
        if (newTask.getNotification() != Notifications.OFF) {
            resultIntent.putExtra(TaskData.NOTIFY_OPERATION, 1);
        } else {
            resultIntent.putExtra(TaskData.NOTIFY_OPERATION, 0);
        }
        return false;
    }

    private TaskData createNewTask() {
        String title = addTitle.getText().toString();
        String desc = addDesc.getText().toString();
        String time = timeText.getText().toString();
        String date = dateText.getText().toString();
        if (title.isEmpty() || time.isEmpty() || date.isEmpty()) return null;

        Categories c = Categories.fromString(spinnerCategory.getSelectedItem().toString());
        Notifications n = Notifications.fromValue(spinnerNotifs.getSelectedItem().toString());
        boolean isFinished = toggleButton.isChecked();
        LocalDateTime endtime = DateFormatter.getFullToClass(date, time);


        return new TaskData(title, desc, endtime, isFinished, n, c);
    }

    private LocalDateTime getDateTimeFromActivity() {
        String time = timeText.getText().toString();
        String date = dateText.getText().toString();
        return DateFormatter.getFullToClass(date, time);
    }

    public static boolean isEndTimeValid(LocalDateTime endTime, int notifyTime) {
        LocalDateTime now = LocalDateTime.now(); // Aktualny czas
        LocalDateTime notifyTimeDateTime = endTime.minusMinutes(notifyTime);

        return !notifyTimeDateTime.isBefore(now);
    }

    private boolean updateTask(TaskData task) {
        String title = addTitle.getText().toString();
        String desc = addDesc.getText().toString();
        String time = timeText.getText().toString();
        String date = dateText.getText().toString();
        if (title.isEmpty() || time.isEmpty() || date.isEmpty()) return false;

        Categories c = Categories.fromString(spinnerCategory.getSelectedItem().toString());
        Notifications n = Notifications.fromValue(spinnerNotifs.getSelectedItem().toString());
        boolean isFinished = toggleButton.isChecked();
        LocalDateTime endtime = DateFormatter.getFullToClass(date, time);

        task.setTitle(title);
        task.setDescription(desc);
        task.setCategory(c);
        task.setNotification(n);
        task.setFinished(isFinished);
        task.setEndTime(endtime);
        return true;
    }
}
