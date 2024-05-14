package com.example.todolist_mobile_app.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.todolist_mobile_app.Database.Converters.LocalDateTimeConverter;
import com.example.todolist_mobile_app.Interfaces.TaskDataDao;
import com.example.todolist_mobile_app.Recycler.TaskData;

@Database(entities = {TaskData.class}, version = 1, exportSchema = false)
@TypeConverters(LocalDateTimeConverter.class)
public abstract class TaskDatabase extends RoomDatabase {
    public abstract TaskDataDao taskDataDao();
}
