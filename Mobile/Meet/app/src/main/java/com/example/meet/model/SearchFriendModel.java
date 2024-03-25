package com.example.meet.model;

public class SearchFriendModel {
    private  int img ;
    private String nameFriend;

    public SearchFriendModel(int img, String nameFriend) {
        this.img = img;
        this.nameFriend = nameFriend;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getNameFriend() {
        return nameFriend;
    }

    public void setNameFriend(String nameFriend) {
        this.nameFriend = nameFriend;
    }
}
