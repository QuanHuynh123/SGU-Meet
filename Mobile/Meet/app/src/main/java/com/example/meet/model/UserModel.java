package com.example.meet.model;

import com.google.firebase.Timestamp;

import java.util.List;

public class UserModel {
    private String email;
    private String gender;
    private int age;
    private String name;
    private List<String> friendRequests;
    private List<String> sentFriendRequests;
    private List<String> acceptedFriendRequests;
    private List<String> friendList;
    private Timestamp createdTimestamp;
    private String userId;

    public UserModel() {
    }

    public UserModel(String email, String gender, int age, String name, List<String> friendRequests, List<String> sentFriendRequests, List<String> acceptedFriendRequests, List<String> friendList, Timestamp createdTimestamp, String userId) {
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.name = name;
        this.friendRequests = friendRequests;
        this.sentFriendRequests = sentFriendRequests;
        this.acceptedFriendRequests = acceptedFriendRequests;
        this.friendList = friendList;
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

    public List<String> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(List<String> friendRequests) {
        this.friendRequests = friendRequests;
    }

    public List<String> getSentFriendRequests() {
        return sentFriendRequests;
    }

    public void setSentFriendRequests(List<String> sentFriendRequests) {
        this.sentFriendRequests = sentFriendRequests;
    }

    public List<String> getAcceptedFriendRequests() {
        return acceptedFriendRequests;
    }

    public void setAcceptedFriendRequests(List<String> acceptedFriendRequests) {
        this.acceptedFriendRequests = acceptedFriendRequests;
    }

    public List<String> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<String> friendList) {
        this.friendList = friendList;
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
                ", friendRequests=" + friendRequests +
                ", sentFriendRequests=" + sentFriendRequests +
                ", acceptedFriendRequests=" + acceptedFriendRequests +
                ", friendList=" + friendList +
                ", createdTimestamp=" + createdTimestamp +
                ", userId='" + userId + '\'' +
                '}';
    }
}
