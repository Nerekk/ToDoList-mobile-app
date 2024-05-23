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

    public static String getStringNotification(Notifications notifications) {
        int v = notifications.getValue();
        if (v == 0) return "Off";
        return v + " minutes";
    }

    public int getIndex() {
        Notifications[] values = Notifications.values();
        for (int i = 0; i < values.length; i++) {
            if (values[i] == this) {
                return i;
            }
        }
        return -1;
    }

    public static String[] fillNotifications() {
        Notifications[] n = Notifications.values();
        String[] notifications = new String[n.length];
        for (int i = 0; i < n.length; i++) {
            if (n[i].getValue() == 0) {
                notifications[i] = "Off";
                continue;
            }
            notifications[i] = n[i].getValue() + " mins";
        }
        return notifications;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
