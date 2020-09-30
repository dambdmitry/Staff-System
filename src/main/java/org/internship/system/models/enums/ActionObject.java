package org.internship.system.models.enums;

public enum ActionObject {
    EMPLOYEE("employee"),
    DEPARTMENT("department"),
    ORDER("order");

    private final String name;

    ActionObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
