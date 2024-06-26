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

    public static synchronized TaskDatabase initDatabase(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(
                    context.getApplicationContext(),
                    TaskDatabase.class,
                    "tasks_db").allowMainThreadQueries().build();
        }
        return database;
    }

    public static synchronized long insert(TaskData data) {
        if (database == null) return -1;
        TaskDataDao dao = database.taskDataDao();
        return dao.insertTask(data);
    }

    public static synchronized void delete(TaskData data) {
        if (database == null) return;
        TaskDataDao dao = database.taskDataDao();
        dao.deleteTask(data);
    }

    public static synchronized void deleteById(int taskId) {
        if (database == null) return;
        TaskDataDao dao = database.taskDataDao();
        dao.deleteTaskById(taskId);
    }


    public static synchronized List<TaskData> getAll() {
        if (database == null) return null;
        TaskDataDao dao = database.taskDataDao();
        return dao.getAll();
    }

    public static synchronized TaskData getTaskById(int id) {
        if (database == null) return null;
        TaskDataDao dao = database.taskDataDao();
        return dao.getTaskById(id);
    }

    public static synchronized int getFirstUnusedId() {
        if (database == null) {
            // Zwróć 1 jako domyślny ID, gdy baza danych nie została jeszcze zainicjowana
            return -1;
        }

        // Uzyskaj dostęp do Dao z bazy danych
        TaskDataDao taskDataDao = database.taskDataDao();

        // Wykonaj zapytanie w celu znalezienia pierwszego nieużywanego ID
        int firstUnusedId = taskDataDao.getMaxId() + 1;


        return firstUnusedId;
    }
}
