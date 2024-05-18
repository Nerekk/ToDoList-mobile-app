package com.example.todolist_mobile_app.Enums;

public enum OrderType {
    Closest,
    Newest;

    public static OrderType fromString(String value) {
        for (OrderType orderType : OrderType.values()) {
            if (orderType.name().equalsIgnoreCase(value)) {
                return orderType;
            }
        }
        throw new IllegalArgumentException("No enum constant with value " + value);
    }

    public static String[] fillOrderType() {
        String[] orderTypes;
        OrderType[] s = OrderType.values();

        orderTypes = new String[s.length];
        for (int i = 0; i < s.length; i++) {
            orderTypes[i] = s[i].toString();
        }

        return orderTypes;
    }
}
