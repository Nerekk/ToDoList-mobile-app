package com.example.todolist_mobile_app.Enums;

public enum Notifications {
    OFF(0),
    MINUTES_1(1),
    MINUTES_5(5),
    MINUTES_15(15),
    MINUTES_30(30);

    private final int value;

    Notifications(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Notifications fromValue(String s) {
        if (s.equalsIgnoreCase("off")) return OFF;
        String value = s.split(" ")[0];
        for (Notifications n : Notifications.values()) {
            if (String.valueOf(n.value).equals(value)) {
                return n;
            }
        }
        throw new IllegalArgumentException("No enum constant with value " + value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
