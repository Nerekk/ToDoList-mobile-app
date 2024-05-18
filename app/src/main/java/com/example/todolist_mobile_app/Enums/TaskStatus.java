package com.example.todolist_mobile_app.Enums;

public enum TaskStatus {
    All,
    Todo,
    Done;

    public static TaskStatus fromString(String value) {
        for (TaskStatus taskStatus : TaskStatus.values()) {
            if (taskStatus.name().equalsIgnoreCase(value)) {
                return taskStatus;
            }
        }
        throw new IllegalArgumentException("No enum constant with value " + value);
    }

    public static String[] fillTaskStatus() {
        String[] taskTypes;
        TaskStatus[] s = TaskStatus.values();

        taskTypes = new String[s.length];
        for (int i = 0; i < s.length; i++) {
            taskTypes[i] = s[i].toString();
        }

        return taskTypes;
    }
}
