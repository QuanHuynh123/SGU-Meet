package com.example.meet.model;

public class LoginRequest {
    private String idToken;

    public LoginRequest(String idToken) {
        this.idToken = idToken;
    }

    public LoginRequest() {
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}