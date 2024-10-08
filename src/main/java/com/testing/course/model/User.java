package com.testing.course.model;

public class User {
    private boolean valid;
    private String username;

    public User(boolean valid, String username) {
        this.valid = valid;
        this.username = username;
    }

    public User() {

    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "valid=" + valid +
                ", username='" + username + '\'' +
                '}';
    }
}
