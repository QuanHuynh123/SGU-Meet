package com.example.meet.model;

import com.google.firebase.Timestamp;

import java.sql.Time;

public class UserModel {
    private String email;
    private String gender;
    private int age;
    private String name;
    private Timestamp createdTimestamp;
    private  String userId;

    public UserModel() {
    }

    public UserModel(String email, String gender, int age, String name, Timestamp createdTimestamp, String userId) {
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.name = name;
        this.createdTimestamp = createdTimestamp;
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    @Override
    public String toString() {
        return "UserModel{" +
                "email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", name='" + name + '\'' +
                ", createdTimestamp=" + createdTimestamp +
                ", userId='" + userId + '\'' +
                '}';
    }
}
