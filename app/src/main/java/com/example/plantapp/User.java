package com.example.plantapp;

import java.util.ArrayList;

public class User {
    String email="";
    String nickname="";
    String id="";
    String imageurl="";

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
//
//    public User(String email, String nickname, String id){
//        this.email=email;
//        this.nickname=nickname;
//        this.id=id;
//
//    }
    public User(){
        this.email=email;
        this.nickname=nickname;
        this.id=id;
        this.imageurl=imageurl;
    }
}
