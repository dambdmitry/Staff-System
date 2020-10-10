package edu.prctice.staff.models.enums;

public enum ActionByUser {
    ADDITION("addition"),
    REMOVE("remove");

    private final String name;

    ActionByUser(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
