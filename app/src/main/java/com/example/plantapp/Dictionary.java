package com.example.plantapp;

import android.widget.ImageView;

public class Dictionary {
    public String name="";
    public String water="";
    public String temp="";
    public String tip="";
    public String imageurl="";
    public String place="";
    public String sp_dictionary="";

    public Dictionary(String name, String water, String temp, String tip, String imageurl, String place, String sp_dictionary) {
        this.name = name;
        this.water = water;
        this.temp = temp;
        this.tip = tip;
        this.imageurl=imageurl;
        this.place=place;
        this.sp_dictionary=sp_dictionary;
    }

    public Dictionary(){

    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getSp_dictionary() {
        return sp_dictionary;
    }

    public void setSp_dictionary(String sp_dictionary) {
        this.sp_dictionary = sp_dictionary;
    }
}
