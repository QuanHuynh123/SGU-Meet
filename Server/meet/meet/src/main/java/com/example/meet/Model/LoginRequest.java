package com.example.meet.Model;

public class LoginRequest {
    private String idToken;

    public LoginRequest() {
    }

    public LoginRequest(String idToken) {
        this.idToken = idToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
