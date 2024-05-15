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

    public static String[] fillCategories(boolean allFlag) {
        String[] categories;
        Categories[] c = Categories.values();

        if (allFlag) {
            categories = new String[c.length+1];
            categories[0] = "All";
            for (int i = 0; i < c.length; i++) {
                categories[i+1] = c[i].toString();
            }
        } else {
            categories = new String[c.length];
            for (int i = 0; i < c.length; i++) {
                categories[i] = c[i].toString();
            }
        }

        return categories;
    }
}
