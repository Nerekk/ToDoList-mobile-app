package com.example.todolist_mobile_app.Data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.todolist_mobile_app.Enums.Categories;
import com.example.todolist_mobile_app.Enums.Notifications;
import com.example.todolist_mobile_app.Utils.DateFormatter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity(tableName = "tasks_table")
public class TaskData implements Serializable {
    @PrimaryKey
    private int id;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isFinished;
    private Notifications notification;
    private Categories category;
    // one more field for files/images


    public final static String DONE = "Done";
    public final static String INCOMPLETE = "Incomplete";
    public final static String ID = "taskId";
    public final static String NOTIFY_ID = "notifyId";
    public final static String NOTIFY_OPERATION = "notifyOperation";

    @Ignore
    public TaskData(String title, String description, boolean isFinished, Categories category) {
        this.title = title;
        this.description = description;
        this.isFinished = isFinished;
        this.category = category;

        this.startTime = LocalDateTime.now();
        this.notification = Notifications.OFF;
    }

    public TaskData(String title,
                    String description,
                    LocalDateTime endTime,
                    boolean isFinished,
                    Notifications notification,
                    Categories category) {
        this.title = title;
        this.description = description;
        this.startTime = LocalDateTime.now();
        this.endTime = endTime;
        this.isFinished = isFinished;
        this.notification = notification;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public Notifications getNotification() {
        return notification;
    }

    public void setNotification(Notifications notification) {
        this.notification = notification;
    }

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }

    public LocalDateTime setEndTimeFromString(String date, String time) {
        String dateTimeString = date + " " + time;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTimeString, dateTimeFormatter);
    }

    public String getStatus() {
        if (isFinished) {
            return DONE;
        } else {
            return INCOMPLETE;
        }
    }

    public String getNotificationStatus() {
        return Notifications.getStringNotification(notification);
    }

    @Override
    public String toString() {
        return "TaskData{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
