package com.example.todolist_mobile_app.Recycler;

import com.example.todolist_mobile_app.Category;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskData {
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isFinished;
    private boolean isNotifyOn;
    private Category category;
    // one more field for files/images


    public final static String DONE = "Done";
    public final static String INCOMPLETE = "Incomplete";

    public TaskData(String title, String description, boolean isFinished, Category category) {
        this.title = title;
        this.description = description;
        this.isFinished = isFinished;
        this.category = category;

        this.startTime = LocalDateTime.now();
        this.isNotifyOn = false;
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

    public boolean isNotifyOn() {
        return isNotifyOn;
    }

    public void setNotifyOn(boolean notifyOn) {
        isNotifyOn = notifyOn;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getStartTimeFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return startTime.format(formatter);
    }

    public String getEndTimeFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return endTime.format(formatter);
    }

    public String getStatus() {
        if (isFinished) {
            return DONE;
        } else {
            return INCOMPLETE;
        }
    }
}
