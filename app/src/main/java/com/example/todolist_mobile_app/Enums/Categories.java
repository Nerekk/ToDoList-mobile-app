package com.example.todolist_mobile_app.Enums;

public enum Categories {
    Work,
    School,
    Home,
    Health,
    Finances,
    Recreation,
    Other;

    public static Categories fromString(String value) {
        for (Categories category : Categories.values()) {
            if (category.name().equalsIgnoreCase(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("No enum constant with value " + value);
    }
}
