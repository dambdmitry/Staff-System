package org.internship.system.models;

public enum Permission {
    READ("read"),
    CHANGE("change");
    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
