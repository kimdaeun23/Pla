package com.example.plantapp;

import android.widget.ImageView;

public class Dictionary {
    public String name="";
    public String water="";
    public String temp="";
    public String tip="";
    public String imageurl="";
    public String place="";

    public Dictionary(String name, String water, String temp, String tip, String imageurl, String place) {
        this.name = name;
        this.water = water;
        this.temp = temp;
        this.tip = tip;
        this.imageurl=imageurl;
        this.place=place;
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
}
