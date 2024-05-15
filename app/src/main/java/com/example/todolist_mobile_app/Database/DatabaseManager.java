package com.example.todolist_mobile_app.Database;

import android.content.Context;

import androidx.room.Room;

import com.example.todolist_mobile_app.Interfaces.TaskDataDao;
import com.example.todolist_mobile_app.Data.TaskData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DatabaseManager {
    private static TaskDatabase database;
    private static Executor executor = Executors.newSingleThreadExecutor();

    public static synchronized TaskDatabase initDatabase(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(
                    context.getApplicationContext(),
                    TaskDatabase.class,
                    "tasks_db").allowMainThreadQueries().build();
        }
        return database;
    }

    public static synchronized void insert(TaskData data) {
        executor.execute(() -> {
            if (database == null) return;
            TaskDataDao dao = database.taskDataDao();
            dao.insertTask(data);
        });
    }

    public static synchronized void delete(TaskData data) {
        executor.execute(() -> {
            if (database == null) return;
            TaskDataDao dao = database.taskDataDao();
            dao.deleteTask(data);
        });
    }

//    public static synchronized void getAll(DataCallback callback) {
//        executor.execute(() -> {
//            if (database == null) return;
//            TaskDataDao dao = database.taskDataDao();
//            List<TaskData> tasks = dao.getAll();
//            callback.onDataLoaded(tasks);
//        });
//    }

    public static synchronized List<TaskData> getAll() {
        if (database == null) return null;
        TaskDataDao dao = database.taskDataDao();
        return dao.getAll();
//        callback.onDataLoaded(tasks);
    }
}
