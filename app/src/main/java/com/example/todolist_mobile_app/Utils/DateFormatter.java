package com.example.todolist_mobile_app.Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatter {
    public static String getFullToString(LocalDateTime fullTime) {
        if (fullTime == null) return "None";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return fullTime.format(formatter);
    }

    public static String getTimeToString(LocalDateTime time) {
        if (time == null) return "None";
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(timeFormatter);
    }

    public static String getDateToString(LocalDateTime date) {
        if (date == null) return "None";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(dateFormatter);
    }

    public static LocalDateTime getFullToClass(String date, String time) {
        String dateTimeString = date + " " + time;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(dateTimeString, dateTimeFormatter);
    }
}
