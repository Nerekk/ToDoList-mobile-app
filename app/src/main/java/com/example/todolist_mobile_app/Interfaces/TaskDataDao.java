package com.example.todolist_mobile_app.Interfaces;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.todolist_mobile_app.Data.TaskData;

import java.util.List;

@Dao
public interface TaskDataDao {
    @Query("SELECT * FROM tasks_table")
    List<TaskData> getAll();

    @Query("SELECT * FROM tasks_table WHERE id = :id")
    TaskData getTaskById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(TaskData taskData);

    @Delete
    void deleteTask(TaskData taskData);
}
