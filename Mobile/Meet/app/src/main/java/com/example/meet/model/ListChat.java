package com.example.meet.model;

public class ListChat {
    private int id;
    private String name ;
    private String chat;
    private String time ;

    public int getId() {
        return id;
    }

    public ListChat(int id, String name, String chat, String time) {
        this.id = id;
        this.name = name;
        this.chat = chat;
        this.time = time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
