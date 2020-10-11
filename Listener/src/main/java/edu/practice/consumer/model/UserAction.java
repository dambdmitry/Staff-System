package edu.practice.consumer.model;

public class UserAction {
    private String login;
    private String action;
    private String object;

    public UserAction() {
    }

    @Override
    public String toString() {
        return "UserAction{" +
                "login='" + login + '\'' +
                ", action='" + action + '\'' +
                ", object='" + object + '\'' +
                '}';
    }

    public UserAction(String login, String action, String object) {
        this.login = login;
        this.action = action;
        this.object = object;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}
