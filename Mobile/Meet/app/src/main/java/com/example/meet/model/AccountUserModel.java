package com.example.meet.model;

public class AccountUserModel {
    private String email;
    private String password;
    private boolean emailVerified;
    private String name;
    private boolean disabled;

    public AccountUserModel() {
    }

    public AccountUserModel(String email, String password, boolean emailVerified, String name, boolean disabled) {
        this.email = email;
        this.password = password;
        this.emailVerified = emailVerified;
        this.name = name;
        this.disabled = disabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
